/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clases.divisa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;

/**
 * Contiene métodos sobrescritos con el valor de la tasa de cambio en pesos
 * mexicanos respecto a otras monedas acorde al resumen de cierre del mes 29 de
 * abril de 2022 en:
 * https://www.banxico.org.mx/SieInternet/consultarDirectorioInternetAction.do?accion=consultarCuadroAnalitico&idCuadro=CA113&sector=6&locale=es
 * del Banco de México.
 *
 * @author mcore
 */
public class PesoMexicano extends Divisa {

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

    @Override
    public BigDecimal conversionDivisa(double tasaCambio, double valor, String valorSeleccionTipoDivisa) {
        BigDecimal valorMonedaUsuario = new BigDecimal(String.valueOf(valor));
        BigDecimal valorMonedaTasaCambio = new BigDecimal(String.valueOf(tasaCambio));
        BigDecimal valorConversion;
        boolean esConversionMonedaMexicana = valorSeleccionTipoDivisa.startsWith("Peso Mexicano (MXN)");
        if (esConversionMonedaMexicana) {
            if (this.equals(this) && valor > 0) {
                valorConversion = valorMonedaUsuario.divide(valorMonedaTasaCambio, 2, RoundingMode.HALF_UP);
                JOptionPane.showMessageDialog(null, "El ajuste de $" + valorMonedaUsuario.setScale(2, RoundingMode.HALF_UP) + " " + valorSeleccionTipoDivisa + " es de: $" + valorConversion);
                return valorConversion;
            } else {
                JOptionPane.showMessageDialog(null, "Posibles errores: "
                        + "\n 1. No es posible ingresar valores menores o igual a 0. "
                        + "\n 2. No es posible hacer conversión de monedas extranjeras en esta función.");
                return null;
            }
        } else {
            if (valor > 0) {
                valorConversion = valorMonedaUsuario.multiply(valorMonedaTasaCambio).setScale(2, RoundingMode.HALF_UP);
                JOptionPane.showMessageDialog(null, "El ajuste de $" + valorMonedaUsuario.setScale(2, RoundingMode.HALF_UP) + " " + valorSeleccionTipoDivisa + " es de: $" + valorConversion);
                return valorConversion;
            } else {
                JOptionPane.showMessageDialog(null, "No es posible ingresar valores menores o igual a 0");
                return null;
            }
        }

    }
}
