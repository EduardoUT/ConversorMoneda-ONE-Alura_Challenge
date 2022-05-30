/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanas;

import com.clases.divisa.PesoMexicano;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jdk.nashorn.internal.runtime.JSType;

/**
 *
 * @author mcore
 */
public class TestDivisa {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        //Configurando apariencia de las ventanas emergentes.
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("OptionPane.background", Color.DARK_GRAY);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Panel.background", Color.DARK_GRAY);
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("ComboBox.background", Color.DARK_GRAY);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(126, 193, 228));

        //Instanciando la clase que contiene todos los métodos para conversión.
        PesoMexicano pesoMexicano = new PesoMexicano();

        //Creando ventana menú con ComboBox de dos opciones.
        Object seleccionTipoConversion = JOptionPane.showInputDialog(
                null,
                "Seleccione una opción de conversión: ",
                "Menú",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{
                    "Conversor de Moneda",
                    "Conversor de Temperatura"
                },
                "Conversor de Moneda" //Opción seleccionada por defecto.
        );

        //Si el usuario no presiona Cancel o el botón cerrar "X".
        if (seleccionTipoConversion != null) {
            System.out.println("El usuario ha elegido " + seleccionTipoConversion);
            if (esConversorMoneda(seleccionTipoConversion)) {
                cantidadDivisaUsuario();
            } else {

            }
        } else {
            System.exit(0);
        }

        //String valorUsuario = JOptionPane.showInputDialog("Ingrese la cantidad deseada: ");
        //pesoMexicano.conversionDivisaLocal(pesoMexicano.getTasaCambioEuros(), Double.valueOf(valorUsuario));
        //String valorUsuarioDos = JOptionPane.showInputDialog("Ingrese la cantidad deseada: ");
        //pesoMexicano.conversionDivisaExtranjera(pesoMexicano.getTasaCambioDolarAmericano(), Double.valueOf(valorUsuarioDos));
    }

    /**
     * Al ser sólo dos items del menú comprobamos sólo si es o no la opción
     * Conversor de Moneda.
     *
     * @param seleccionUsuario
     * @return boolean - Devuelve valor boleano.
     */
    public static boolean esConversorMoneda(Object seleccionUsuario) {
        return seleccionUsuario.equals("Conversor de Moneda");
    }

    /**
     * Comprobando que el valor ingresado en el inputMessage sea numerico
     * decimal o entero.
     *
     * @param inputUsuario
     * @return
     */
    public static boolean esNumerico(String inputUsuario) {
        return JSType.isNumber(Double.valueOf(inputUsuario));
    }

    /**
     * Llama a la ventana de ingreso de valor a convertir en divisa.
     */
    public static void cantidadDivisaUsuario() {
        String valorUsuario = JOptionPane.showInputDialog(
                null,
                "Ingresa la cantidad de dinero que deseas convertir: ",
                "Cantidad a Convertir",
                JOptionPane.QUESTION_MESSAGE
        );

        if (valorUsuario != null) {
            try {
                esNumerico(valorUsuario);
                Object seleccionTipoDivisa = JOptionPane.showInputDialog(
                        null,
                        "Elije la moneda a la que desea convertir tu dinero: ",
                        "Tipo de Moneda",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[] {
                            "Peso Mexicano (MXN) a Dólar Américano (USD)",
                            "Peso Mexicano (MXN) a Euro (EUR)",
                            "Peso Mexicano (MXN) a Libra Esterlina (GBP)",
                            "Peso Mexicano (MXN) a Yen (JPY)",
                            "Peso Mexicano (MXN) a Won Coreano (KRW)",
                            "Dólar Americano (USD) a Peso Mexicano (MXN)",
                            "Euro (EUR) a Peso Mexicano (MXN)",
                            "Libra Exterlina (GBP) a Peso Mexicano (MXN)",
                            "Yen (JPY) a Peso Mexicano (MXN)",
                            "Won Coreano (KRW) a Peso Mexicano (MXN)"
                        },
                        "Peso Mexicano (MXN) a Euro (EUR)"
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El valor ingresado no es un número "
                        + "entero o decimal");
                /**
                 * Con recursividad volvemos a pedir el valor llamando la
                 * ventana si no es numérico.
                 */
                cantidadDivisaUsuario();
            }
        } else {
            System.exit(0);
        }

    }
}
