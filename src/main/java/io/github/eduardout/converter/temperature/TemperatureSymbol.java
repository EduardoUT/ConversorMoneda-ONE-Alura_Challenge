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
public enum TemperatureSymbol {
    CELSIUS("Celsius", "°C", 0d),
    FARENHEIT("Farenheit", "°F", 32d),
    KELVIN("Kelvin", "°K", 273.15);

    private final String symbolName;
    private final String symbol;
    private final Double freezingPoint;

    private TemperatureSymbol(String symbolName, String symbol, Double freezingPoint) {
        this.symbolName = symbolName;
        this.symbol = symbol;
        this.freezingPoint = freezingPoint;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getFreezingPoint() {
        return freezingPoint;
    }

    @Override
    public String toString() {
        return symbolName + " - " + symbol;
    }
}
