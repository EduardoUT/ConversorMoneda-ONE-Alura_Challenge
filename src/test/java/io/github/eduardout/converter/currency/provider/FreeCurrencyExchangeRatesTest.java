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
 *
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
    void testSuccessfilApiCall() throws Exception {
        String testUrl = "https://api.example.com";
        JSONObject mockResponse = new JSONObject().put("USD", new JSONObject().put("EUR", 0.85));
        when(mockPropertiesConfig.getKeyProperties())
                .thenReturn(new String[]{"api.endpoint"});
        when(mockPropertiesConfig.getPropertyValue("api.endpoint"))
                .thenReturn(testUrl);
        when(mockApiClient.fetchDataAsJSONObject(testUrl))
                .thenReturn(mockResponse);
        when(mockRateParser.parseRate(mockResponse, mockBase, mockTarget))
                .thenReturn(new BigDecimal("0.85"));
        BigDecimal result = rates.getCurrencyRate(mockBase, mockTarget);
        assertEquals(new BigDecimal("0.85"), result);
        verify(mockFallbackProvider).updateCurrencyRates(mockResponse);
    }

    @Test
    public void testFallbackWhenAllEnpointsFail() throws Exception {
        when(mockPropertiesConfig.getKeyProperties())
                .thenReturn(new String[]{"endpoint.one", "endpoint.two"});
        when(mockPropertiesConfig.getPropertyValue(anyString()))
                .thenReturn("https://api.example.com");
        when(mockApiClient.fetchDataAsJSONObject(anyString()))
                .thenThrow(new IOException("Connection failed."));
        when(mockFallbackProvider.getCurrencyRate(mockBase, mockTarget))
                .thenReturn(new BigDecimal("1.25"));
        BigDecimal result = rates.getCurrencyRate(mockBase, mockTarget);
        assertEquals(new BigDecimal("1.25"), result);
        verify(mockFallbackProvider).getCurrencyRate(mockBase, mockTarget);
    }

    @Test
    void testRetryNextEndpointOnFailure() throws Exception {
        String badUrl = "https://bad.endpoint";
        String goodUrl = "https://good.endpoint";
        JSONObject goodResponse = new JSONObject().put("usd", new JSONObject().put("jpy", 140.0));
        when(mockPropertiesConfig.getKeyProperties())
                .thenReturn(new String[]{"first", "second"});
        when(mockPropertiesConfig.getPropertyValue("first"))
                .thenReturn(badUrl);
        when(mockPropertiesConfig.getPropertyValue("second"))
                .thenReturn(goodUrl);
        when(mockApiClient.fetchDataAsJSONObject(badUrl))
                .thenThrow(new IOException("Connection failded"));
        when(mockApiClient.fetchDataAsJSONObject(goodUrl))
                .thenReturn(goodResponse);
        when(mockRateParser.parseRate(goodResponse, mockBase, mockTarget))
                .thenReturn(new BigDecimal("140.0"));
        BigDecimal result = rates.getCurrencyRate(mockBase, mockTarget);
        assertEquals(new BigDecimal("140.0"), result);
        verify(mockApiClient, times(2)).fetchDataAsJSONObject(anyString());
    }
}
