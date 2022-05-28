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
        
        PesoMexicano pesoMexicano = new PesoMexicano();
        //String valorUsuario = JOptionPane.showInputDialog("Ingrese la cantidad deseada: ");
        //pesoMexicano.conversionDivisaLocal(pesoMexicano.getTasaCambioEuros(), Double.valueOf(valorUsuario));
        //String valorUsuarioDos = JOptionPane.showInputDialog("Ingrese la cantidad deseada: ");
        //pesoMexicano.conversionDivisaExtranjera(pesoMexicano.getTasaCambioDolarAmericano(), Double.valueOf(valorUsuarioDos));
        
        /**
         * AGREGAR MENU CON COMBOBOX
         */
    }
}
