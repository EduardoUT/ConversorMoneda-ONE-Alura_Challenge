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

import static io.github.eduardout.converter.temperature.TemperatureSymbol.KELVIN;
import static io.github.eduardout.converter.temperature.TemperatureSymbol.CELSIUS;
import java.util.Objects;

/**
 *
 * @author EduardoUT
 */
public class CelsiusToKelvin implements CalculableTemperature {

    @Override
    public Double applyFormula(Double target) {
        return target + KELVIN.getFreezingPoint();
    }

    @Override
    public TemperatureSymbol getBaseSymbol() {
        return CELSIUS;
    }

    @Override
    public TemperatureSymbol getTargetSymbol() {
        return KELVIN;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CelsiusToKelvin other = (CelsiusToKelvin) obj;
        return Objects.equals(getBaseSymbol(), other.getBaseSymbol())
                && Objects.equals(getTargetSymbol(), other.getTargetSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBaseSymbol(), getTargetSymbol());
    }
}
