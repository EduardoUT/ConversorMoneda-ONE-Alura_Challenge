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
package io.github.eduardout.converter.currency.provider;

import static io.github.eduardout.converter.GlobalLogger.*;

import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.util.NBPExchangeRatesParser;
import io.github.eduardout.converter.util.RateParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.json.JSONArray;

/**
 * @author EduardoUT
 */
public class NBPExchangeRates implements RateProvider, RateProviderAvailableCurrencies {

    private APIClient apiClient;
    private PropertiesConfig propertiesConfig;
    private RateParser rateParser;

    public NBPExchangeRates(APIClient aPIClient,
                            PropertiesConfig propertiesConfig,
                            RateParser rateParser) {
        this.apiClient = aPIClient;
        this.propertiesConfig = propertiesConfig;
        this.rateParser = rateParser;
    }

    @Override
    public Optional<Map<String, BigDecimal>> getCurrencyRates(CurrencyUnit base, CurrencyUnit target) {
        Map<String, BigDecimal> rates = null;
        try {
            String url = propertiesConfig.getPropertyValue("exchangerates");
            registerLog(Level.INFO, "Fetching data from API.");
            JSONArray response = apiClient.fetchDataAsJSONArray(url);
            rates = rateParser.parseRate(response, base, target);
        } catch (IOException | IllegalStateException e) {
            registerLogException(Level.SEVERE, "Error: {0}", e);
        }
        return Optional.ofNullable(rates);
    }

    @Override
    public Optional<List<String>> getCurrencies() {
        List<String> currencies = null;
        try {
            String currencyKey = "currency";
            String url = propertiesConfig.getPropertyValue("exchangerates");
            JSONArray response = apiClient.fetchDataAsJSONArray(url);
            JSONArray rates = response.getJSONObject(0).getJSONArray("rates");
            currencies = rates.toList()
                    .stream()
                    .filter(HashMap.class::isInstance)
                    .map(HashMap.class::cast)
                    .filter(hashMap -> hashMap.remove(currencyKey, hashMap.get(currencyKey)))
                    .filter(hashMap -> hashMap.remove("mid", hashMap.get("mid")))
                    .map(hashMap -> hashMap.get("code").toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            registerLogException(Level.SEVERE, "Error: {0}", e);
        }
        return Optional.ofNullable(currencies);
    }
}
