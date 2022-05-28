/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ventanas;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author mcore
 */
public class ConversorMoneda {

    /**
     * @param args the command line arguments
     * @throws javax.swing.UnsupportedLookAndFeelException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //Configurando estilos de las ventanas emergentes.
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("OptionPane.background", Color.DARK_GRAY);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Panel.background", Color.DARK_GRAY);
        UIManager.put("Button.background", Color.DARK_GRAY);

        //JOptionPane.showMessageDialog(null, "Hola");
        double dolarAmericanoCambioPesoMexicano = 20.34530;
        double euroCambioPesoMexicano = 21.2145;
        double librasExterlinasCambioPesoMexicano = 25.54861;
        double yenJaponesCambioPesoMexicano = 0.15724;
        double wonSulCoreanoCambioPesoMexicano = 16.19689;
        //System.out.println(String.valueOf(pesoMexicano));
        //System.out.println(valorExacto);
        BigDecimal conversion = conversionPesoMexicano(dolarAmericanoCambioPesoMexicano, 40.77);
        System.out.println(conversion);
        conversion = conversionMonedaExtranjera(dolarAmericanoCambioPesoMexicano, 2);
        System.out.println(conversion);
        conversion = conversionPesoMexicano(librasExterlinasCambioPesoMexicano, 1);
        System.out.println(conversion);
    }

    /**
     * En la clase padre definir...
     *
     * Atributos:
     *
     * private double valorMoneda;
     *
     * public Divisa() { this.valorMoneda = 0.05061; }
     *
     * Getters y Setters
     *
     * public double getValorMoneda() { return valorMoneda; }
     *
     * public abstract void setValorMoneda(double valorMoneda);
     *
     * Método abstracto para convertir de la moneda nacional a extranjera y
     * visceversa public abstract BigDecimal
     * hacerConversionMonedaNacional(double moneda, Dolar dolar);
     *
     *
     * En implementación... public abstract BigDecimal
     * hacerConversionMonedaNacional(Divisa pesoMexicano, Divisa dolar) { if
     * (pesoMexicano.getValorMoneda() > 0) { BigDecimal valorMonedaNacional =
     * new BigDecimal(String.valueOf(pesoMexicano.getValorMoneda())); BigDecimal
     * valorMonedaExtranjera = new
     * BigDecimal(String.valueOf(this.getValorMoneda())); BigDecimal valorTotal
     * = new BigDecimal("0.0"); valorTotal =
     * valorMonedaNacional.multiply(valorMonedaExtranjera).setScale(2,
     * RoundingMode.HALF_UP); return valorTotal; } else {
     * JOptionPane.showMessageDialog(null, "No es posible ingresar valores
     * menores o igual a 0"); return null; } }
     *
     * @param dolar
     * @param pesoMexicanoTasaDeCambio
     * @return
     */
    public static BigDecimal conversionPesoMexicano(double pesoMexicanoTasaDeCambio, double monedaExtranjera) {
        if (monedaExtranjera > 0) {
            BigDecimal valorMXN = new BigDecimal(String.valueOf(pesoMexicanoTasaDeCambio));
            BigDecimal valorMonedaExtranjera = new BigDecimal(String.valueOf(monedaExtranjera));
            return valorMonedaExtranjera.divide(valorMXN, 2, RoundingMode.HALF_UP);
        } else {
            JOptionPane.showMessageDialog(null, "No es posible ingresar valores menores o igual a 0");
            return null;
        }
    }

    public static BigDecimal conversionMonedaExtranjera(double monedaExtranjera, double pesoMexicano) {
        if (monedaExtranjera > 0) {
            BigDecimal valorMXN = new BigDecimal(String.valueOf(pesoMexicano));
            BigDecimal valorUSD = new BigDecimal(String.valueOf(monedaExtranjera));
            return valorMXN.multiply(valorUSD).setScale(2, RoundingMode.HALF_UP);
        } else {
            JOptionPane.showMessageDialog(null, "No es posible ingresar valores menores o igual a 0");
            return null;
        }
    }
}
