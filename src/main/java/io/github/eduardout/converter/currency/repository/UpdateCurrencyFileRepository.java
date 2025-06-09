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
package io.github.eduardout.converter.currency.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author EduardoUT
 */
public interface UpdateCurrencyFileRepository {

    /**
     * Updates the latest available currencies from the API  provider
     * in a local JSON file, in case there is no available network
     * connection.
     *
     * @param currencies A Map with the currency code and the equivalent monetary amount to store.
     * @throws java.io.IOException Yhrows when handling the local repository file.
     */
    void updateCurrencyRates(Map<String, BigDecimal> currencies) throws IOException;
}
