/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanas;

import java.util.regex.Pattern;

/**
 *
 * @author mcore
 */
public class RegexNumerosDecimales {

    public static boolean esValorDecimal(String valor) {
        String regex = "^([\\d]*(?:[\\.]?[\\d]+)+)$";
        Pattern pt = Pattern.compile(regex);
        java.util.regex.Matcher mat = pt.matcher(valor);
        return mat.find();
    }
}
