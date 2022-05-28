/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;

/**
 * Contiene métodos sobrescritos con el valor
 * de la tasa de cambio en pesos mexicanos respecto
 * a otras monedas acorde al resumen de cierre del mes 29 de abril de 2022
 * en:
 * https://www.banxico.org.mx/SieInternet/consultarDirectorioInternetAction.do?accion=consultarCuadroAnalitico&idCuadro=CA113&sector=6&locale=es
 * del Banco de México.
 * @author mcore
 */
public class PesoMexicano extends Divisa implements ConversionDivisaLocal {

    @Override
    public BigDecimal conversionDivisaLocal(double tasaCambio, double valor) {
        if (this.equals(this) && valor > 0) {
            BigDecimal valorMonedaNacional = new BigDecimal(String.valueOf(tasaCambio));
            BigDecimal valorMonedaExtranjera = new BigDecimal(String.valueOf(valor));
            BigDecimal valorTotal = new BigDecimal("0.0");
            valorTotal = valorMonedaExtranjera.divide(valorMonedaNacional, 2, RoundingMode.HALF_UP);
            JOptionPane.showMessageDialog(null, "El ajuste de $" + valor
                    + "MXN a USD es de: $" + valorTotal + " USD."); //Tomar MXN a USD del valor ComboBox seleccionado.
            return valorTotal;
        } else {
            JOptionPane.showMessageDialog(null, "Posibles errores: "
                    + "\n 1. No es posible ingresar valores menores o igual a 0. "
                    + "\n 2. No es posible hacer conversión de monedas extranjeras en esta función.");
            return null;
        }
    }

    @Override
    public BigDecimal conversionDivisaExtranjera(double tasaCambio, double valor) {
        return super.conversionDivisaExtranjera(tasaCambio, valor); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    /**
     * @return the pesoMexicanoCambioDolarAmericano
     */
    @Override
    public double getTasaCambioDolarAmericano() {
        double tasaCambioDolares = super.getTasaCambioDolarAmericano();
        return tasaCambioDolares += 20.34530;
    }

    /**
     * @return the pesoMexicanoCambioEuros
     */
    @Override
    public double getTasaCambioEuros() {
        double tasaCambioEuros = super.getTasaCambioEuros();
        return tasaCambioEuros += 21.2145;
    }

    /**
     * @return the pesoMexicanoCambioLibrasEsterlinas
     */
    @Override
    public double getTasaCambioLibrasEsterlinas() {
        double tasaCambioLibras = super.getTasaCambioLibrasEsterlinas();
        return tasaCambioLibras += 25.54861;
    }

    /**
     * @return the pesoMexicanoCambioYenJapones
     */
    @Override
    public double getTasaCambioYenJapones() {
        double tasaCambioYen = super.getTasaCambioYenJapones();
        return tasaCambioYen += 0.15724;
    }

    /**
     * @return the pesoMexicanoCambioWonSulCoreano
     */
    @Override
    public double getTasaCambioWonSurCoreano() {
        double tasaCambioWon = super.getTasaCambioWonSurCoreano();
        return tasaCambioWon += 16.19689;
    }
}
