/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clases.divisa;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 
 * @author Eduardo Reyes Hernández
 */
public abstract class Divisa {

    private double tasaCambioDolarAmericanoFijo;
    private double tasaCambioEurosFijo;
    private double tasaCambioLibrasEsterlinasFijo;
    private double tasaCambioYenJaponesFijo;
    private double tasaCambioWonSulCoreanoFijo;
    private double tasaCambioDolarAmericanoApi;
    private double tasaCambioEurosApi;
    private double tasaCambioLibrasEsterlinasApi;
    private double tasaCambioYenJaponesApi;
    private double tasaCambioWonSulCoreanoApi;
    private static final String NOMBRE_DIVISA_MEXICO = "MXN";
    private static final String NOMBRE_DIVISA_USA = "USD";
    private static final String NOMBRE_DIVISA_GRAN_BRETANA = "GBP";
    private static final String NOMBRE_DIVISA_EUROPA = "EUR";
    private static final String NOMBRE_DIVISA_YEN_JAPON = "JPY";
    private static final String NOMBRE_DIVISA_WON = "KRW";
    private static final String SIMBOLO_DOLAR = "US$";
    private static final String SIMBOLO_EURO = "€";
    private static final String SIMBOLO_LIBRA_ESTERLINA = "£";
    private static final String SIMBOLO_YEN = "¥";
    private static final String SIMBOLO_WON_COREANO = "₩";

    /**
     * @return the tasaCambioDolarAmericano
     */
    public double getTasaCambioDolarAmericanoApi() {
        return tasaCambioDolarAmericanoApi;
    }

    /**
     * @return the tasaCambioEuros
     */
    public double getTasaCambioEurosApi() {
        return tasaCambioEurosApi;
    }

    /**
     * @param tasaCambioEuros the tasaCambioEuros to set
     */
    public void setTasaCambioEurosApi(double tasaCambioEuros) {
        this.tasaCambioEurosApi = tasaCambioEuros;
    }

    /**
     * @return the tasaCambioLibrasEsterlinas
     */
    public double getTasaCambioLibrasEsterlinasApi() {
        return tasaCambioLibrasEsterlinasApi;
    }

    /**
     * @return the tasaCambioYenJapones
     */
    public double getTasaCambioYenJaponesApi() {
        return tasaCambioYenJaponesApi;
    }

    /**
     * @return the tasaCambioWonSulCoreano
     */
    public double getTasaCambioWonSurCoreanoApi() {
        return tasaCambioWonSulCoreanoApi;
    }

    /**
     * @return the tasaCambioDolarAmericanoFijo
     */
    public double getTasaCambioDolarAmericanoFijo() {
        return tasaCambioDolarAmericanoFijo;
    }

    /**
     * @return the tasaCambioEurosFijo
     */
    public double getTasaCambioEurosFijo() {
        return tasaCambioEurosFijo;
    }

    /**
     * @return the tasaCambioLibrasEsterlinasFijo
     */
    public double getTasaCambioLibrasEsterlinasFijo() {
        return tasaCambioLibrasEsterlinasFijo;
    }

    /**
     * @return the tasaCambioYenJaponesFijo
     */
    public double getTasaCambioYenJaponesFijo() {
        return tasaCambioYenJaponesFijo;
    }

    /**
     * @return the tasaCambioWonSulCoreanoFijo
     */
    public double getTasaCambioWonSulCoreanoFijo() {
        return tasaCambioWonSulCoreanoFijo;
    }

    /**
     * @return the NOMBRE_DIVISA_MEXICO
     */
    public static String getNOMBRE_DIVISA_MEXICO() {
        return NOMBRE_DIVISA_MEXICO;
    }

    /**
     * @return the NOMBRE_DIVISA_USA
     */
    public static String getNOMBRE_DIVISA_USA() {
        return NOMBRE_DIVISA_USA;
    }

    /**
     * @return the NOMBRE_DIVISA_EUROPA
     */
    public static String getNOMBRE_DIVISA_EUROPA() {
        return NOMBRE_DIVISA_EUROPA;
    }

    /**
     * @return the NOMBRE_DIVISA_GRAN_BRETANA
     */
    public static String getNOMBRE_DIVISA_GRAN_BRETANA() {
        return NOMBRE_DIVISA_GRAN_BRETANA;
    }

    /**
     * @return the NOMBRE_DIVISA_YEN_JAPON
     */
    public static String getNOMBRE_DIVISA_YEN_JAPON() {
        return NOMBRE_DIVISA_YEN_JAPON;
    }

    /**
     * @return the NOMBRE_DIVISA_WON
     */
    public static String getNOMBRE_DIVISA_WON() {
        return NOMBRE_DIVISA_WON;
    }

    /**
     * @return the SIMBOLO_DOLAR
     */
    public static String getSIMBOLO_DOLAR() {
        return SIMBOLO_DOLAR;
    }

    /**
     * @return the SIMBOLO_EURO
     */
    public static String getSIMBOLO_EURO() {
        return SIMBOLO_EURO;
    }

    /**
     * @return the SIMBOLO_LIBRA_ESTERLINA
     */
    public static String getSIMBOLO_LIBRA_ESTERLINA() {
        return SIMBOLO_LIBRA_ESTERLINA;
    }

    /**
     * @return the SIMBOLO_YEN
     */
    public static String getSIMBOLO_YEN() {
        return SIMBOLO_YEN;
    }

    /**
     * @return the SIMBOLO_WON_COREANO
     */
    public static String getSIMBOLO_WON_COREANO() {
        return SIMBOLO_WON_COREANO;
    }

    /**
     * Devuelve el valor de la moneda extranjera con el ajuste a la moneda
     * nacional.
     *
     * @param valor
     * @param valorSeleccionTipoDivisa
     * @return valorTotal - Moneda extranjera a pesos mexicanos.
     */
    public abstract String conversionDivisa(double valor, String valorSeleccionTipoDivisa);

    public BigDecimal operacionConMonedaLocal(BigDecimal valorMonedaUsuario, BigDecimal valorMonedaTasaCambio) {
        return valorMonedaUsuario.divide(valorMonedaTasaCambio, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal operacionConMonedaExtranjera(BigDecimal valorMonedaUsuario, BigDecimal valorMonedaTasaCambio) {
        return valorMonedaUsuario.multiply(valorMonedaTasaCambio).setScale(2, RoundingMode.HALF_UP);
    }
}
