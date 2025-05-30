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
import io.github.eduardout.converter.util.ExchangeAPIParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.times;

import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author EduardoUT
 */
@ExtendWith(MockitoExtension.class)
class ExchangeAPITest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private PropertiesConfig mockPropertiesConfig;

    @Mock
    private ExchangeAPIParser mockRateParser;

    private ExchangeAPI exchangeAPI;

    @BeforeEach
    void setUpProperties() {
        exchangeAPI = new ExchangeAPI(
                mockHttpClient,
                mockPropertiesConfig,
                ".fcera",
                mockRateParser
        );
    }

    @Test
    void testSuccessfulApiCall() throws Exception {
        Map<String, BigDecimal> expectedRates = new HashMap<>();
        expectedRates.put("usd", new BigDecimal("0.05075398"));
        expectedRates.put("eur", new BigDecimal("0.044226646"));
        String testUrl = "https://api.currency-fake";
        String buildUrlWithParams = testUrl + "/v1/currencies/mxn.min.json";
        JSONObject mockResponse = new JSONObject("{}")
                .put("mxn", new JSONObject()
                        .put("usd", 0.05075398)
                        .put("eur", 0.044226646)
                );
        when(mockPropertiesConfig.getKeyProperties(exchangeAPI.getPropertyKeyPrefix()))
                .thenReturn(Collections.singletonList("api.endpoint"));
        when(mockPropertiesConfig.getPropertyValue(exchangeAPI.getPropertyKeyPrefix(), "api.endpoint"))
                .thenReturn(testUrl);
        when(mockHttpClient.fetchData(buildUrlWithParams))
                .thenReturn(mockResponse.toString());
        when(mockRateParser.parseRate(mockResponse.toString()))
                .thenReturn(expectedRates);
        Map<String, BigDecimal> result = exchangeAPI.getCurrencyRates();
        BigDecimal baseAmount = result.get("usd");
        BigDecimal targetAmount = result.get("eur");
        assertEquals(expectedRates.get("usd"), baseAmount);
        assertEquals(expectedRates.get("eur"), targetAmount);

        verify(mockPropertiesConfig).getKeyProperties(exchangeAPI.getPropertyKeyPrefix());
        verify(mockPropertiesConfig).getPropertyValue(exchangeAPI.getPropertyKeyPrefix(), "api.endpoint");
        verify(mockHttpClient).fetchData(buildUrlWithParams);
        verify(mockRateParser).parseRate(mockResponse.toString());
    }

    @Test
    void testWhenEndpointsFail() throws Exception {
        when(mockPropertiesConfig.getKeyProperties(exchangeAPI.getPropertyKeyPrefix()))
                .thenReturn(Arrays.asList("endpoint.one", "endpoint.two"));
        when(mockPropertiesConfig.getPropertyValue(exchangeAPI.getPropertyKeyPrefix(), "endpoint.one"))
                .thenReturn("https://api.example.com");
        when(mockPropertiesConfig.getPropertyValue(exchangeAPI.getPropertyKeyPrefix(), "endpoint.two"))
                .thenReturn("https://api2.example.com");
        when(mockHttpClient.fetchData(anyString()))
                .thenThrow(new IOException("Connection failed."));
        Map<String, BigDecimal> result = exchangeAPI.getCurrencyRates();
        assertEquals(Collections.emptyMap(), result);
        verify(mockHttpClient, times(2)).fetchData(anyString());
    }

    @Test
    void testRetryNextEndpointOnFailure() throws Exception {
        Map<String, BigDecimal> expected = new HashMap<>();
        expected.put("usd", new BigDecimal("0.05075398"));
        expected.put("jpy", new BigDecimal("0.15786539"));
        String badUrl = "https://bad.endpoint";
        String goodUrl = "https://good.endpoint";
        String paramsUrl = "/v1/currencies/mxn.min.json";
        String buildBadUrl = badUrl + paramsUrl;
        String buildGoodUrl = goodUrl + paramsUrl;
        JSONObject goodResponse = new JSONObject("{}")
                .put("mxn", new JSONObject()
                        .put("usd", 0.05075398)
                        .put("jpy", 7.15786539)
                );
        when(mockPropertiesConfig.getKeyProperties(exchangeAPI.getPropertyKeyPrefix()))
                .thenReturn(Arrays.asList("first", "second"));
        when(mockPropertiesConfig.getPropertyValue(exchangeAPI.getPropertyKeyPrefix(), "first"))
                .thenReturn(badUrl);
        when(mockPropertiesConfig.getPropertyValue(exchangeAPI.getPropertyKeyPrefix(), "second"))
                .thenReturn(goodUrl);
        when(mockHttpClient.fetchData(buildBadUrl))
                .thenThrow(new IOException("Connection failed"));
        when(mockHttpClient.fetchData(buildGoodUrl))
                .thenReturn(goodResponse.toString());
        when(mockRateParser.parseRate(goodResponse.toString()))
                .thenReturn(expected);
        Map<String, BigDecimal> result = exchangeAPI.getCurrencyRates();
        BigDecimal baseAmount = result.get("usd");
        BigDecimal targetAmount = result.get("jpy");
        assertEquals(expected.get("usd"), baseAmount);
        assertEquals(expected.get("jpy"), targetAmount);
        verify(mockHttpClient).fetchData(buildBadUrl);
        verify(mockHttpClient).fetchData(buildGoodUrl);
        verify(mockRateParser).parseRate(goodResponse.toString());
    }
}
