/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.hive.internal.client.repository;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.*;
import org.openhab.binding.hive.internal.client.dto.SessionDto;
import org.openhab.binding.hive.internal.client.dto.SessionsDto;
import org.openhab.binding.hive.internal.client.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link SessionRepository}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class DefaultSessionRepository implements SessionRepository {
    private final Logger logger = LoggerFactory.getLogger(DefaultSessionRepository.class);

    private final HiveApiRequestFactory requestFactory;

    public DefaultSessionRepository(
            final HiveApiRequestFactory requestFactory
    ) {
        Objects.requireNonNull(requestFactory);

        this.requestFactory = requestFactory;
    }

    private URI getEndpointPathForSession(final SessionId sessionId) {
        return HiveApiConstants.ENDPOINT_SESSION.resolve(sessionId.toString());
    }

    @Override
    public Session createSession(final Username username, final Password password) {
        /* Build our request entity with our user credentials */
        final SessionDto credentials = new SessionDto();
        credentials.username = username;
        credentials.password = password;

        final SessionsDto requestEntity = new SessionsDto();
        requestEntity.sessions = Collections.singletonList(credentials);

        /* Send our new session request to the Hive API. */
        final HiveApiResponse response = this.requestFactory.newRequest(HiveApiConstants.ENDPOINT_SESSIONS)
                .accept(MediaType.API_V6_5_0_JSON)
                .post(requestEntity);

        if (response.getStatusCode() == 400) {
            throw new HiveApiAuthenticationException(
                    "Creating a new session failed because an incorrect username or password was provided"
            );
        } else if (response.getStatusCode() != 200) {
            throw new HiveApiUnknownException("Creating a new session failed with response code: " + response.getStatusCode());
        }

        /* Try to convert DTO into a domain object */
        final SessionsDto responseEntity = response.getContent(SessionsDto.class);

        final @Nullable List<@Nullable SessionDto> sessions = responseEntity.sessions;
        if (sessions == null || sessions.size() == 0) {
            throw new HiveClientResponseException("List of sessions is unexpectedly empty.");
        }

        final @Nullable SessionDto sessionDto = sessions.get(0);
        if (sessionDto == null) {
            throw new HiveClientResponseException("Session object is unexpectedly null.");
        }

        final @Nullable SessionId sessionId = sessionDto.sessionId;
        final @Nullable UserId userId = sessionDto.userId;

        if (sessionId != null && userId != null) {
            logger.trace("Created new Hive API session with sessionId: {}", sessionDto.sessionId);

            return new Session(
                    sessionId,
                    userId
            );
        } else {
            throw new HiveClientResponseException("Session object is malformed.");
        }
    }

    @Override
    public void deleteSession(final Session session) {
        /* Send our delete session request to the Hive API. */
        final HiveApiResponse response = this.requestFactory.newRequest(getEndpointPathForSession(session.getSessionId()))
                .accept(MediaType.API_V6_5_0_JSON)
                .delete();

        if (response.getStatusCode() == 401) {
            throw new HiveApiNotAuthorisedException();
        } else if (response.getStatusCode() != 200) {
            throw new HiveApiUnknownException("Deleting session failed with response code: " + response.getStatusCode());
        }
    }

    @Override
    public boolean isValidSession(final @Nullable Session session) {
        if (session == null) {
            return false;
        }

        /* Try to get our own session from the Hive API. */
        final HiveApiResponse response = this.requestFactory.newRequest(getEndpointPathForSession(session.getSessionId()))
                .accept(MediaType.API_V6_5_0_JSON)
                .get();

        if (response.getStatusCode() == 200) {
            // If we succeeded the session is still valid.
            return true;
        } else if (response.getStatusCode() == 401 || response.getStatusCode() == 403) {
            // If we failed our session has expired (or was never valid).
            return false;
        }

        throw new HiveApiUnknownException("Checking session failed for an unknown reason");
    }
}
