/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanas;

import java.math.BigDecimal;

/**
 *
 * @author mcore
 */
public interface ConversionDivisaLocal {
    /**
     * Devuelve el valor de la moneda local con el ajuste
     * a la moneda extranjera.
     * @param tasaCambio
     * @param valor
     * @return valorTotal - Pesos mexicanos a Moneda extranjera.
     */
    public abstract BigDecimal conversionDivisaLocal(double tasaCambio, double valor);
}
