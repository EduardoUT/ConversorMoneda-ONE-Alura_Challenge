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

import java.math.BigDecimal;
import java.util.Map;

/**
 * Interface where online or offline implementations get the currency rates.
 *
 * @author EduardoUT
 */
public interface RateProvider {

    /**
     * In this method we set the base CurrencyUnit to get its corresponding monetary
     * amount.
     *
     * @return An Optional Map with the corresponding keys (currency code) and values (amount).
     */
    Map<String, BigDecimal> getCurrencyRates();
}
