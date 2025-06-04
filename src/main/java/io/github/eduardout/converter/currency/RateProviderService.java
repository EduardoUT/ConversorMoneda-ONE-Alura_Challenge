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

import static io.github.eduardout.converter.GlobalLogger.*;

import io.github.eduardout.converter.currency.provider.RateProviderRegistry;
import io.github.eduardout.converter.currency.repository.JSONCurrencyRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author EduardoUT
 */
public class RateProviderService {

    private final RateProviderRegistry rateProviderRegistry;
    private final JSONCurrencyRepository jsonCurrencyRepository;

    public RateProviderService(RateProviderRegistry rateProviderRegistry, JSONCurrencyRepository jsonCurrencyRepository) {
        validateRateProviderRegistry(rateProviderRegistry);
        this.rateProviderRegistry = rateProviderRegistry;
        this.jsonCurrencyRepository = jsonCurrencyRepository;
    }

    public Map<String, BigDecimal> filterCurrencyRatesFromAvailableProvider() {
        Map<String, BigDecimal> currencyRates = rateProviderRegistry.filterCurrencyRatesFromAvailableProvider();
        if (currencyRates.isEmpty()) {
            return jsonCurrencyRepository.getCurrencyRates();
        }
        return currencyRates;
    }

    public void updateCurrencyRates() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, BigDecimal> currencyRates = filterCurrencyRatesFromAvailableProvider();
                if (!currencyRates.isEmpty()) {
                    try {
                        jsonCurrencyRepository.updateCurrencyRates(currencyRates);
                    } catch (IOException e) {
                        registerLogException(Level.SEVERE, "An error occurs on the repository while updating"
                                + " the currency rates: {0} ", e);
                        Thread.currentThread().interrupt();
                    }
                } else {
                    Thread.currentThread().interrupt();
                }
            }
        }, 0, TimeUnit.MINUTES.toMillis(5));
    }

    /**
     * Filters the available currencies from API and creates a new CurrencyUnit
     * instance if its compatible with ISO4217Currency enum.
     *
     * @return A sorted List of CurrencyUnit fetched from the
     * FreeCurrencyExhangeRates API.
     */
    public List<CurrencyUnit> availableCurrencyUnits() {
        List<CurrencyUnit> currencyUnits = Collections.emptyList();
        Map<String, ISO4217Currency> appCurrencies = ISO4217Currency.getISO4217Currencies();
        Map<String, BigDecimal> availableCurrencies = filterCurrencyRatesFromAvailableProvider();
        if (!availableCurrencies.isEmpty()) {
            currencyUnits = availableCurrencies
                    .keySet()
                    .stream()
                    .filter(appCurrencies::containsKey)
                    .map(currency -> {
                        ISO4217Currency iSO4217Currency = appCurrencies.get(currency);
                        return new CurrencyUnit(iSO4217Currency);
                    })
                    .sorted(Comparator.comparing(CurrencyUnit::getCurrencyCode))
                    .collect(Collectors.toList());
        }
        return currencyUnits;
    }

    private void validateRateProviderRegistry(RateProviderRegistry rateProviderRegistry) {
        if (rateProviderRegistry == null) {
            throw new IllegalArgumentException("RateProviderRegistry argument is null.");
        }
    }
}
