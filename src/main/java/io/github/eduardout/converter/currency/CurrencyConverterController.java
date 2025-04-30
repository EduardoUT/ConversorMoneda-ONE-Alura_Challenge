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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author EduardoUT
 */
public class CurrencyConverterController {

    private FreeCurrencyExchangeRatesService currencyService;
    private CurrencyConverter currencyConverter;

    public CurrencyConverterController(CurrencyConverter currencyConverter, FreeCurrencyExchangeRatesService exhangeRatesService) {
        this.currencyConverter = currencyConverter;
        this.currencyService = exhangeRatesService;
    }

    public void setConversion(JTextField campoIngresoDivisa, JTextField campoConversion, JComboBox<CurrencyUnit> base,
            JComboBox<CurrencyUnit> target) {
        CurrencyUnit baseSelection = (CurrencyUnit) base.getSelectedItem();
        CurrencyUnit targetSelection = (CurrencyUnit) target.getSelectedItem();
        BigDecimal amount = new BigDecimal(campoIngresoDivisa.getText());
        BigDecimal result = currencyConverter.convert(baseSelection, targetSelection, amount);
        campoConversion.setText(result.toString());
    }

    public void loadAvailableCurrencies(JComboBox<CurrencyUnit> comboBoxCurrencies) {
        Optional<List<CurrencyUnit>> apiAvailableCurrencies = currencyService.availableCurrencyCodes();
        apiAvailableCurrencies
                .orElseGet(Collections::emptyList)
                .forEach(item -> comboBoxCurrencies.addItem(item));
    }

}
