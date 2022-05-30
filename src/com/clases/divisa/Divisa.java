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
     * Devuelve el valor de la moneda extranjera con el ajuste a la moneda
     * nacional.
     *
     * @param tasaCambio
     * @param valor
     * @return valorTotal - Moneda extranjera a pesos mexicanos.
     */
    public abstract BigDecimal conversionDivisa(double tasaCambio, double valor, String valorSeleccionTipoDivisa);
}
