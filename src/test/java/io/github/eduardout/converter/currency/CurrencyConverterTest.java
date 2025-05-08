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
import static io.github.eduardout.converter.currency.CurrencyConverter.DEFAULT_AMOUNT;

import io.github.eduardout.converter.currency.provider.RateProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author EduardoUT
 */
@ExtendWith(MockitoExtension.class)
class CurrencyConverterTest {

    private CurrencyConverter currencyConverter;
    private IllegalArgumentException illegalArgumentException;
    private final BigDecimal amount = new BigDecimal("100.00");
    private final CurrencyUnit baseDefault = new CurrencyUnit(USD);
    private final CurrencyUnit targetDefault = new CurrencyUnit(EUR);
    private RateProvider rateProvider;

    @SuppressWarnings("unused")
    @BeforeEach
    void setUpRateProviderMock() {
        rateProvider = mock(RateProvider.class);
    }

    BigDecimal calculateAmount(BigDecimal baseAmount, BigDecimal targetAmount) {
        return amount
                .multiply(DEFAULT_AMOUNT.divide(baseAmount, baseAmount.scale(), RoundingMode.HALF_UP))
                .divide(DEFAULT_AMOUNT.divide(targetAmount, targetAmount.scale(), RoundingMode.HALF_UP),
                        4, RoundingMode.HALF_UP
                );
    }

    @DisplayName("Debería comprobar que el código ISO del argumento base sea "
            + "diferenta al código ISO del argumento target.")
    @Test
    void testBaseCurrencyUnitReference() {
        CurrencyUnit sameReferenceAsTarget = targetDefault;
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(sameReferenceAsTarget, targetDefault, amount);
                });
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
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(baseDefault, sameReferenceAsBase, amount);
                });
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
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(null, targetDefault, amount);
                });
        assertEquals("Base CurrencyUnit is null.",
                illegalArgumentException.getMessage());
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(baseDefault, null, amount);
                }
        );
        assertEquals("Target CurrencyUnit is null.",
                illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException para argumentos null "
            + "del tipo de interface RateProvider")
    @Test
    void testNullRateProvider() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(null);
                    currencyConverter.convert(baseDefault, targetDefault, amount);
                });
        assertEquals("RateProvider argument is null.", illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando se de un monto null")
    @Test
    void testNullAmount() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(baseDefault, targetDefault, null);
                });
        assertEquals("Amount is null.", illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando el monto sea "
            + "menor o igual a 0")
    @Test
    void testMinimumAmountBoundary() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(baseDefault, targetDefault, new BigDecimal("0.00"));
                });
        assertEquals("Minimum valid amount of 1, 1.0 or greater is required.",
                illegalArgumentException.getMessage()
        );
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(baseDefault, targetDefault, new BigDecimal("-1.00"));
                });
        assertEquals("Minimum valid amount of 1, 1.0 or greater is required.",
                illegalArgumentException.getMessage()
        );
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando el monto sea "
            + "mayor a 999999999.9999")
    @Test
    void testMaximumAmountBoundary() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    currencyConverter = new CurrencyConverter(rateProvider);
                    currencyConverter.convert(baseDefault, targetDefault,
                            new BigDecimal("999999999.999").add(BigDecimal.ONE));
                });
        assertEquals("Maximum valid amount of 999999999.9999 was exceded.",
                illegalArgumentException.getMessage()
        );
    }

    @DisplayName("Debería cálcular correctamente la conversión")
    @Test
    void testGetConversion() {
        Map<String, BigDecimal> expectedRate = new HashMap<>();
        expectedRate.put(baseDefault.getCurrencyCode(), new BigDecimal("0.05075398"));
        expectedRate.put(targetDefault.getCurrencyCode(), new BigDecimal("0.044226646"));
        when(rateProvider.getCurrencyRates(baseDefault, targetDefault))
                .thenReturn(expectedRate);
        currencyConverter = new CurrencyConverter(rateProvider);
        BigDecimal result = currencyConverter.convert(baseDefault, targetDefault, amount);
        assertEquals(calculateAmount(expectedRate.get(baseDefault.getCurrencyCode()),
                expectedRate.get(targetDefault.getCurrencyCode())), result);
    }

    @DisplayName("Debería cálcular correctamente la conversión inversa")
    @Test
    void testReverseConversion() {
        Map<String, BigDecimal> expectedRate = new HashMap<>();
        expectedRate.put(baseDefault.getCurrencyCode(), new BigDecimal("0.05075398"));
        expectedRate.put(targetDefault.getCurrencyCode(), new BigDecimal("0.044226646"));
        when(rateProvider.getCurrencyRates(targetDefault, baseDefault))
                .thenReturn(expectedRate);
        currencyConverter = new CurrencyConverter(rateProvider);
        BigDecimal result = currencyConverter.convert(targetDefault, baseDefault, amount);
        assertEquals(calculateAmount(expectedRate.get(targetDefault.getCurrencyCode()),
                expectedRate.get(baseDefault.getCurrencyCode())), result);
    }

    @DisplayName("Debería lanzar IllegalStateException cuando todos los RateProvider fallen.")
    @Test
    void testDefaultRateUsedWhenNotProvided() {
        IllegalStateException illegalStateException;
        illegalStateException = assertThrowsExactly(IllegalStateException.class, () -> {
            when(rateProvider.getCurrencyRates(baseDefault, targetDefault))
                    .thenReturn(Collections.emptyMap());
            currencyConverter = new CurrencyConverter(rateProvider);
            currencyConverter.convert(baseDefault, targetDefault, amount);
        });
        assertEquals("No currencies were found from provider.", illegalStateException.getMessage());
    }
}
