/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanas;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
                
            } else {

            }
        } else {
            System.exit(0);
        }

        PesoMexicano pesoMexicano = new PesoMexicano();
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
}
