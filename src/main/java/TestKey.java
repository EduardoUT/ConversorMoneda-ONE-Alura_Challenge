
import static io.github.eduardout.converter.GlobalLogger.*;

import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.currency.provider.APIClient;
import io.github.eduardout.converter.currency.provider.NBPExchangeRates;
import io.github.eduardout.converter.util.NBPExchangeRatesParser;
import io.github.eduardout.converter.util.RateParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.logging.Level;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author EduardoUT
 */
public class TestKey {

    public static void main(String[] args) {/*
        try {
            APIClient aPIClient = APIClient.getInstance();
            PropertiesConfig propertiesConfig = PropertiesConfig.fromFile("config.properties", "nbp.");
            RateParser rateParser = new NBPExchangeRatesParser();
            NBPExchangeRates nbp = new NBPExchangeRates(aPIClient, propertiesConfig, rateParser);
            //System.out.println(nbp.getCurrencyRates(new CurrencyUnit(ISO4217Currency.USD), new CurrencyUnit(ISO4217Currency.EUR)));
            System.out.println(nbp.getCurrencies().get().size());
        } catch (IOException e) {
            registerLogException(Level.SEVERE, "Error al procesar el archivo properties.", e);
        }*/
        System.out.println(new BigDecimal(1));
    }

}
