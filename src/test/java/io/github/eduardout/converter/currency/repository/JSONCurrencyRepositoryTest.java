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
package io.github.eduardout.converter.currency.repository;

import io.github.eduardout.converter.currency.CurrencyUnit;

import static io.github.eduardout.converter.currency.ISO4217Currency.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

import io.github.eduardout.converter.util.ExchangeAPIParser;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author EduardoUT
 */
class JSONCurrencyFileRepositoryTest {

    @TempDir
    static Path tempDir;

    private JSONCurrencyFileRepository jSONCurrencyFileRepository;
    private ExchangeAPIParser exchangeAPIParser;
    private Path testFilePath;
    private CurrencyUnit baseCurrency;
    private CurrencyUnit targetCurrency;

    @SuppressWarnings("unused")
    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("test-rates.json");
        baseCurrency = new CurrencyUnit(MXN);
        targetCurrency = new CurrencyUnit(GBP);
        exchangeAPIParser = new ExchangeAPIParser();
    }

    @Test
    void testReadWriteOperations() throws IOException {
        jSONCurrencyFileRepository = new JSONCurrencyFileRepository(testFilePath.toString(), exchangeAPIParser);
        JSONObject testData = new JSONObject("{}");
        testData.put("gbp", 0.037968493);
        testData.put("mxn", 1);
        jSONCurrencyFileRepository.updateCurrencyRates(testData);
        Map<String, BigDecimal> targetRate = jSONCurrencyFileRepository.getCurrencyRates(baseCurrency, targetCurrency);
        BigDecimal baseAmount = targetRate.get(baseCurrency.getCurrencyCode());
        BigDecimal targetAmount = targetRate.get(targetCurrency.getCurrencyCode());
        assertEquals(BigDecimal.ONE, baseAmount);
        assertEquals(new BigDecimal("0.037968493"), targetAmount);
    }

    @DisplayName("Debería devolver un Optional con un Map vacío cuando los códigos de " +
            "divisa no existan.")
    @Test
    void testReadInvalidCurrencyPair() throws IOException {
        JSONObject invalidData = new JSONObject("{}");
        invalidData.put("usd", 0.75);
        jSONCurrencyFileRepository = new JSONCurrencyFileRepository(testFilePath.toString(), exchangeAPIParser);
        jSONCurrencyFileRepository.updateCurrencyRates(invalidData);
        Map<String, BigDecimal> response = jSONCurrencyFileRepository.getCurrencyRates(baseCurrency, targetCurrency);
        assertEquals(Collections.emptyMap(), response);
    }

    @DisplayName("Debería actualizar el repositorio solo cuando los datos del "
            + "json sean diferentes.")
    @Test
    void testUpdateWhenThereIsNewData() throws IOException {
        Map<String, BigDecimal> unexpected = new HashMap<>();
        unexpected.put("gbp", new BigDecimal("0.038013551"));
        JSONObject currentData = new JSONObject("{}")
                .put("gbp", 0.038013551);
        JSONObject newData = new JSONObject("{}")
                .put("gbp", 0.038013554);
        jSONCurrencyFileRepository = new JSONCurrencyFileRepository(testFilePath.toString(), exchangeAPIParser);
        jSONCurrencyFileRepository.updateCurrencyRates(currentData);
        jSONCurrencyFileRepository.updateCurrencyRates(currentData);
        jSONCurrencyFileRepository.updateCurrencyRates(newData);
        jSONCurrencyFileRepository.updateCurrencyRates(newData);
        Map<String, BigDecimal> result = jSONCurrencyFileRepository.getCurrencyRates(baseCurrency, targetCurrency);
        assertNotEquals(unexpected.get("gbp"), result.get("gbp"));
    }
}
