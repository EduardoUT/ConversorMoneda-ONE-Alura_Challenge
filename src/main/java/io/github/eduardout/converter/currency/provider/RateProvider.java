/*
 * Copyright (C) 2025 Eduardo UT
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
package io.github.eduardout.converter.currency.provider;

import io.github.eduardout.converter.currency.CurrencyUnit;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Interface where online or offline implementations get the CurrencyRate.
 *
 * @author EduardoUT
 */
public interface RateProvider {

    /**
     * In this method we set the base CurrencyUnit to get the target monetary
     * amount.
     *
     * @param base CurrencyUnit as the base curreny.
     * @param target CurrencyUnit as the target currency to get the equivalence
     * amount.
     * @return An Optional BigDecimal with the equivalence target amount of the
     * base.
     */
    Optional<BigDecimal> getCurrencyRate(CurrencyUnit base, CurrencyUnit target);
}
