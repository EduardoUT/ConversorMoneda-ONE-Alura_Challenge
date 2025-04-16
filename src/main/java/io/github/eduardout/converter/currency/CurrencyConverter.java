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

/**
 *
 * @author EduardoUT
 */
public class CurrencyConverter {

    private CurrencyUnit base;
    private CurrencyUnit target;
    private RateProvider rateProvider;
    private BigDecimal amount;
    private static final BigDecimal MIN_AMOUNT = BigDecimal.ONE;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("999999999.9999");
    private static final BigDecimal ORIGIN_AMOUNT = new BigDecimal("1000");

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
    }

    public BigDecimal getConvertion() {
        return amount.multiply(getCurrencyRateFromProvider(base, target))
                .setScale(2, RoundingMode.FLOOR);
    }

    public BigDecimal reverseConvertion() {
        BigDecimal originEquivalentAmount = ORIGIN_AMOUNT.divide(
                getCurrencyRateFromProvider(base, target), 3, RoundingMode.FLOOR
        ).movePointLeft(3);
        return amount.multiply(originEquivalentAmount).setScale(
                3, RoundingMode.FLOOR
        );
    }

    private BigDecimal getCurrencyRateFromProvider(CurrencyUnit base, CurrencyUnit target) {
        return rateProvider.getCurrencyRate(base, target);
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
