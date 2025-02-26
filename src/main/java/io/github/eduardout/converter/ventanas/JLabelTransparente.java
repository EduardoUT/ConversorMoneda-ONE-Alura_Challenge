package com.ventanas;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Para que la imágen de fondo pueda visualizarse en el jLabel, click derecho en
 * el jLabel -> Customize Code... -> Custom Creation -> Escribir una nueva
 * instancia a esta clase p.ej: jLabel1 = new JLabelTransparente(); Puede
 * aplicarse a cualquier jLabel para hacer transparente.
 * 
 * @author Eduardo Reyes Hernández
 */
public class JLabelTransparente extends JLabel {

    private Image imagen;

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        imagen = new ImageIcon(getClass().getResource("/Imagenes/Background2.png")).getImage();
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        setOpaque(false);
        super.paint(g);
    }
}
