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

import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import io.github.eduardout.converter.util.RateParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyString;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.times;

import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author EduardoUT
 */
@ExtendWith(MockitoExtension.class)
class FreeCurrencyExchangeRatesTest {

    @Mock
    private APIClient mockApiClient;

    @Mock
    private PropertiesConfig mockPropertiesConfig;

    @Mock
    private JSONCurrencyFileRepository mockFallbackProvider;

    @Mock
    private RateParser mockRateParser;

    @Mock
    private CurrencyUnit mockBase;

    @Mock
    private CurrencyUnit mockTarget;

    @InjectMocks
    private FreeCurrencyExchangeRates rates;

    @Test
    void testSuccessfulApiCall() throws Exception {
        Map<String, BigDecimal> expectedRates = new HashMap<>();
        expectedRates.put("usd", new BigDecimal("0.05075398"));
        expectedRates.put("eur", new BigDecimal("0.044226646"));
        String testUrl = "https://api.currency-fake.mxn.min.json";
        JSONObject mockResponse = new JSONObject("{}")
                .put("mxn", new JSONObject()
                        .put("usd", 0.05075398)
                        .put("eur", 0.044226646)
                );
        when(mockPropertiesConfig.getKeyProperties())
                .thenReturn(Collections.singletonList("api.endpoint"));
        when(mockPropertiesConfig.getPropertyValue("api.endpoint"))
                .thenReturn(testUrl);
        when(mockApiClient.fetchDataAsJSONObject(testUrl))
                .thenReturn(mockResponse);
        when(mockRateParser.parseRate(mockResponse.getJSONObject("mxn"), mockBase, mockTarget))
                .thenReturn(expectedRates);
        Optional<Map<String, BigDecimal>> result = rates.getCurrencyRates(mockBase, mockTarget);
        BigDecimal baseAmount = result.orElseThrow(NoSuchElementException::new).get("usd");
        BigDecimal targetAmount = result.orElseThrow(NoSuchElementException::new).get("eur");
        assertEquals(expectedRates.get("usd"), baseAmount);
        assertEquals(expectedRates.get("eur"), targetAmount);
        verify(mockFallbackProvider).updateCurrencyRates(mockResponse.getJSONObject("mxn"));
    }

    @Test
    void testFallbackWhenAllEnpointsFail() throws Exception {
        Map<String, BigDecimal> expected = new HashMap<>();
        expected.put("MXN", new BigDecimal("1.25"));
        when(mockPropertiesConfig.getKeyProperties())
                .thenReturn(Arrays.asList("endpoint.one", "endpoint.two"));
        when(mockPropertiesConfig.getPropertyValue(anyString()))
                .thenReturn("https://api.example.com");
        when(mockApiClient.fetchDataAsJSONObject(anyString()))
                .thenThrow(new IOException("Connection failed."));
        when(mockFallbackProvider.getCurrencyRates(mockBase, mockTarget))
                .thenReturn(Optional.of(expected));
        Optional<Map<String, BigDecimal>> result = rates.getCurrencyRates(mockBase, mockTarget);
        assertEquals(new BigDecimal("1.25"), result.orElseThrow(NoSuchElementException::new).get("MXN"));
        verify(mockFallbackProvider).getCurrencyRates(mockBase, mockTarget);
    }

    @Test
    void testRetryNextEndpointOnFailure() throws Exception {
        Map<String, BigDecimal> expected = new HashMap<>();
        expected.put("usd", new BigDecimal("0.05075398"));
        expected.put("jpy", new BigDecimal("7.15786539"));
        String badUrl = "https://bad.endpoint";
        String goodUrl = "https://good.endpoint";
        JSONObject goodResponse = new JSONObject("{}")
                .put("mxn", new JSONObject()
                        .put("usd", 0.05075398)
                        .put("jpy", 7.15786539)
                );
        when(mockPropertiesConfig.getKeyProperties())
                .thenReturn(Arrays.asList("first", "second"));
        when(mockPropertiesConfig.getPropertyValue("first"))
                .thenReturn(badUrl);
        when(mockPropertiesConfig.getPropertyValue("second"))
                .thenReturn(goodUrl);
        when(mockApiClient.fetchDataAsJSONObject(badUrl))
                .thenThrow(new IOException("Connection failed"));
        when(mockApiClient.fetchDataAsJSONObject(goodUrl))
                .thenReturn(goodResponse);
        when(mockRateParser.parseRate(goodResponse.getJSONObject("mxn"), mockBase, mockTarget))
                .thenReturn(expected);
        Optional<Map<String, BigDecimal>> result = rates.getCurrencyRates(mockBase, mockTarget);
        BigDecimal baseAmount = result.orElseThrow(NoSuchElementException::new).get("usd");
        BigDecimal targetAmount = result.orElseThrow(NoSuchElementException::new).get("jpy");
        assertEquals(expected.get("usd"), baseAmount);
        assertEquals(expected.get("jpy"), targetAmount);
        verify(mockApiClient, times(2)).fetchDataAsJSONObject(anyString());
    }
}
