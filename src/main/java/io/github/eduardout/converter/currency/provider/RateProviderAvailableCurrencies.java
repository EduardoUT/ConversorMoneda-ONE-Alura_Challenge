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
package io.github.eduardout.converter.currency.provider;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author EduardoUT
 */
public interface RateProviderAvailableCurrencies {

    /**
     * If the Rate Provider has an endpoint of the currencies, use this
     * implementation to list.
     *
     * @return A list of the available ISO code currencies of the API.
     */
    Optional<List<String>> getCurrencies();
}
