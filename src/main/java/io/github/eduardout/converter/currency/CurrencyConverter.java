/*
 * Copyright (C) 2025 EduardoUT
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.eduardout.converter.currency;

import io.github.eduardout.converter.currency.provider.RateProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EduardoUT
 */
public class CurrencyConverter {

    private static final BigDecimal MIN_AMOUNT = BigDecimal.ONE;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("999999999.9999");
    public static final BigDecimal DEFAULT_AMOUNT = new BigDecimal("1.00");
    private CurrencyUnit base;
    private CurrencyUnit target;
    private RateProvider rateProvider;
    private BigDecimal amount;
    private Map<String, BigDecimal> mappedCurrencies;

    public CurrencyConverter(CurrencyUnit base, CurrencyUnit target,
                             RateProvider rateProvider, BigDecimal amount) {
        validateBaseCurrencyUnit(base, target);
        validateTargetCurrencyUnit(target, base);
        validateRateProvider(rateProvider);
        validateAmount(amount);
        this.base = base;
        this.target = target;
        this.rateProvider = rateProvider;
        this.amount = amount;
        setMappedCurrencies(base, target);
    }

    /**
     * Sets a map with the base and target key (currency code) and value (equivalent amount) obtained from the rate provider.
     *
     * @param base   The base currency unit to search by its iso-code.
     * @param target The target currency unit to search by its iso-code.
     */
    private void setMappedCurrencies(CurrencyUnit base, CurrencyUnit target) {
        mappedCurrencies = new HashMap<>();
        mappedCurrencies.putAll(rateProvider.getCurrencyRates(base, target).orElseGet(Collections::emptyMap));
        if (mappedCurrencies.isEmpty()) {
            throw new IllegalStateException("No currencies were found from provider.");
        }
    }

    /**
     * @return The equivalent amount from the base currency.
     */
    public BigDecimal getBaseAmount() {
        return mappedCurrencies.get(base.getCurrencyCode());
    }

    /**
     * @return The equivalent amount from the target currency.
     */
    public BigDecimal getTargetAmount() {
        return mappedCurrencies.get(target.getCurrencyCode());
    }

    /**
     * Performs the next formula to calculate the given amount:
     * <p>
     * USD to EUR
     * <p>
     * EUR = (USD * (DEFAULT_AMOUNT / baseAmount) / (DEFAULT_AMOUNT / targetAmount)
     * <p>
     * Being DEFAULT_AMOUNT a monetary representation of 1 unit.
     * <p>
     * Arguments can be swapped for example USD -> EUR or EUR -> USD, as the provider
     * gets actual and valid values.
     *
     * @param baseAmount   Amount equivalent of the base currency unit.
     * @param targetAmount Amount equivalent of the target currency unit.
     * @return The conversion between the base amount and target amount.
     */
    private BigDecimal calculateAmount(BigDecimal baseAmount, BigDecimal targetAmount) {
        return amount
                .multiply(DEFAULT_AMOUNT.divide(baseAmount, baseAmount.scale(), RoundingMode.FLOOR))
                .divide(DEFAULT_AMOUNT.divide(targetAmount, targetAmount.scale(), RoundingMode.FLOOR),
                        4, RoundingMode.FLOOR
                );
    }

    public BigDecimal getConversion() {
        return calculateAmount(getBaseAmount(), getTargetAmount());
    }

    public BigDecimal reverseConversion() {
        return calculateAmount(getTargetAmount(), getBaseAmount());
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is null.");
        }
        int comparationResult = amount.compareTo(MIN_AMOUNT);
        if (comparationResult < 0) {
            throw new IllegalArgumentException("Minimum valid amount of 1, 1.0 "
                    + "or greater is required.");
        }
        comparationResult = amount.compareTo(MAX_AMOUNT);
        if (comparationResult > 0) {
            throw new IllegalArgumentException("Maximum valid amount of "
                    + "999999999.9999 was exceded.");
        }
    }

    private void validateBaseCurrencyUnit(CurrencyUnit base, CurrencyUnit target) {
        if (base == null) {
            throw new IllegalArgumentException("Base CurrencyUnit is null.");
        }
        if (base.equals(target)) {
            throw new IllegalArgumentException("Invalid base CurrencyUnit: same "
                    + "type or same reference as target CurrencyUnit argument "
                    + "were provided.");
        }
    }

    private void validateTargetCurrencyUnit(CurrencyUnit target, CurrencyUnit base) {
        if (target == null) {
            throw new IllegalArgumentException("Target CurrencyUnit is null.");
        }
        if (target.equals(base)) {
            throw new IllegalArgumentException("Invalid target CurrencyUnit: same "
                    + "type or same reference as base CurrencyUnit argument "
                    + "were provided.");
        }
    }

    private void validateRateProvider(RateProvider rateProvider) {
        if (rateProvider == null) {
            throw new IllegalArgumentException("RateProvider argument is null.");
        }
    }
}
