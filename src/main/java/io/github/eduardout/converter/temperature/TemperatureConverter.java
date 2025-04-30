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

/**
 *
 * @author EduardoUT
 */
public class TemperatureConverter {

    private CalculableTemperature calculableTemperature;

    public TemperatureConverter(CalculableTemperature calculableTemperature) {
        validateCalculableTemperature(calculableTemperature);
        this.calculableTemperature = calculableTemperature;
    }
    
    public CalculableTemperature getCalculableTemperature() {
        validateCalculableTemperature(calculableTemperature);
        return calculableTemperature;
    }
    
    public void setCalculableTemperature(CalculableTemperature calculableTemperature) {
        validateCalculableTemperature(calculableTemperature);
        this.calculableTemperature = calculableTemperature;
    }
    
    private void validateCalculableTemperature(CalculableTemperature calculableTemperature) {
        if (calculableTemperature == null) {
            throw new IllegalArgumentException("Class instance is null.");
        }
    }

    public Double compute(Double base) {
        if (Double.isInfinite(base) || Double.isNaN(base)) {
            throw new IllegalArgumentException("Base value out of boundary.");
        }
        return Double.valueOf(
                String.format(
                        "%1$.2f",
                        calculableTemperature.applyFormula(base)
                )
        );
    }
}
