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
package io.github.eduardout.converter.currency;

import io.github.eduardout.converter.view.ConverterUI;

/**
 *
 * @author EduardoUT
 */
public class CurrencyController {

    private CurrencyService currencyService;
    private CurrencyConverter currencyConverter;
    private ConverterUI converterUI;

    public CurrencyController(CurrencyConverter currencyConverter) {
        currencyService = new CurrencyService(currencyConverter);
    }

}
