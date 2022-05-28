/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;

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
     * Devuelve el valor de la moneda extranjera con el ajuste
     * a la moneda nacional.
     * @param tasaCambio
     * @param valor
     * @return valorTotal - Moneda extranjera a pesos mexicanos.
     */
    public BigDecimal conversionDivisaExtranjera(double tasaCambio, double valor) {
        if (valor > 0 ) {
            BigDecimal valorMonedaNacional = new BigDecimal(valor);
            BigDecimal valorMonedaExtranjera = new BigDecimal(tasaCambio);
            BigDecimal valorTotal = new BigDecimal("0.0");
            valorTotal = valorMonedaNacional.multiply(valorMonedaExtranjera).setScale(2, RoundingMode.HALF_UP);
            JOptionPane.showMessageDialog(null, "El ajuste de $" + valor
                    + " MXN a USD es de: $" + valorTotal + " USD.");
            return valorTotal;
        } else {
            JOptionPane.showMessageDialog(null, "No es posible ingresar valores menores o igual a 0");
            return null;
        }
    }
}
