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
package io.github.eduardout.converter;

import static io.github.eduardout.converter.GlobalLogger.*;
import io.github.eduardout.converter.currency.ISO4217Currency;
import io.github.eduardout.converter.currency.CurrencyConverter;
import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.currency.provider.APIClient;
import io.github.eduardout.converter.currency.provider.FreeCurrencyExchangeRates;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import io.github.eduardout.converter.util.DefaultRateParser;
import io.github.eduardout.converter.util.RateParser;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;

import javax.swing.*;

/**
 *
 * @author EduardoUT
 */
public class ConverterApp {

    public static void main(String[] args) {
        try {
            GlobalLogger.setUpLoggerConfigurationFile();
            APIClient apiClient = APIClient.getInstance();
            PropertiesConfig config = PropertiesConfig.fromFile("config.properties", "fcera.");
            JSONCurrencyFileRepository repository = new JSONCurrencyFileRepository("");
            RateParser rateParser = new DefaultRateParser();
            CurrencyConverter cc = new CurrencyConverter(
                    new CurrencyUnit(ISO4217Currency.USD),
                    new CurrencyUnit(ISO4217Currency.MXN),
                    new FreeCurrencyExchangeRates(apiClient, config, repository, rateParser),
                    new BigDecimal("1.00")
            );

            System.out.println(cc.getConversion());
            System.out.println(cc.reverseConversion());


        } catch (IOException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(null, "Algo salió mal al consultar las divisas.", "Error al obtener divisas.", JOptionPane.ERROR_MESSAGE);
            registerLogException(Level.SEVERE, "Error: {0}", ex);

        }
    }
}
