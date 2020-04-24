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

import java.time.Instant;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 *
 *
 * @author Ross Brown - Initial contribution
 */
@NonNullByDefault
public final class DefaultFeatureAttribute<T> implements SettableFeatureAttribute<T> {
    private final @Nullable T targetValue;
    private final T displayValue;
    private final T reportedValue;
    private final Instant reportReceivedTime;
    private final Instant reportChangedTime;

    private DefaultFeatureAttribute(
            final @Nullable T targetValue,
            final T displayValue,
            final T reportedValue,
            final Instant reportChangedTime,
            final Instant reportReceivedTime
    ) {
        this.targetValue = targetValue;
        this.displayValue = Objects.requireNonNull(displayValue);
        this.reportedValue = Objects.requireNonNull(reportedValue);
        this.reportChangedTime = Objects.requireNonNull(reportChangedTime);
        this.reportReceivedTime = Objects.requireNonNull(reportReceivedTime);
    }

    @Override
    public @Nullable T getTargetValue() {
        return this.targetValue;
    }

    @Override
    public T getDisplayValue() {
        return this.displayValue;
    }

    @Override
    public T getReportedValue() {
        return this.reportedValue;
    }

    @Override
    public Instant getReportReceivedTime() {
        return this.reportReceivedTime;
    }

    @Override
    public Instant getReportChangedTime() {
        return this.reportChangedTime;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private @Nullable T targetValue = null;
        private @Nullable T displayValue;
        private @Nullable T reportedValue;
        private @Nullable Instant reportReceivedTime;
        private @Nullable Instant reportChangedTime;

        public Builder<T> from(final SettableFeatureAttribute<T> featureAttribute) {
            Objects.requireNonNull(featureAttribute);

            return this.targetValue(featureAttribute.getTargetValue())
                    .displayValue(featureAttribute.getDisplayValue())
                    .reportedValue(featureAttribute.getReportedValue())
                    .reportReceivedTime(featureAttribute.getReportReceivedTime())
                    .reportChangedTime(featureAttribute.getReportChangedTime());
        }

        public Builder<T> from(final FeatureAttribute<T> featureAttribute) {
            Objects.requireNonNull(featureAttribute);

            return this.displayValue(featureAttribute.getDisplayValue())
                    .reportedValue(featureAttribute.getReportedValue())
                    .reportReceivedTime(featureAttribute.getReportReceivedTime())
                    .reportChangedTime(featureAttribute.getReportChangedTime());
        }

        public Builder<T> targetValue(final @Nullable T targetValue) {
            this.targetValue = targetValue;

            return this;
        }

        public Builder<T> displayValue(final T displayValue) {
            this.displayValue = Objects.requireNonNull(displayValue);

            return this;
        }

        public Builder<T> reportedValue(final T reportedValue) {
            this.reportedValue = Objects.requireNonNull(reportedValue);

            return this;
        }

        public Builder<T> reportReceivedTime(final Instant reportReceivedTime) {
            this.reportReceivedTime = Objects.requireNonNull(reportReceivedTime);

            return this;
        }

        public Builder<T> reportChangedTime(final Instant reportChangedTime) {
            this.reportChangedTime = Objects.requireNonNull(reportChangedTime);

            return this;
        }

        public DefaultFeatureAttribute<T> build() {
            final @Nullable T displayValue = this.displayValue;
            final @Nullable T reportedValue = this.reportedValue;
            final @Nullable Instant reportReceivedTime = this.reportReceivedTime;
            final @Nullable Instant reportChangedTime = this.reportChangedTime;

            if (displayValue == null
                    || reportedValue == null
                    || reportReceivedTime == null
                    || reportChangedTime == null
            ) {
                throw new IllegalStateException(BuilderUtil.REQUIRED_ATTRIBUTE_NOT_SET_MESSAGE);
            }

            return new DefaultFeatureAttribute<>(
                    this.targetValue,
                    displayValue,
                    reportedValue,
                    reportChangedTime,
                    reportReceivedTime
            );
        }
    }
}
