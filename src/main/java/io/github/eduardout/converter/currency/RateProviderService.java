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

import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.currency.provider.HttpClient;
import io.github.eduardout.converter.currency.provider.ExchangeAPI;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import io.github.eduardout.converter.util.ExchangeAPIParser;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author EduardoUT
 */
public class ExchangeAPIService {

    private ExchangeAPI exchangeAPI;

    public ExchangeAPIService(HttpClient httpClient,
                              PropertiesConfig propertiesConfig,
                              JSONCurrencyFileRepository jSONCurrencyFileRepository,
                              ExchangeAPIParser exchangeAPIParser) {
        exchangeAPI = new ExchangeAPI(
                httpClient,
                propertiesConfig,
                jSONCurrencyFileRepository,
                exchangeAPIParser
        );
    }

    /**
     * Filters the available currencies from API and creates a new CurrencyUnit
     * instance if its compatible with ISO4217Currency enum.
     *
     * @return A sorted List of CurrencyUnit fetched from the
     * FreeCurrencyExhangeRates API.
     */
    public List<CurrencyUnit> availableCurrencyCodes() {
        Map<String, ISO4217Currency> appCurrencies = ISO4217Currency.getISO4217Currencies();
        return exchangeAPI
                .getCurrencies()
                .stream()
                .filter(appCurrencies::containsKey)
                .map(apiCurrency -> {
                    ISO4217Currency iSO4217Currency = appCurrencies.get(apiCurrency);
                    return new CurrencyUnit(iSO4217Currency);
                })
                .sorted(Comparator.comparing(CurrencyUnit::getCurrencyCode))
                .collect(Collectors.toList());
    }

    /**
     * @return The FreeCurrencyExchange API RateProvider.
     */
    public ExchangeAPI getExchangeAPI() {
        return exchangeAPI;
    }

}
