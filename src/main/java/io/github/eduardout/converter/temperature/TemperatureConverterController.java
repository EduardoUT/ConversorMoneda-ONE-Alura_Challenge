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
package io.github.eduardout.converter.temperature;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author EduardoUT
 */
public class TemperatureConverterController {

    private final TemperatureConverter temperatureConverter;
    private Set<CalculableTemperature> formulas;
    private final CelsiusToFarenheit defaultFormula;

    public TemperatureConverterController(TemperatureConverter temperatureConverter) {
        defaultFormula = new CelsiusToFarenheit();
        this.temperatureConverter = temperatureConverter;
        initializeFormulas();
    }

    private void initializeFormulas() {
        formulas = new HashSet<>();
        formulas.add(defaultFormula);
        formulas.add(new CelsiusToKelvin());
        formulas.add(new FarenheitToCelsius());
        formulas.add(new FarenheitToKelvin());
        formulas.add(new KelvinToCelsius());
        formulas.add(new KelvinToFarenheit());
    }

    public void setConversion(JComboBox<TemperatureSymbol> baseTemperatureComboBox,
            JComboBox<TemperatureSymbol> targetTemperatureComboBox,
            JTextField inputTemperature, JTextField outputTemperature) {
        int baseTempidx = baseTemperatureComboBox.getSelectedIndex();
        int targetTempidx = targetTemperatureComboBox.getSelectedIndex();
        TemperatureSymbol baseTemperature = baseTemperatureComboBox.getItemAt(baseTempidx);
        TemperatureSymbol targetTemperature = targetTemperatureComboBox.getItemAt(targetTempidx);
        Optional<CalculableTemperature> selectedFormula = filterFormula(baseTemperature, targetTemperature);
        temperatureConverter.setCalculableTemperature(selectedFormula.orElse(temperatureConverter.getCalculableTemperature()));
        Double temperature = Double.valueOf(inputTemperature.getText());
        Double result = temperatureConverter.compute(temperature);
        outputTemperature.setText(result.toString());
    }

    private Optional<CalculableTemperature> filterFormula(TemperatureSymbol baseTemperature,
            TemperatureSymbol targetTemperature) {
        return formulas
                .stream()
                .filter(formula -> formula.getBaseSymbol().equals(baseTemperature)
                && formula.getTargetSymbol().equals(targetTemperature))
                .findFirst();
    }

    public void loadComboBoxSymbols(JComboBox<TemperatureSymbol> comboBoxSymbols) {
        List<TemperatureSymbol> symbols = Arrays.asList(TemperatureSymbol.values());
        symbols.forEach(symbol -> comboBoxSymbols.addItem(symbol));
    }
}
