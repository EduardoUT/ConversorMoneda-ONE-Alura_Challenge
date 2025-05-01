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
import io.github.eduardout.converter.currency.provider.APIClient;
import io.github.eduardout.converter.currency.provider.FreeCurrencyExchangeRates;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import io.github.eduardout.converter.util.RateParser;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author EduardoUT
 */
public class FreeCurrencyExchangeRatesService {

    private FreeCurrencyExchangeRates freeCurrencyExchangeRates;

    public FreeCurrencyExchangeRatesService(APIClient apiClient,
            PropertiesConfig propertiesConfig,
            JSONCurrencyFileRepository jSONCurrencyFileRepository,
            RateParser rateParser) {
        freeCurrencyExchangeRates = new FreeCurrencyExchangeRates(apiClient,
                propertiesConfig, jSONCurrencyFileRepository, rateParser
        );
    }

    /**
     * Filters the available currencies from API and creates a new CurrencyUnit
     * instance if its compatible with ISO4217Currency enum.
     *
     * @return A sorted List of CurrencyUnit fetched from the
     * FreeCurrencyExhangeRates API.
     */
    public Optional<List<CurrencyUnit>> availableCurrencyCodes() {
        Map<String, ISO4217Currency> appCurrencies = ISO4217Currency.getISO4217Currencies();
        List<CurrencyUnit> apiCurrencies = freeCurrencyExchangeRates.getCurrencies()
                .get()
                .stream()
                .filter(apiCurrency -> appCurrencies.containsKey(apiCurrency))
                .map(apiCurrency -> {
                    ISO4217Currency iSO4217Currency = appCurrencies.get(apiCurrency);
                    return new CurrencyUnit(iSO4217Currency);
                })
                .sorted(Comparator.comparing(CurrencyUnit::getCurrencyCode))
                .collect(Collectors.toList());
        return Optional.ofNullable(apiCurrencies);
    }

    /**
     *
     * @return The FreeCurrencyExchange API RateProvider.
     */
    public FreeCurrencyExchangeRates getFreeCurrencyExchangeRates() {
        return freeCurrencyExchangeRates;
    }

}
