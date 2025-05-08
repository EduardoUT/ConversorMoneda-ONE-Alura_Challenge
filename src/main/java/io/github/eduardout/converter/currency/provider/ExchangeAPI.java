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
import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.util.RateParser;
import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.ISO4217Currency;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.json.JSONObject;

/**
 * <p>
 * This class is an implementation of a RateProvider interface, so we can fetch
 * the currency rates in real time from the Free Currency Exchange Rates API,
 * internet connection is needed to work correctly with request methods.
 * </p>
 * <p>
 * Constats defined in this class must be the same as in the config.properties
 * file, currently ones are defined manually for example:<br><br>
 * key=value<br>
 * another.key=value<br><br>
 * The Free Currency Exchange Rates API always takes the base as 1.00 monetary
 * unit and returns the equivalence target amount of that monetary unit, so for
 * this implementation was used the MXN currency as base to fetch data, these
 * are configured on the links on properties file
 * </p>
 *
 * @author EduardoUT
 */
public class ExchangeAPI implements RateProvider, RateProviderAvailableCurrencies {

    private APIClient apiClient;
    private PropertiesConfig propertiesConfig;
    private JSONCurrencyFileRepository fallbackProvider;
    private RateParser rateParser;

    public ExchangeAPI(APIClient apiClient,
                       PropertiesConfig propertiesConfig, JSONCurrencyFileRepository fallbackProvider,
                       RateParser rateParser) {
        this.apiClient = apiClient;
        this.propertiesConfig = propertiesConfig;
        this.fallbackProvider = fallbackProvider;
        this.rateParser = rateParser;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRates(CurrencyUnit base, CurrencyUnit target) {
        CurrencyUnit apiBaseCurrencyUnit = new CurrencyUnit(ISO4217Currency.MXN);
        for (String key : propertiesConfig.getKeyProperties()) {
            try {
                String url = propertiesConfig.getPropertyValue(key);
                registerLog(Level.INFO, "Fetching data from API.");
                JSONObject response = apiClient.fetchDataAsJSONObject(url);
                JSONObject apiBaseKey = response.getJSONObject(apiBaseCurrencyUnit.getCurrencyCode().toLowerCase());
                fallbackProvider.updateCurrencyRates(apiBaseKey);
                return rateParser.parseRate(apiBaseKey, base, target);
            } catch (IOException | IllegalStateException e) {
                registerLogException(Level.SEVERE, "Error: {0} ", e);
            }
        }
        registerLog(Level.SEVERE, "All response endpoints failed, using JSON file.");
        return fallbackProvider.getCurrencyRates(base, target);
    }

    @Override
    public List<String> getCurrencies() {
        List<String> apiCurrencies = Collections.emptyList();
        try {
            PropertiesConfig properties = PropertiesConfig.fromFile("config.properties", "available.");
            String url = properties.getPropertyValue("fcera.currencies");
            JSONObject response = apiClient.fetchDataAsJSONObject(url);
            apiCurrencies = response.toMap().keySet()
                    .stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            registerLogException(Level.SEVERE, "Error: {0} ", ex);
        }
        return apiCurrencies;
    }
}
