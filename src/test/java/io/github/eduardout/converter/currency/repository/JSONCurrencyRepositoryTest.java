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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author EduardoUT
 */
class JSONCurrencyRepositoryTest {

    @TempDir
    static Path tempDir;

    private JSONCurrencyRepository jSONCurrencyRepository;
    private Path testFilePath;
    private CurrencyUnit baseCurrency;
    private CurrencyUnit targetCurrency;

    @SuppressWarnings("unused")
    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("test-rates.json");
        baseCurrency = new CurrencyUnit(MXN);
        targetCurrency = new CurrencyUnit(GBP);
    }


    @Test
    void testReadWriteOperations() throws IOException {
        jSONCurrencyRepository = new JSONCurrencyRepository(testFilePath.toString());
        Map<String, BigDecimal> currencyRates = new HashMap<>();
        currencyRates.put("GBP", new BigDecimal("0.037968493"));
        currencyRates.put("MXN", new BigDecimal("1"));
        jSONCurrencyRepository.updateCurrencyRates(currencyRates);
        Map<String, BigDecimal> repositoryCurrencyRates = jSONCurrencyRepository.getCurrencyRates();
        BigDecimal baseAmount = repositoryCurrencyRates.get(baseCurrency.getCurrencyCode());
        BigDecimal targetAmount = repositoryCurrencyRates.get(targetCurrency.getCurrencyCode());
        assertEquals(BigDecimal.ONE, baseAmount);
        assertEquals(new BigDecimal("0.037968493"), targetAmount);
    }

    @DisplayName("Debería validar como inexistentes divisas que no estan registradas.")
    @Test
    void testReadInvalidCurrencyPair() throws IOException {
        Map<String, BigDecimal> currencyRates = new HashMap<>();
        currencyRates.put("USD", new BigDecimal("0.75"));
        jSONCurrencyRepository = new JSONCurrencyRepository(testFilePath.toString());
        Map<String, BigDecimal> response = jSONCurrencyRepository.getCurrencyRates();
        for(String key : currencyRates.keySet()) {
            assertFalse(response.containsKey(key));
        }
    }

    @DisplayName("Debería actualizar el repositorio solo cuando los datos del "
            + "json sean diferentes.")
    @Test
    void testUpdateWhenThereIsNewData() throws IOException {
        Map<String, BigDecimal> unexpected = new HashMap<>();
        unexpected.put("GBP", new BigDecimal("0.038013551"));
        Map<String, BigDecimal> currentData = new HashMap<>();
        currentData.put("GBP", new BigDecimal("0.038013551"));
        Map<String, BigDecimal> newData = new HashMap<>();
        newData.put("GBP", new BigDecimal("0.038013554"));
        jSONCurrencyRepository = new JSONCurrencyRepository(testFilePath.toString());
        jSONCurrencyRepository.updateCurrencyRates(currentData);
        jSONCurrencyRepository.updateCurrencyRates(currentData);
        jSONCurrencyRepository.updateCurrencyRates(newData);
        jSONCurrencyRepository.updateCurrencyRates(newData);
        Map<String, BigDecimal> result = jSONCurrencyRepository.getCurrencyRates();
        assertNotEquals(unexpected.get("GBP"), result.get("GBP"));
    }
}
