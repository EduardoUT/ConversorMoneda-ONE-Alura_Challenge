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

import static io.github.eduardout.converter.currency.ISO4217Currency.*;

import java.math.BigDecimal;
import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author EduardoUT
 */
@ExtendWith(MockitoExtension.class)
class CurrencyConverterTest {

    private CurrencyConverter currencyConverter;
    private IllegalArgumentException illegalArgumentException;
    private final BigDecimal amount = new BigDecimal("100.00");
    private final CurrencyUnit baseDefault = new CurrencyUnit(MXN);
    private final CurrencyUnit targetDefault = new CurrencyUnit(EUR);
    private RateProviderService rateProviderService;
    private static final CurrencyUnit mexicanCurrency = new CurrencyUnit(MXN);
    private static final CurrencyUnit greatBritainCurrency = new CurrencyUnit(GBP);
    private static final CurrencyUnit euroCurrency = new CurrencyUnit(EUR);
    private static final CurrencyUnit dolarCurrency = new CurrencyUnit(USD);

    @SuppressWarnings("unused")
    @BeforeEach
    void setUpCurrencyConverter() {
        rateProviderService = mock(RateProviderService.class);
        currencyConverter = new CurrencyConverter(rateProviderService);
    }

    @DisplayName("Debería comprobar que el código ISO del argumento base sea "
            + "diferenta al código ISO del argumento target.")
    @Test
    void testBaseCurrencyUnitReference() {
        CurrencyUnit sameReferenceAsTarget = targetDefault;
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(sameReferenceAsTarget, targetDefault, amount));
        assertEquals("Invalid base CurrencyUnit: same "
                        + "type or same reference as target CurrencyUnit argument "
                        + "were provided.",
                illegalArgumentException.getMessage()
        );
    }

    @DisplayName("Debería comprobar que el código ISO del argumento target sea "
            + "diferenta al código ISO del argumento base.")
    @Test
    void testTargetCurrencyUnitReference() {
        CurrencyUnit sameReferenceAsBase = baseDefault;
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(baseDefault, sameReferenceAsBase, amount));
        assertEquals("Invalid base CurrencyUnit: same "
                        + "type or same reference as target CurrencyUnit argument "
                        + "were provided.",
                illegalArgumentException.getMessage()
        );
    }

    @DisplayName("Debería lanzar IllegalArgumentException para argumentos de "
            + "tipo CurrencyUnit cuyo valor fue null.")
    @Test
    void testCurrencyUnitWithNullArguments() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(null, targetDefault, amount));
        assertEquals("Base CurrencyUnit is null.",
                illegalArgumentException.getMessage());
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(baseDefault, null, amount)
        );
        assertEquals("Target CurrencyUnit is null.",
                illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException para argumentos null "
            + "del tipo de interface RateProviderService")
    @Test
    void testNullRateProvider() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(null);
                    currencyConverter.convert(baseDefault, targetDefault, amount);
                });
        assertEquals("RateProviderService argument is null.", illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando se de un monto null")
    @Test
    void testNullAmount() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(baseDefault, targetDefault, null));
        assertEquals("Amount is null.", illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando el monto sea "
            + "menor o igual a 0")
    @Test
    void testMinimumAmountBoundary() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(baseDefault, targetDefault, new BigDecimal("0.00")));
        assertEquals("Minimum valid amount of 1, 1.0 or greater is required.",
                illegalArgumentException.getMessage()
        );
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(baseDefault, targetDefault, new BigDecimal("-1.00")));
        assertEquals("Minimum valid amount of 1, 1.0 or greater is required.",
                illegalArgumentException.getMessage()
        );
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando el monto sea "
            + "mayor a 999999999.9999")
    @Test
    void testMaximumAmountBoundary() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> currencyConverter.convert(baseDefault, targetDefault,
                        new BigDecimal("999999999.999").add(BigDecimal.ONE)));
        assertEquals("Maximum valid amount of 999999999.9999 was exceded.",
                illegalArgumentException.getMessage()
        );
    }

    @DisplayName("Debería cálcular correctamente la conversión de divisas de distintas fuentes de datos.")
    @ParameterizedTest
    @FieldSource({"exchangeRatesAPIDataTest", "bankOfPolandExchangeRatesAPIDataTest", "bankOfMexicoExchangeRatesAPIDataTest"})
    void testGetConversion(CurrencyUnit base, CurrencyUnit target, Map<String, BigDecimal> currencyRates, BigDecimal expectedResult) {
        when(rateProviderService.filterCurrencyRatesFromAvailableProvider())
                .thenReturn(currencyRates);
        BigDecimal resultConversion = currencyConverter.convert(base, target, amount);
        assertEquals(expectedResult, resultConversion);
    }

    static Map<String, BigDecimal> getExchangeRatesCurrencies() {
        Map<String, BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put(mexicanCurrency.getCurrencyCode(), new BigDecimal("1"));
        exchangeRates.put(dolarCurrency.getCurrencyCode(), new BigDecimal("0.051967493"));
        exchangeRates.put(euroCurrency.getCurrencyCode(), new BigDecimal("0.045709434"));
        exchangeRates.put(greatBritainCurrency.getCurrencyCode(), new BigDecimal("0.038472809"));
        return exchangeRates;
    }

    @SuppressWarnings("unused")
    static List<Arguments> exchangeRatesAPIDataTest = Arrays.asList(
            arguments(mexicanCurrency, greatBritainCurrency, getExchangeRatesCurrencies(), new BigDecimal("3.847280900")),
            arguments(mexicanCurrency, dolarCurrency, getExchangeRatesCurrencies(), new BigDecimal("5.196749300")),
            arguments(mexicanCurrency, euroCurrency, getExchangeRatesCurrencies(), new BigDecimal("4.570943400")),
            arguments(greatBritainCurrency, mexicanCurrency, getExchangeRatesCurrencies(), new BigDecimal("2599.238334800")),
            arguments(greatBritainCurrency, dolarCurrency, getExchangeRatesCurrencies(), new BigDecimal("74.032451402")),
            arguments(greatBritainCurrency, euroCurrency, getExchangeRatesCurrencies(), new BigDecimal("84.168202564")),
            arguments(dolarCurrency, mexicanCurrency, getExchangeRatesCurrencies(), new BigDecimal("1924.279857000")),
            arguments(dolarCurrency, greatBritainCurrency, getExchangeRatesCurrencies(), new BigDecimal("135.075899969")),
            arguments(dolarCurrency, euroCurrency, getExchangeRatesCurrencies(), new BigDecimal("113.690957101")),
            arguments(euroCurrency, mexicanCurrency, getExchangeRatesCurrencies(), new BigDecimal("2187.732186800")),
            arguments(euroCurrency, greatBritainCurrency, getExchangeRatesCurrencies(), new BigDecimal("118.809713115")),
            arguments(euroCurrency, dolarCurrency, getExchangeRatesCurrencies(), new BigDecimal("87.957743122"))
    );

    static Map<String, BigDecimal> getNationalBankOfPolandCurrencies() {
        Map<String, BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put(mexicanCurrency.getCurrencyCode(), new BigDecimal("0.1944"));
        exchangeRates.put(dolarCurrency.getCurrencyCode(), new BigDecimal("3.7537"));
        exchangeRates.put(euroCurrency.getCurrencyCode(), new BigDecimal("4.2507"));
        exchangeRates.put(greatBritainCurrency.getCurrencyCode(), new BigDecimal("5.0552"));
        return exchangeRates;
    }

    @SuppressWarnings("unused")
    static List<Arguments> bankOfPolandExchangeRatesAPIDataTest = Arrays.asList(
            arguments(mexicanCurrency, greatBritainCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("3.8455")),
            arguments(mexicanCurrency, dolarCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("5.1789")),
            arguments(mexicanCurrency, euroCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("4.5734")),
            arguments(greatBritainCurrency, mexicanCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("2600.4115")),
            arguments(greatBritainCurrency, dolarCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("134.6725")),
            arguments(greatBritainCurrency, euroCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("118.9263")),
            arguments(dolarCurrency, mexicanCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("1930.9156")),
            arguments(dolarCurrency, greatBritainCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("74.2542")),
            arguments(dolarCurrency, euroCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("88.3078")),
            arguments(euroCurrency, mexicanCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("2186.5741")),
            arguments(euroCurrency, greatBritainCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("84.0857")),
            arguments(euroCurrency, dolarCurrency, getNationalBankOfPolandCurrencies(), new BigDecimal("113.2403"))
    );

    static Map<String, BigDecimal> getBankOfMexicoCurrencies() {
        Map<String, BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put(mexicanCurrency.getCurrencyCode(), new BigDecimal("1"));
        exchangeRates.put(dolarCurrency.getCurrencyCode(), new BigDecimal("19.38580"));
        exchangeRates.put(euroCurrency.getCurrencyCode(), new BigDecimal("22.00288"));
        exchangeRates.put(greatBritainCurrency.getCurrencyCode(), new BigDecimal("26.13981"));
        return exchangeRates;
    }

    @SuppressWarnings("unused")
    static List<Arguments> bankOfMexicoExchangeRatesAPIDataTest = Arrays.asList(
            arguments(mexicanCurrency, greatBritainCurrency, getBankOfMexicoCurrencies(), new BigDecimal("3.82558")),
            arguments(mexicanCurrency, dolarCurrency, getBankOfMexicoCurrencies(), new BigDecimal("5.15841")),
            arguments(mexicanCurrency, euroCurrency, getBankOfMexicoCurrencies(), new BigDecimal("4.54486")),
            arguments(greatBritainCurrency, mexicanCurrency, getBankOfMexicoCurrencies(), new BigDecimal("2613.98100")),
            arguments(greatBritainCurrency, dolarCurrency, getBankOfMexicoCurrencies(), new BigDecimal("134.83999")),
            arguments(greatBritainCurrency, euroCurrency, getBankOfMexicoCurrencies(), new BigDecimal("118.80177")),
            arguments(dolarCurrency, mexicanCurrency, getBankOfMexicoCurrencies(), new BigDecimal("1938.58000")),
            arguments(dolarCurrency, greatBritainCurrency, getBankOfMexicoCurrencies(), new BigDecimal("74.16198")),
            arguments(dolarCurrency, euroCurrency, getBankOfMexicoCurrencies(), new BigDecimal("88.10574")),
            arguments(euroCurrency, mexicanCurrency, getBankOfMexicoCurrencies(), new BigDecimal("2200.28800")),
            arguments(euroCurrency, greatBritainCurrency, getBankOfMexicoCurrencies(), new BigDecimal("84.17383")),
            arguments(euroCurrency, dolarCurrency, getBankOfMexicoCurrencies(), new BigDecimal("113.49998"))
    );

    @DisplayName("Debería lanzar IllegalStateException cuando todos los RateProviderService fallen.")
    @Test
    void testDefaultRateUsedWhenNotProvided() {
        IllegalStateException illegalStateException;
        illegalStateException = assertThrowsExactly(IllegalStateException.class, () -> {
            when(rateProviderService.filterCurrencyRatesFromAvailableProvider())
                    .thenReturn(Collections.emptyMap());
            currencyConverter.convert(baseDefault, targetDefault, amount);
        });
        assertEquals("No currencies were found from provider.", illegalStateException.getMessage());
    }

    @DisplayName("Debería lanzar NoSuchElementException cuando la divisa base o target no se hayan encontrado.")
    @Test
    void testNoSuchElementException() {
        NoSuchElementException noSuchElementException;
        CurrencyUnit nonExistentBaseCurrency = new CurrencyUnit(AUD);
        CurrencyUnit nonExistentTargetCurrency = new CurrencyUnit(SDD);
        Map<String, BigDecimal> responseCurrencies = getExchangeRatesCurrencies();
        noSuchElementException = assertThrowsExactly(NoSuchElementException.class, () -> {
            when(rateProviderService.filterCurrencyRatesFromAvailableProvider())
                    .thenReturn(responseCurrencies);
            currencyConverter.convert(nonExistentBaseCurrency, nonExistentTargetCurrency, amount);
        });
        assertEquals("Currencies " + nonExistentBaseCurrency.getCurrencyCode() + " or "
                        + nonExistentTargetCurrency.getCurrencyCode() + " not supported for this currencies provider.",
                noSuchElementException.getMessage());

        noSuchElementException = assertThrowsExactly(NoSuchElementException.class, () -> {
            when(rateProviderService.filterCurrencyRatesFromAvailableProvider())
                    .thenReturn(responseCurrencies);
            currencyConverter.convert(nonExistentTargetCurrency, nonExistentBaseCurrency, amount);
        });
        assertEquals("Currencies " + nonExistentTargetCurrency.getCurrencyCode() + " or "
                        + nonExistentBaseCurrency.getCurrencyCode() + " not supported for this currencies provider.",
                noSuchElementException.getMessage());
    }
}
