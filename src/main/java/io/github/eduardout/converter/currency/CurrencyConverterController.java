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

import java.math.BigDecimal;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author EduardoUT
 */
public class CurrencyConverterController {

    private final RateProviderService rateProviderService;
    private final CurrencyConverter currencyConverter;
    private List<CurrencyUnit> availableCurrencyUnits;

    public CurrencyConverterController(CurrencyConverter currencyConverter, RateProviderService rateProviderService) {
        this.currencyConverter = currencyConverter;
        this.rateProviderService = rateProviderService;
        loadAvailableCurrencyUnits();
    }

    private void loadAvailableCurrencyUnits() {
        availableCurrencyUnits = rateProviderService.availableCurrencyUnits();
    }

    public void setConversion(JTextField campoIngresoDivisa, JTextField campoConversion, JComboBox<CurrencyUnit> base,
            JComboBox<CurrencyUnit> target) {
        CurrencyUnit baseSelection = (CurrencyUnit) base.getSelectedItem();
        CurrencyUnit targetSelection = (CurrencyUnit) target.getSelectedItem();
        BigDecimal amount = new BigDecimal(campoIngresoDivisa.getText());
        BigDecimal result = currencyConverter.convert(baseSelection, targetSelection, amount);
        campoConversion.setText(result.toString());
    }

    public void loadAvailableCurrencies(JComboBox<CurrencyUnit> baseComboBoxCurrencies,
            JComboBox<CurrencyUnit> targetComboBoxCurrencies) {
        addCurrenciesAsComboBoxItems(availableCurrencyUnits, baseComboBoxCurrencies);
        addCurrenciesAsComboBoxItems(availableCurrencyUnits, targetComboBoxCurrencies);
    }

    private void addCurrenciesAsComboBoxItems(List<CurrencyUnit> currencies,
            JComboBox<CurrencyUnit> comboBox) {
        currencies.forEach(comboBox::addItem);
    }

    /**
     * Method to check the disponibility of the rate provider.
     *
     * @return A boolean value to validate if the currency list of the rate
     * provider are not empty.
     */
    public boolean hasAvailableCurrencyUnits() {
        loadAvailableCurrencyUnits();
        return !availableCurrencyUnits.isEmpty();
    }
}
