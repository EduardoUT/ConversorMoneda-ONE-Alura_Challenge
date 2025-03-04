/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.hithub.eduardout.converter.currency;

import io.github.eduardout.converter.currency.CurrencyCodeIso4217;
import io.github.eduardout.converter.currency.CurrencyUnit;
import java.util.Currency;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author eduar
 */
class CurrencyUnitTest {

    private CurrencyUnit currencyUnit = new CurrencyUnit(CurrencyCodeIso4217.USD);

    @DisplayName("Debería aceptar códigos de divisa ISO 4217 válidos, "
            + "compatibles con Currency.")
    @MethodSource("currenciesEnumType")
    @ParameterizedTest
    void testCompatibleCurrencies(CurrencyCodeIso4217 currencyCodeIso4217) {
        Currency enumCurrencyCode = Currency.getInstance(currencyCodeIso4217.toString());
        assertTrue(currencyUnit.getCurrencies().contains(enumCurrencyCode));
    }

    @SuppressWarnings("unused")
    static Set<Currency> currenciesProvider() {
        return Currency.getAvailableCurrencies();
    }

    @SuppressWarnings("unused")
    static CurrencyCodeIso4217[] currenciesEnumType() {
        return CurrencyCodeIso4217.values();
    }
}
