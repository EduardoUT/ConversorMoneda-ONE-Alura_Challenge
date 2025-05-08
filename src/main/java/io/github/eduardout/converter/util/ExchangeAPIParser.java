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
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONObject;

/**
 * This is an implementation to return the BigDecimal currency rate according to
 * the body response of a JSONObject the Free Currency Exchange Rates API.
 *
 * @author EduardoUT
 */
public class FreeCurrencyExchangeRatesParser implements RateParser {

    @Override
    public Map<String, BigDecimal> parseRate(Object response, CurrencyUnit base, CurrencyUnit target) {
        JSONObject jsonObject = new JSONObject(response);
        String baseCode = base.getCurrencyCode();
        String targetCode = target.getCurrencyCode();
        return jsonObject.toMap().entrySet()
                .stream()
                .filter(entrySet ->
                        entrySet.getKey().equalsIgnoreCase(baseCode)
                                || entrySet.getKey().equalsIgnoreCase(targetCode))
                .collect(Collectors.toMap(
                                entryKey -> entryKey.getKey().toUpperCase(),
                                entryValue -> new BigDecimal(entryValue.getValue().toString())
                        )
                );
    }
}
