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
package io.github.eduardout.converter.util;

import io.github.eduardout.converter.currency.CurrencyUnit;

import java.math.BigDecimal;
import java.util.Map;

import org.json.JSONObject;

/**
 * Since API currency rate providers have different ways to show the concrete
 * currency rate amount, you can create your own to parse the response
 * depending on the JSONObject response.
 *
 * @author EduardoUT
 */
public interface RateParser {

    /**
     * @param response The response from the API Provider.
     * @return A Map with the currency codes as keys and its amount as values.
     */
    Map<String, BigDecimal> parseRate(String response);
}
