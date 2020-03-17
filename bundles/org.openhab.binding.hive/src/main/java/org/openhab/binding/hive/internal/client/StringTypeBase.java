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
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
abstract class StringTypeBase extends FromStringTypeBase<String> {
    public StringTypeBase(final String stringValue) {
        super(stringValue);
    }

    protected String transform(final String stringValue) {
        if (stringValue.isEmpty()) {
            throw new IllegalArgumentException("Value must not be empty");
        }

        return stringValue;
    }
}
