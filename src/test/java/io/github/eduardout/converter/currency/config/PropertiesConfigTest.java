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
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
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

    void setUpClass() throws IOException {
        propertiesKeyPrefix = "testp.";
        propertiesPath = "test.properties";
        propertiesConfig = PropertiesConfig.fromFile(propertiesPath, propertiesKeyPrefix);
    }

    @Test
    void fromFile_NullOrEmptyArguments_ThrowsException()
            throws IOException {
        setUpClass();
        String messagePathException = "Properties path cannot be null "
                + "or empty.";
        String messagePrefixException = "The property key prefix is null or empty.";
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            propertiesConfig = PropertiesConfig.fromFile(null, propertiesKeyPrefix);
        });
        assertEquals(messagePathException, argumentException.getMessage());
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            propertiesConfig = PropertiesConfig.fromFile("", propertiesKeyPrefix);
        });
        assertEquals(messagePathException, argumentException.getMessage());
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            propertiesConfig = PropertiesConfig.fromFile(propertiesPath, null);
        });
        assertEquals(messagePrefixException, argumentException.getMessage());
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            propertiesConfig = PropertiesConfig.fromFile(propertiesPath, "");
        });
        assertEquals(messagePrefixException, argumentException.getMessage());
    }

    @Test
    void fromFileValidPrefix() throws IOException {
        setUpClass();
        assertEquals("https://firstkey.com", propertiesConfig.getPropertyValue("first.key"));
    }

    @Test
    void fromFileNotFound_ThrowsException() throws IOException {
        FileNotFoundException notFoundException;
        notFoundException = assertThrowsExactly(FileNotFoundException.class, () -> {
            propertiesConfig = PropertiesConfig.fromFile("not_exist.properties", "random.");
        });
        assertEquals("Properties file not found.", notFoundException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException si el key o value "
            + "son null o vacíos.")
    @Test
    void fromFileEmptyProperties_ThrowsException() throws IOException {
        String expectedMessage = "Properties file cannot be left empty.";
        stateException = assertThrowsExactly(IllegalStateException.class, () -> {
            propertiesConfig = PropertiesConfig.fromFile("intentionalEmptyFile.properties", "some.");
        });
        assertEquals(expectedMessage, stateException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalArgumentException para un key,"
            + "null o vacío.")
    @Test
    void getPropertyValueNullOrEmpty_ThrowsException() throws IOException {
        String expectedMessage = "Property key provided is null or empty.";
        setUpClass();
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            propertiesConfig.getPropertyValue(null);
        });
        assertEquals(expectedMessage, argumentException.getMessage());
        argumentException = assertThrowsExactly(IllegalArgumentException.class, () -> {
            propertiesConfig.getPropertyValue("");
        });
        assertEquals(expectedMessage, argumentException.getMessage());
    }

    @DisplayName("Debería lanzar IllegalStateException cuando la propiedad no "
            + "sea una de las asignadas en el key Set")
    @Test
    void getPropertyValueNotInKeySet_ThrowsException() throws IOException, URISyntaxException {
        setUpClass();
        String notAKey = "not.exist";
        stateException = assertThrowsExactly(IllegalStateException.class, () -> {
            propertiesConfig.getPropertyValue(notAKey);
        });
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
        setUpClass();
        String key = "url";
        String invalidUrlValue = "http://example.com";
        String expectedMessage = "Invalid URL for  '" + key + "': "
                + invalidUrlValue;
        propertiesConfig = PropertiesConfig.fromFile(propertiesPath, "invalid.");
        stateException = assertThrowsExactly(IllegalStateException.class, () -> {
            propertiesConfig.getPropertyValue(key);
        });
        assertEquals(expectedMessage, stateException.getMessage());
    }
}
