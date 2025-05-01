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

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author EduardoUT
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TemperatureConverterTest {

    private TemperatureConverter temperatureConverter;

    @SuppressWarnings("unused")
    @BeforeAll
    void setUp() {
        temperatureConverter = new TemperatureConverter(new CelsiusToFarenheit());
    }

    @Test
    void testNullConstructor() {
        IllegalArgumentException argumentException;
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            temperatureConverter.setCalculableTemperature(null);
        });
        assertEquals("Class instance is null.", argumentException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidBoundaryValues")
    void testBoundaries(Double value) {
        IllegalArgumentException argumentException;
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            temperatureConverter.compute(value);
        });
        assertEquals("Base value out of boundary.", argumentException.getMessage());
    }

    @SuppressWarnings("unused")
    static Stream<Double> invalidBoundaryValues() {
        return Stream.of(
                Double.MAX_VALUE * 2,
                -Double.MAX_VALUE * 2,
                0.0 / 0.0,
                Math.sqrt(-1),
                Double.POSITIVE_INFINITY
        );
    }

    @Test
    void testCelciusToFarenheit() {
        temperatureConverter.setCalculableTemperature(new CelsiusToFarenheit());
        assertEquals(113.0, temperatureConverter.compute(45d));
    }

    @Test
    void testCelsiusToKelvin() {
        temperatureConverter.setCalculableTemperature(new CelsiusToKelvin());
        assertEquals(296.15, temperatureConverter.compute(23d));
    }

    @Test
    void testFarenheitToCelsius() {
        temperatureConverter.setCalculableTemperature(new FarenheitToCelsius());
        assertEquals(-3.69, temperatureConverter.compute(25.36));
    }

    @Test
    void testFarenheitToKelvin() {
        temperatureConverter.setCalculableTemperature(new FarenheitToKelvin());
        assertEquals(322.04, temperatureConverter.compute(120d));
    }

    @Test
    void testKelvinToCelsius() {
        temperatureConverter.setCalculableTemperature(new KelvinToCelsius());
        assertEquals(-3.15, temperatureConverter.compute(270d));
    }

    @Test
    void testKelvinToFarenheit() {
        temperatureConverter.setCalculableTemperature(new KelvinToFarenheit());
        assertEquals(8.33, temperatureConverter.compute(260d));
    }
}
