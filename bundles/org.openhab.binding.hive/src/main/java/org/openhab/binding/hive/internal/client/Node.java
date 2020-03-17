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
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hive.internal.client.feature.Feature;

import java.util.Set;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public interface Node {
    NodeId getId();
    NodeName getName();
    NodeType getNodeType();
    ProductType getProductType();
    Protocol getProtocol();
    NodeId getParentNodeId();

    Set<Feature> getFeatures();
    <T extends Feature> @Nullable T getFeature(Class<T> classOfFeature);
}
