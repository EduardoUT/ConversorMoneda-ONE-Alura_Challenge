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
package io.github.eduardout.converter.currency.config;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PropertiesConfigTest {

    private PropertiesConfig propertiesConfig;
    private IllegalArgumentException argumentException;
    private IllegalStateException stateException;
    private String propertiesKeyPrefix;
    private String propertiesPath;

    @BeforeEach
    void setUpClass() throws IOException {
        propertiesKeyPrefix = "testp.";
        propertiesPath = "test.properties";
        propertiesConfig = PropertiesConfig.fromFile(propertiesPath);
    }

    @Test
    void fromFile_NullOrEmptyArguments_ThrowsException() {
        String messagePathException = "Properties path cannot be null "
                + "or empty.";
        propertiesPath = null;
        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig = PropertiesConfig.fromFile(propertiesPath)
        );
        assertEquals(messagePathException, argumentException.getMessage());
        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig = PropertiesConfig.fromFile("")
        );
        assertEquals(messagePathException, argumentException.getMessage());
    }

    @Test
    void fromFileNotFound_ThrowsException() {
        FileNotFoundException notFoundException;
        notFoundException = assertThrowsExactly(FileNotFoundException.class,
                () -> propertiesConfig = PropertiesConfig.fromFile("not_exist.properties")
        );
        assertEquals("Properties file not found.", notFoundException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException si el key o value "
            + "son null o vacíos.")
    @Test
    void fromFileEmptyProperties_ThrowsException() {
        String expectedMessage = "Properties file cannot be left empty.";
        stateException = assertThrowsExactly(IllegalStateException.class,
                () -> propertiesConfig = PropertiesConfig.fromFile("intentionalEmptyFile.properties")
        );
        assertEquals(expectedMessage, stateException.getMessage());
    }

    @Test
    void getPropertyValueValidArguments() {
        assertEquals("https://firstkey.com", propertiesConfig.getPropertyValue(propertiesKeyPrefix, "first.key"));
    }

    @DisplayName("Debería lanzar IllegalArgumentException para un key "
            + "null o vacío")
    @Test
    void getPropertyValueInvalidArguments_ThrowsException() {
        String keyInvalidMessage = "Property key provided is null or empty.";
        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getPropertyValue(propertiesKeyPrefix, null)
        );
        assertEquals(keyInvalidMessage, argumentException.getMessage());
        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getPropertyValue(propertiesKeyPrefix, "")
        );
        assertEquals(keyInvalidMessage, argumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException para el argumento propertyKeyPrefix " +
            "null o vacío y verificar si finaliza con un punto")
    @Test
    void testPropertyKeyPrefix() {
        String badPropertyKeyPrefix = "testp-";
        String prefixInvalidMessageOne = "The property key prefix is null or empty.";
        String prefixInvalidMessageTwo = "The property key prefix must end with a point " +
                "character the given was: " + badPropertyKeyPrefix;
        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getPropertyValue(null, "first.key")
        );
        assertEquals(prefixInvalidMessageOne, argumentException.getMessage());

        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getPropertyValue("", "first.key")
        );
        assertEquals(prefixInvalidMessageOne, argumentException.getMessage());

        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getPropertyValue(badPropertyKeyPrefix, "first.key"));
        assertEquals(prefixInvalidMessageTwo, argumentException.getMessage());

        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getKeyProperties(null)
        );
        assertEquals(prefixInvalidMessageOne, argumentException.getMessage());

        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getKeyProperties("")
        );
        assertEquals(prefixInvalidMessageOne, argumentException.getMessage());

        argumentException = assertThrowsExactly(IllegalArgumentException.class,
                () -> propertiesConfig.getKeyProperties(badPropertyKeyPrefix)
        );
        assertEquals(prefixInvalidMessageTwo, argumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalStateException cuando la propiedad no "
            + "sea una de las asignadas en el key Set")
    @Test
    void getPropertyValueNotInKeySet_ThrowsException() {
        String notAKey = "not.exist";
        stateException = assertThrowsExactly(IllegalStateException.class,
                () -> propertiesConfig.getPropertyValue(propertiesKeyPrefix, notAKey)
        );
        assertEquals("The given key " + notAKey
                        + " does not exists on the loaded properties file or "
                        + "check prefix for " + propertiesKeyPrefix,
                stateException.getMessage()
        );
    }

    @DisplayName("Debería lanzar IllegalStateException cuando la url no cumpla "
            + "con el formato esperado.")
    @Test
    void getPropertyValueInvalidUrlFormat() throws IOException {
        String key = "url";
        String invalidUrlValue = "http://example.com";
        String expectedMessage = "Invalid URL for  '" + key + "': "
                + invalidUrlValue;
        propertiesConfig = PropertiesConfig.fromFile(propertiesPath);
        stateException = assertThrowsExactly(IllegalStateException.class,
                () -> propertiesConfig.getPropertyValue("invalid.", key)
        );
        assertEquals(expectedMessage, stateException.getMessage());
    }
}
