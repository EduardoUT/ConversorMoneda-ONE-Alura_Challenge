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

import static io.github.eduardout.converter.GlobalLogger.*;
import static io.github.eduardout.converter.currency.ISO4217Currency.*;
import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.currency.provider.APIClient;
import io.github.eduardout.converter.currency.provider.FreeCurrencyExchangeRates;
import io.github.eduardout.converter.currency.provider.RateProvider;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import io.github.eduardout.converter.util.DefaultRateParser;
import io.github.eduardout.converter.util.RateParser;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author EduardoUT
 */
public class CurrencyConverterTest {

    private CurrencyConverter currencyConverter;
    private IllegalArgumentException illegalArgumentException;
    private BigDecimal amount = new BigDecimal("100.00");
    private CurrencyUnit baseDefault = new CurrencyUnit(MXN);
    private CurrencyUnit targetDefault = new CurrencyUnit(USD);
    private RateProvider rateProvider;

    void setUpApiProvider(RateProvider rateProvider) {
        this.rateProvider = rateProvider;
    }

    void setUp(BigDecimal amount, CurrencyUnit base, CurrencyUnit target) {
        try {
            APIClient apiClient = APIClient.getInstance();
            PropertiesConfig propertiesConfig = PropertiesConfig
                    .fromFile("config.properties", "fcera.");
            JSONCurrencyFileRepository repository = new JSONCurrencyFileRepository("");
            RateParser rateParser = new DefaultRateParser();
            setUpApiProvider(new FreeCurrencyExchangeRates(apiClient, propertiesConfig, repository, rateParser));
            currencyConverter = new CurrencyConverter(
                    base,
                    target,
                    rateProvider,
                    amount);
        } catch (IOException e) {
            registerLogException(Level.SEVERE, "Error on property file {0}", e);
        }
    }

    @DisplayName("Debería comprobar que el código ISO del argumento base sea "
            + "diferenta al código ISO del argumento target.")
    @Test
    void testBaseCurrencyUnitReference() {
        CurrencyUnit sameReferenceAsTarget = targetDefault;
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(amount, sameReferenceAsTarget, targetDefault);
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
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(amount, baseDefault, sameReferenceAsBase);
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
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(amount, null, targetDefault);
        });
        assertEquals("Base CurrencyUnit is null.",
                illegalArgumentException.getMessage());
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(amount, baseDefault, null);
        });
        assertEquals("Target CurrencyUnit is null.",
                illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException para argumentos null "
            + "del tipo de interface RateProvider")
    @Test
    void testNullRateProvider() {

    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando se de un monto null")
    @Test
    void testNullAmount() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(null, baseDefault, targetDefault);
        });
        assertEquals("Amount is null.", illegalArgumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando el monto sea "
            + "menor o igual a 0")
    @Test
    void testMinimumAmountBoundary() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(new BigDecimal("0.00"), baseDefault, targetDefault);
        });
        assertEquals("Minimum valid amount of 1, 1.0 or greater is required.",
                illegalArgumentException.getMessage()
        );
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(new BigDecimal("-1.00"), baseDefault, targetDefault);
        });
        assertEquals("Minimum valid amount of 1, 1.0 or greater is required.",
                illegalArgumentException.getMessage()
        );
    }

    @DisplayName("Debería lanzar IllegalArgumentException cuando el monto sea "
            + "mayor a 999999999.9999")
    @Test
    void testMaximumAmountBoundary() {
        illegalArgumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            setUp(new BigDecimal("999999999.999").add(BigDecimal.ONE), baseDefault, targetDefault);
        });
        assertEquals("Maximum valid amount of 999999999.9999 was exceded.",
                illegalArgumentException.getMessage()
        );
    }
}
