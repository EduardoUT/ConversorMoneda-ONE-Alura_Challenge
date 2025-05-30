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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * This class have two ways to handle key, value properties of a resource like a
 * database, an API, or a concrete file in the resources package, this last one
 * must be defined in the resources package and must have properties to read.
 * <p>
 * When using a file must have the .properties extention.
 *
 * @author EduardoUT
 */
public class PropertiesConfig {

    private final Properties properties;

    private PropertiesConfig(Properties properties) {
        this.properties = properties;
    }

    public static PropertiesConfig fromFile(String propertiesPath)
            throws IOException {
        if (propertiesPath == null || propertiesPath.isEmpty()) {
            throw new IllegalArgumentException("Properties path cannot be null "
                    + "or empty.");
        }

        try (InputStream input = PropertiesConfig.class.getClassLoader()
                .getResourceAsStream(propertiesPath)) {
            if (input == null) {
                throw new FileNotFoundException("Properties file not found.");
            }
            Properties props = new Properties();
            props.load(input);
            if (props.isEmpty()) {
                throw new IllegalStateException("Properties file cannot be left "
                        + "empty.");
            }
            return new PropertiesConfig(props);
        }
    }

    public List<String> getKeyProperties(String propertyKeyPrefix) {
        validatePropertyKeyPrefix(propertyKeyPrefix);
        return properties.keySet().stream()
                .filter(key -> key.toString().startsWith(propertyKeyPrefix))
                .map(key -> key.toString().replace(propertyKeyPrefix, ""))
                .collect(Collectors.toList());
    }

    public String getPropertyValue(String propertyKeyPrefix, String key) {
        validatePropertyKeyPrefix(propertyKeyPrefix);
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Property key provided is null "
                    + "or empty.");
        }
        if (!properties.containsKey(propertyKeyPrefix + key)) {
            throw new IllegalStateException("The given key " + key
                    + " does not exists on the loaded properties file or "
                    + "check prefix for " + propertyKeyPrefix);
        }
        String value = properties.getProperty(propertyKeyPrefix + key);
        if (value.matches("^https?://.+")) {
            validateUrlFormat(value, key);
        }
        return value;
    }

    private void validatePropertyKeyPrefix(String propertyKeyPrefix) {
        if (propertyKeyPrefix == null || propertyKeyPrefix.isEmpty()) {
            throw new IllegalArgumentException("The property key prefix is null or empty.");
        }

        if(!propertyKeyPrefix.endsWith(".")) {
            throw new IllegalArgumentException("The property key prefix must end with a point character" +
                    " the given was: " + propertyKeyPrefix);
        }
    }

    private void validateUrlFormat(String url, String key) {
        if (url != null && !url.matches("^https://.+")) {
            throw new IllegalStateException(
                    "Invalid URL for  '" + key + "': " + url
            );
        }
    }
}
