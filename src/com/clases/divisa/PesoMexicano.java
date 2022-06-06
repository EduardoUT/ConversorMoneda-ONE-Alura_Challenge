/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clases.divisa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;
import com.service.ConexionApi;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contiene métodos sobrescritos con el valor de la tasa de cambio en pesos
 * mexicanos con tipos de cambio para revalorización de balance 
 * del Banco de México con precio de cierre de jornada al 31 de mayo de 2022.
 * Más info consulte:
 * https://www.banxico.org.mx/SieInternet/consultarDirectorioInternetAction.do?accion=consultarCuadroAnalitico&idCuadro=CA113&sector=6&locale=es
 * 
 * @author Eduardo Reyes Hernández
 */
public class PesoMexicano extends Divisa {

    /**
     * @return the pesoMexicanoCambioDolarAmericano
     */
    @Override
    public double getTasaCambioDolarAmericanoApi() {
        try {
            double tasaCambioDolares = super.getTasaCambioDolarAmericanoApi();
            double rateApi = ConexionApi.tasaBaseCambio(Divisa.getNOMBRE_DIVISA_USA(), Divisa.getNOMBRE_DIVISA_MEXICO());
            tasaCambioDolares += rateApi;
            return tasaCambioDolares;
        } catch (Exception ex) {
            Logger.getLogger(PesoMexicano.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.getTasaCambioDolarAmericanoFijo();
    }

    /**
     * @return the pesoMexicanoCambioEuros
     */
    @Override
    public double getTasaCambioEurosApi() {
        try {
            double tasaCambioEuros = super.getTasaCambioEurosApi();
            double rateApi = ConexionApi.tasaBaseCambio(Divisa.getNOMBRE_DIVISA_EUROPA(), Divisa.getNOMBRE_DIVISA_MEXICO());
            tasaCambioEuros += rateApi;
            return tasaCambioEuros;
        } catch (Exception ex) {
            Logger.getLogger(PesoMexicano.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.getTasaCambioEurosFijo();
    }

    /**
     * @return the pesoMexicanoCambioLibrasEsterlinas
     */
    @Override
    public double getTasaCambioLibrasEsterlinasApi() {
        try {
            double tasaCambioLibrasEsterlinas = super.getTasaCambioLibrasEsterlinasApi();
            double rateApi = ConexionApi.tasaBaseCambio(Divisa.getNOMBRE_DIVISA_GRAN_BRETANA(), Divisa.getNOMBRE_DIVISA_MEXICO());
            tasaCambioLibrasEsterlinas += rateApi;
            return tasaCambioLibrasEsterlinas;
        } catch (Exception ex) {
            Logger.getLogger(PesoMexicano.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.getTasaCambioLibrasEsterlinasFijo();
    }

    /**
     * @return the pesoMexicanoCambioYenJapones
     */
    @Override
    public double getTasaCambioYenJaponesApi() {
        try {
            double tasaCambioYenes = super.getTasaCambioYenJaponesApi();
            double rateApi = ConexionApi.tasaBaseCambio(Divisa.getNOMBRE_DIVISA_YEN_JAPON(), Divisa.getNOMBRE_DIVISA_MEXICO());
            tasaCambioYenes += rateApi;
            return tasaCambioYenes;
        } catch (Exception ex) {
            Logger.getLogger(PesoMexicano.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.getTasaCambioYenJaponesFijo();
    }

    /**
     * @return the pesoMexicanoCambioWonSulCoreano
     */
    @Override
    public double getTasaCambioWonSurCoreanoApi() {
        try {
            double tasaCambioWonSurCoreano = super.getTasaCambioWonSurCoreanoApi();
            double rateApi = ConexionApi.tasaBaseCambio(Divisa.getNOMBRE_DIVISA_WON(), Divisa.getNOMBRE_DIVISA_MEXICO());
            System.out.println(rateApi);
            tasaCambioWonSurCoreano += rateApi;
            return tasaCambioWonSurCoreano;
        } catch (Exception ex) {
            Logger.getLogger(PesoMexicano.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.getTasaCambioWonSulCoreanoFijo();
    }

    @Override
    public double getTasaCambioDolarAmericanoFijo() {
        double valorTasaFija = super.getTasaCambioDolarAmericanoFijo();
        return valorTasaFija += 19.68550;
    }

    @Override
    public double getTasaCambioEurosFijo() {
        double valorTasaFija = super.getTasaCambioDolarAmericanoFijo();
        return valorTasaFija += 21.08612;
    }

    @Override
    public double getTasaCambioLibrasEsterlinasFijo() {
        double valorTasaFija = super.getTasaCambioEurosFijo();
        return valorTasaFija += 24.79979;
    }

    @Override
    public double getTasaCambioYenJaponesFijo() {
        double valorTasaFija = super.getTasaCambioYenJaponesFijo();
        return valorTasaFija += 0.15296;
    }

    @Override
    public double getTasaCambioWonSulCoreanoFijo() {
        double valorTasaFija = super.getTasaCambioWonSulCoreanoFijo();
        return valorTasaFija += 15.91376;
    }

    public BigDecimal conversionDivisaAlternativo(double tasaCambio, double valor, String valorSeleccionTipoDivisa) {
        BigDecimal valorMonedaUsuario = new BigDecimal(String.valueOf(valor));
        BigDecimal valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
        BigDecimal valorConversion;
        boolean esConversionMonedaMexicana = valorSeleccionTipoDivisa.startsWith("Peso Mexicano (MXN)");
        if (esConversionMonedaMexicana) {
            if (this.equals(this) && valor > 0) {
                valorConversion = valorMonedaUsuario.divide(valorMonedaTasaCambio, 2, RoundingMode.FLOOR);
                JOptionPane.showMessageDialog(null, "El ajuste de $" + valorMonedaUsuario.setScale(2, RoundingMode.FLOOR) + " " + valorSeleccionTipoDivisa + " es de: $" + valorConversion);
                return valorConversion;
            } else {
                JOptionPane.showMessageDialog(null, "Posibles errores: "
                        + "\n 1. No es posible ingresar valores menores o igual a 0. "
                        + "\n 2. No es posible hacer conversión de monedas extranjeras en esta función.");
                return null;
            }
        } else {
            if (valor > 0) {
                valorConversion = valorMonedaUsuario.multiply(valorMonedaTasaCambio).setScale(2, RoundingMode.FLOOR);
                JOptionPane.showMessageDialog(null, "El ajuste de $" + valorMonedaUsuario.setScale(2, RoundingMode.FLOOR) + " " + valorSeleccionTipoDivisa + " es de: $" + valorConversion);
                return valorConversion;
            } else {
                JOptionPane.showMessageDialog(null, "No es posible ingresar valores menores o igual a 0");
                return null;
            }
        }
    }

    @Override
    public String conversionDivisa(double valor, String valorSeleccionTipoDivisa) {
        double tasaCambio;
        BigDecimal valorMonedaUsuario = new BigDecimal(String.valueOf(valor));
        BigDecimal valorMonedaTasaCambio;
        BigDecimal valorConversion;
        if (valor > 0 && this.equals(this)) {

            switch (valorSeleccionTipoDivisa) {
                case "Peso Mexicano (MXN) a Dólar Américano (USD)":
                    tasaCambio = this.getTasaCambioDolarAmericanoApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaLocal(valorMonedaUsuario, valorMonedaTasaCambio);
                    return Divisa.getSIMBOLO_DOLAR() + " " + valorConversion;
                case "Peso Mexicano (MXN) a Euro (EUR)":
                    tasaCambio = this.getTasaCambioEurosApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaLocal(valorMonedaUsuario, valorMonedaTasaCambio);
                    return Divisa.getSIMBOLO_EURO() + " " + valorConversion;
                case "Peso Mexicano (MXN) a Libra Esterlina (GBP)":
                    tasaCambio = this.getTasaCambioLibrasEsterlinasApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaLocal(valorMonedaUsuario, valorMonedaTasaCambio);
                    return Divisa.getSIMBOLO_LIBRA_ESTERLINA() + " " + valorConversion;
                case "Peso Mexicano (MXN) a Yen (JPY)":
                    tasaCambio = this.getTasaCambioYenJaponesApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaLocal(valorMonedaUsuario, valorMonedaTasaCambio);
                    return Divisa.getSIMBOLO_YEN() + " " + valorConversion;
                case "Peso Mexicano (MXN) a Won Coreano (KRW)":
                    tasaCambio = this.getTasaCambioWonSurCoreanoApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaLocal(valorMonedaUsuario, valorMonedaTasaCambio);
                    return Divisa.getSIMBOLO_WON_COREANO() + " " + valorConversion;
                case "Dólar Americano (USD) a Peso Mexicano (MXN)":
                    tasaCambio = this.getTasaCambioDolarAmericanoApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaExtranjera(valorMonedaUsuario, valorMonedaTasaCambio);
                    return "$ " + valorConversion;
                case "Euro (EUR) a Peso Mexicano (MXN)":
                    tasaCambio = this.getTasaCambioEurosApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaExtranjera(valorMonedaUsuario, valorMonedaTasaCambio);
                    return "$ " + valorConversion;
                case "Libra Exterlina (GBP) a Peso Mexicano (MXN)":
                    tasaCambio = this.getTasaCambioLibrasEsterlinasApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaExtranjera(valorMonedaUsuario, valorMonedaTasaCambio);
                    return "$ " + valorConversion;
                case "Yen (JPY) a Peso Mexicano (MXN)":
                    tasaCambio = this.getTasaCambioYenJaponesApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaExtranjera(valorMonedaUsuario, valorMonedaTasaCambio);
                    return "$ " + valorConversion;
                case "Won Coreano (KRW) a Peso Mexicano (MXN)":
                    tasaCambio = this.getTasaCambioWonSurCoreanoApi();
                    valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
                    valorConversion = super.operacionConMonedaExtranjera(valorMonedaUsuario, valorMonedaTasaCambio);
                    return "$ " + valorConversion;
                default:
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Posibles errores: "
                    + "\n 1. No es posible ingresar valores menores o igual a 0. "
                    + "\n 2. No es posible hacer conversión de monedas extranjeras en esta función.");
            return null;
        }
        return null;
    }
}
