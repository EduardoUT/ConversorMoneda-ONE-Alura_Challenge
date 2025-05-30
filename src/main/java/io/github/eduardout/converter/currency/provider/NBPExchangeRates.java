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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

/**
 * @author EduardoUT
 */
public class NBPExchangeRates extends AbstractCurrencyProvider {

    private RateParser rateParser;

    public NBPExchangeRates(HttpClient httpClient,
                            PropertiesConfig propertiesConfig,
                            String propertyKeyPrefix,
                            RateParser rateParser) {
        super(httpClient, propertiesConfig, propertyKeyPrefix);
        this.rateParser = rateParser;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRates() {
        Map<String, BigDecimal> currencyRates = Collections.emptyMap();
        try {
            String url = super.getPropertiesConfig().getPropertyValue(super.getPropertyKeyPrefix(), "exchangerates");
            registerLog(Level.INFO, "Fetching data from National Bank Of Poland API");
            String response = super.getHttpClient().fetchData(url);
            currencyRates = rateParser.parseRate(response);
        } catch (IOException | IllegalStateException e) {
            registerLogException(Level.SEVERE, "Error: {0}", e);
            registerLog(Level.SEVERE, "Endpoint response for National Bank Of Poland failed.");
        }
        return currencyRates;
    }
}
