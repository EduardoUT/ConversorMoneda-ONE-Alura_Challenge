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

import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.util.RateParser;
import io.github.eduardout.converter.currency.CurrencyUnit;
import static io.github.eduardout.converter.GlobalLogger.*;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import org.json.JSONObject;

/**
 * This class is an implementation of a RateProvider interface, so we can fetch
 * the currency rates in real time from the Free Currency Exchange Rates API,
 * internet connection is needed to work correctly with request methods.
 *
 * Constats defined in this class must be the same as in the config.properties
 * file, currently ones are defined manually for example:
 *
 * key=value
 *
 * another.key=value
 *
 * The Free Currency Exchange Rates API always takes the base as 1.00 monetary
 * unit and returns the equivalence target amount of that monetary unit.
 *
 * @author EduardoUT
 */
public class FreeCurrencyExchangeRates implements RateProvider {

    private APIClient apiClient;
    private PropertiesConfig propertiesConfig;
    private JSONCurrencyFileRepository fallbackProvider;
    private RateParser rateParser;

    public FreeCurrencyExchangeRates(APIClient apiClient,
            PropertiesConfig propertiesConfig, JSONCurrencyFileRepository fallbackProvider,
            RateParser rateParser) throws IOException {
        this.apiClient = apiClient;
        this.propertiesConfig = propertiesConfig;
        this.fallbackProvider = fallbackProvider;
        this.rateParser = rateParser;
    }

    /**
     * Takes the currency rate amount obtained from the API response json and
     * returns it as a BigDecimal.
     *
     * @param base The CurrencyUnit base in this API represents a 1.00 monetary
     * amount.
     * @param target The CurrencyUnit to return its equivalence given the base.
     * @return The BigDecimal equivalence target amount of the given base
     * CurrencyUnit ISO 4217.
     */
    @Override
    public BigDecimal getCurrencyRate(CurrencyUnit base, CurrencyUnit target) {
        for (String key : propertiesConfig.getKeyProperties()) {
            try {
                String url = propertiesConfig.getPropertyValue(key);
                registerLog(Level.INFO, "Fetching data from API.");
                JSONObject response = apiClient.fetchDataAsJSONObject(url);
                fallbackProvider.updateCurrencyRates(response);
                return rateParser.parseRate(response, base, target);
            } catch (IOException | IllegalStateException e) {
                registerLogException(Level.SEVERE, "Error while getting response "
                        + "from API {0} ", e);
            }
        }
        registerLog(Level.SEVERE, "All response endpoinds faied, using JSON file.");
        return fallbackProvider.getCurrencyRate(base, target);
    }
}
