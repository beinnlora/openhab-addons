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

/**
 * A facade for JSON serialisation/deserialisation.
 *
 * <p>
 *     Used to decouple from Gson and make testing easier.
 *     ({@code Gson} is final and so annoying to replace with a test double)
 * </p>
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public interface JsonService {
    String toJson(Object object);
    <T> T fromJson(String json, Class<T> classOfT);
}
