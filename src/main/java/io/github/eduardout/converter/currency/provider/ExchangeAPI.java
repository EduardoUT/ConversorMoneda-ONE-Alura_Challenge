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
import io.github.eduardout.converter.util.ExchangeAPIParser;
import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.ISO4217Currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

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
public class ExchangeAPI extends AbstractCurrencyProvider {

    public static final String ENDPOINT_VERSION = "/v1";
    public static final String ENDPOINT_CURRENCIES = "/currencies";
    public static final CurrencyUnit DEFAULT_CURRENCY_UNIT = new CurrencyUnit(ISO4217Currency.MXN);
    public static final String JSON_EXTENSION = ".min.json";
    private CurrencyUnit baseCurrencyParameter;
    private String endPointVersion;
    private ExchangeAPIParser exchangeAPIParser;

    public ExchangeAPI(HttpClient httpClient,
                       PropertiesConfig propertiesConfig,
                       String propertyKeyPrefix,
                       ExchangeAPIParser exchangeAPIParser) {
        super(httpClient, propertiesConfig, propertyKeyPrefix);
        this.exchangeAPIParser = exchangeAPIParser;
        this.endPointVersion = ENDPOINT_VERSION;
        this.baseCurrencyParameter = DEFAULT_CURRENCY_UNIT;
    }

    /**
     * <p>Must have to start with a / sign, version can be changed too as it doesn't work,
     * check the logger console or file it will provide when it fails or is not correct.
     * </p>
     *
     * @param endPointVersion Sets to a new version.
     */
    public void setEndPointVersion(String endPointVersion) {
        if (this.endPointVersion == null || this.endPointVersion.isEmpty()) {
            throw new IllegalArgumentException("Endpoint version is null or empty.");
        }
        if (!this.endPointVersion.startsWith("/")) {
            throw new IllegalArgumentException("Endpoint version must start with /");
        }
        this.endPointVersion = endPointVersion;
    }

    public CurrencyUnit getBaseCurrencyParameter() {
        return baseCurrencyParameter;
    }

    /**
     * @param baseCurrencyParameter Sets the base currency unit to fetch the currency
     *                              rates if the default (MXN) is not the desired.
     */
    public void setBaseCurrencyParameter(CurrencyUnit baseCurrencyParameter) {
        this.baseCurrencyParameter = baseCurrencyParameter;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRates() {
        for (String key : super.getPropertiesConfig().getKeyProperties(super.getPropertyKeyPrefix())) {
            try {
                String url = super.getPropertiesConfig().getPropertyValue(super.getPropertyKeyPrefix(), key);
                logHealthCheckIfNeeded(getClass(), "Fetching data from Free Exchange Rates API");
                String response = super.getHttpClient().fetchData(buildUrlWithParams(url));
                exchangeAPIParser.setBaseCurrencyUnitKey(baseCurrencyParameter);
                return exchangeAPIParser.parseRate(response);
            } catch (IOException | IllegalStateException e) {
                registerLogException(Level.SEVERE, "Error: {0} ", e);
            }
        }
        registerLog(Level.SEVERE, "All response endpoints for Free Exchange Rates API failed.");
        return Collections.emptyMap();
    }

    /**
     * <p>This concatenates in order the parameters needed by the url endpoints to build
     * the next result:</p>
     * <p>/v1/currencies/mxn.min.json</p>
     * <p>Where the params are:</p>
     * <p>Version: /v1</p>
     * <p>Currencies: /currencies</p>
     * <p>Base currency code: mxn</p>
     * <p>JSON Extension: .min.json</p>
     * <p>
     * Note: As all the url endpoints have different domains, the params
     * will only change and can be set in execution time.
     * </p>
     *
     * <p>
     * The url with base currency code parameter will provide the currency rates,
     * while the url without this parameter will provide all the available currencies
     * of this API provider.
     * </p>
     *
     * @param url The url to concatenate all params.
     * @return The new url with all the parameters.
     */
    private String buildUrlWithParams(String url) {
        String emptyUrl = "";
        if (url == null) {
            return emptyUrl;
        }
        if (!url.isEmpty()) {
            return url.concat(endPointVersion)
                    .concat(ENDPOINT_CURRENCIES)
                    .concat("/")
                    .concat(baseCurrencyParameter.getCurrencyCode().toLowerCase())
                    .concat(JSON_EXTENSION);
        }
        return emptyUrl;
    }
}
