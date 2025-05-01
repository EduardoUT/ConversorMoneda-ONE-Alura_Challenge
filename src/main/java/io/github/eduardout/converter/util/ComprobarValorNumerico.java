/*
 * Copyright (C) 2025 EduardoUT
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.eduardout.converter.util;

import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.JSType;

/**
 * 
 * @author EduardoUT
 */
public class ComprobarValorNumerico {

    private ComprobarValorNumerico() {
        throw new UnsupportedOperationException("Utility class.");
    }

    /**
     * Comprobando que el valor ingresado en el inputMessage sea numerico
     * decimal o entero.
     *
     * @param cantidadDivisaUsuario
     * @return
     */
    public static boolean esNumerico(double cantidadDivisaUsuario) {
        return JSType.isNumber(cantidadDivisaUsuario);
    }
    
    public static boolean esValorDecimal(String valor) {
        String regex = "^\\d*\\.?\\d+$";
        Pattern pt = Pattern.compile(regex);
        java.util.regex.Matcher mat = pt.matcher(valor);
        return mat.find();
    }
    
    public static boolean esValorDecimalOpcionalNegativo(String valor) {
        String regex = "^-?\\d*\\.?\\d+$";
        Pattern pt = Pattern.compile(regex);
        java.util.regex.Matcher mat = pt.matcher(valor);
        return mat.find();
    }
}
