/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.eduardout.converter.currency;

import java.util.Currency;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a CurrencyUnit which gets a new Currency instance from
 * Java API in order to provide an ISO 4217 currency code from
 * CurrencyCodeIso4217 Enum type.
 *
 * From this class we can set and obtain the currency code, a numeric code and
 * its symbol.
 *
 * @author Eduardo Reyes Hernández
 */
public class CurrencyUnit {

    private Currency currency;

    public CurrencyUnit(CurrencyCodeIso4217 currencyCode) {
        if (currencyCode == null) {
            throw new IllegalArgumentException("Entered currency code is null.");
        }
        this.currency = Currency.getInstance(currencyCode.toString());
    }

    /**
     * The Currency object provided by Java API.
     *
     * @return
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * The ISO 4217 CurrencyCode.
     *
     * @return
     */
    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    /**
     * The symbol of the CurrencyUnit.
     *
     * @return
     */
    public String getSymbol() {
        return currency.getSymbol();
    }

    public Set<Currency> getCurrencies() {
        return Currency.getAvailableCurrencies();
    }

    /**
     * The comparing constrains are:
     *
     * - The argument is the same as the current.
     *
     * - The object is null or the name class are different.
     *
     * - To compare unique CurrencyUnirs based in its own numeric code.
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        CurrencyUnit otherCurrencyUnit = (CurrencyUnit) object;
        return Objects.equals(currency.getNumericCode(),
                otherCurrencyUnit.getCurrency().getNumericCode()
        );
    }

    /**
     * Hashing the numeric code property from currency.
     *
     * @return Hash code of the numeric code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(currency.getNumericCode());
    }
}
