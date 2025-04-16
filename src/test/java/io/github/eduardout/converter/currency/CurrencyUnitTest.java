/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.eduardout.converter.currency;

import java.util.Currency;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author eduar
 */
class CurrencyUnitTest {

    private CurrencyUnit currencyUnit = new CurrencyUnit(ISO4217Currency.USD);

    @DisplayName("Debería aceptar códigos de divisa ISO 4217 válidos, "
            + "compatibles con Currency.")
    @MethodSource("currenciesEnumType")
    @ParameterizedTest
    void testCompatibleCurrencies(ISO4217Currency currencyCodeIso4217) {
        Currency enumCurrencyCode = Currency.getInstance(currencyCodeIso4217.toString());
        assertTrue(currencyUnit.getCurrencies().contains(enumCurrencyCode));
    }

    @SuppressWarnings("unused")
    static Set<Currency> currenciesProvider() {
        return Currency.getAvailableCurrencies();
    }

    @SuppressWarnings("unused")
    static ISO4217Currency[] currenciesEnumType() {
        return ISO4217Currency.values();
    }
}
