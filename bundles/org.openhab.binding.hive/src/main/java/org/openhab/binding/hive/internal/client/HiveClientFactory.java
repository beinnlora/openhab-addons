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
package org.openhab.binding.hive.internal.client;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.hive.internal.client.exception.HiveException;

/**
 * A factory for getting new instances of {@link HiveClient}.
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public interface HiveClientFactory {
    /**
     *
     * @param username
     *      The username to authenticate with.
     *
     * @param password
     *      The password to authenticate with.
     *
     * @return
     *      A new {@link HiveClient}.
     */
    HiveClient newClient(
            final String username,
            final String password
    ) throws HiveException;
}
