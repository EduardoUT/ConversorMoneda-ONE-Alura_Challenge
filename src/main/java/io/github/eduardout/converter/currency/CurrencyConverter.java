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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author EduardoUT
 */
public class CurrencyConverter {

    public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.ONE;
    private static final BigDecimal MIN_AMOUNT = DEFAULT_AMOUNT;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("999999999.9999");
    private final RateProviderService rateProviderService;
    private Map<String, BigDecimal> mappedCurrencies;

    public CurrencyConverter(RateProviderService rateProviderService) {
        validateRateProvider(rateProviderService);
        this.rateProviderService = rateProviderService;
    }

    /**
     * Sets a map with the base and target key (currency code) and value
     * (equivalent amount) obtained from the rate provider.
     *
     * @param base   The base currency unit to search by its iso-code.
     * @param target The target currency unit to search by its iso-code.
     */
    private void setMappedCurrencies(CurrencyUnit base, CurrencyUnit target) {
        mappedCurrencies = new HashMap<>();
        Map<String, BigDecimal> currencyRates = rateProviderService.filterCurrencyRatesFromAvailableProvider();
        mappedCurrencies.putAll(currencyRates);
        filterSelectedCurrencies(mappedCurrencies, base, target);
    }

    private void filterSelectedCurrencies(Map<String, BigDecimal> currencies, CurrencyUnit base, CurrencyUnit target) {
        if (mappedCurrencies.isEmpty()) {
            throw new IllegalStateException("No currencies were found from provider.");
        }
        Map<String, ISO4217Currency> enumCurrencies = ISO4217Currency.getISO4217Currencies();
        currencies.entrySet()
                .stream()
                .filter(entrySet -> enumCurrencies.containsKey(entrySet.getKey()))
                .filter(entrySet -> entrySet.getKey().equalsIgnoreCase(base.getCurrencyCode())
                        || entrySet.getKey().equalsIgnoreCase(target.getCurrencyCode()))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Currencies " + base.getCurrencyCode()
                        + " or " + target.getCurrencyCode() + " not supported for this currencies provider."));
    }

    /**
     * Performs the next formula to calculate the given amount:
     * <p>
     * USD to EUR
     * <p>
     * EUR = (USD * (DEFAULT_AMOUNT / baseAmount) / (DEFAULT_AMOUNT /
     * targetAmount)
     * <p>
     * Being DEFAULT_AMOUNT a monetary representation of 1 unit.
     * <p>
     * Arguments can be swapped for example USD -> EUR or EUR -> USD, as the
     * provider gets actual and valid values.
     *
     * @param baseAmount   Amount equivalent of the base currency unit.
     * @param targetAmount Amount equivalent of the target currency unit.
     * @return The conversion between the base amount and target amount.
     */
    private BigDecimal calculateAmount(BigDecimal baseAmount,
                                       BigDecimal targetAmount, BigDecimal amount) {
        int scale = Math.max(baseAmount.scale(), targetAmount.scale());
        if ((baseAmount.equals(DEFAULT_AMOUNT) && targetAmount.intValue() == 0)
                || (baseAmount.intValue() == 0 && targetAmount.equals(DEFAULT_AMOUNT))) {
            baseAmount = DEFAULT_AMOUNT.divide(baseAmount, scale, RoundingMode.HALF_UP);
            targetAmount = DEFAULT_AMOUNT.divide(targetAmount, scale, RoundingMode.HALF_UP);
        }
        return amount.multiply(baseAmount)
                .divide(targetAmount, scale, RoundingMode.HALF_UP);
    }

    public BigDecimal convert(CurrencyUnit base, CurrencyUnit target, BigDecimal amount) {
        validateCurrencyUnits(base, target);
        validateAmount(amount);
        setMappedCurrencies(base, target);
        BigDecimal baseAmount = mappedCurrencies.get(base.getCurrencyCode());
        BigDecimal targetAmount = mappedCurrencies.get(target.getCurrencyCode());
        return calculateAmount(baseAmount, targetAmount, amount);
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

    private void validateCurrencyUnits(CurrencyUnit base, CurrencyUnit target) {
        if (base == null) {
            throw new IllegalArgumentException("Base CurrencyUnit is null.");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target CurrencyUnit is null.");
        }
        if (base.equals(target)) {
            throw new IllegalArgumentException("Invalid base CurrencyUnit: same "
                    + "type or same reference as target CurrencyUnit argument "
                    + "were provided.");
        }
    }

    private void validateRateProvider(RateProviderService rateProviderService) {
        if (rateProviderService == null) {
            throw new IllegalArgumentException("RateProviderService argument is null.");
        }
    }
}
