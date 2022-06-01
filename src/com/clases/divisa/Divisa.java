/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clases.divisa;

import java.math.BigDecimal;

/**
 *
 * @author mcore
 */
public abstract class Divisa {

    private double tasaCambioDolarAmericano;
    private double tasaCambioEuros;
    private double tasaCambioLibrasEsterlinas;
    private double tasaCambioYenJapones;
    private double tasaCambioWonSulCoreano;
    private static final String NOMBRE_DIVISA_MEXICO = "MXN";
    private static final String NOMBRE_DIVISA_USA = "USD";
    private static final String NOMBRE_DIVISA_GRAN_BRETANA = "GBP";
    private static final String NOMBRE_DIVISA_EUROPA = "EUR";
    private static final String NOMBRE_DIVISA_YEN_JAPON = "JPY";
    private static final String NOMBRE_DIVISA_WON = "KRW";

    /**
     * @return the tasaCambioDolarAmericano
     */
    public double getTasaCambioDolarAmericano() {
        return tasaCambioDolarAmericano;
    }

    /**
     * @return the tasaCambioEuros
     */
    public double getTasaCambioEuros() {
        return tasaCambioEuros;
    }

    /**
     * @param tasaCambioEuros the tasaCambioEuros to set
     */
    public void setTasaCambioEuros(double tasaCambioEuros) {
        this.tasaCambioEuros = tasaCambioEuros;
    }

    /**
     * @return the tasaCambioLibrasEsterlinas
     */
    public double getTasaCambioLibrasEsterlinas() {
        return tasaCambioLibrasEsterlinas;
    }

    /**
     * @return the tasaCambioYenJapones
     */
    public double getTasaCambioYenJapones() {
        return tasaCambioYenJapones;
    }

    /**
     * @return the tasaCambioWonSulCoreano
     */
    public double getTasaCambioWonSurCoreano() {
        return tasaCambioWonSulCoreano;
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
     * Devuelve el valor de la moneda extranjera con el ajuste a la moneda
     * nacional.
     *
     * @param tasaCambio
     * @param valor
     * @param valorSeleccionTipoDivisa
     * @return valorTotal - Moneda extranjera a pesos mexicanos.
     */
    public abstract BigDecimal conversionDivisa(double tasaCambio, double valor, String valorSeleccionTipoDivisa);
}
