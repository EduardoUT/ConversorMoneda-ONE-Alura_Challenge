/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clases.temperatura;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;

/**
 *
 * @author mcore
 */
public class Temperatura {

    private BigDecimal escalaCelsius;
    private BigDecimal escalaFarenheit;
    private BigDecimal escalaKelvin;

    public BigDecimal convertirTemperatura(BigDecimal valorUsuario, String valorSeleccionTemperatura) {
        BigDecimal res;
        switch (valorSeleccionTemperatura) {
            case "Celcius (°C) a Farenheit (°F)":
                res = valorUsuario.multiply(new BigDecimal("1.8")).add(new BigDecimal("32"));
                mensajeResultadoConversion(valorUsuario, res, valorSeleccionTemperatura);
                return res.setScale(2, RoundingMode.FLOOR);
            case "Celcius (°C) a Kelvin (°K)":
                res = valorUsuario.add(new BigDecimal("273.15"));
                mensajeResultadoConversion(valorUsuario, res, valorSeleccionTemperatura);
                return res.setScale(2, RoundingMode.FLOOR);
            case "Farenheit (°F) a Celsius (°C)":
                res = valorUsuario.subtract(new BigDecimal("32")).divide(new BigDecimal("1.8"), 2, RoundingMode.FLOOR);
                mensajeResultadoConversion(valorUsuario, res, valorSeleccionTemperatura);
                return res.setScale(2, RoundingMode.FLOOR);
            case "Farenheit (°F) a Kelvin (°K)":
                res = valorUsuario.subtract(new BigDecimal("32")).multiply(new BigDecimal("5")).divide(
                        new BigDecimal("9"), 2, RoundingMode.FLOOR).add(new BigDecimal("273.15"));
                mensajeResultadoConversion(valorUsuario, res, valorSeleccionTemperatura);
                return res.setScale(2, RoundingMode.FLOOR);
            case "Kelvin (°K) a Celsius (°C)":
                res = valorUsuario.subtract(new BigDecimal("273.15"));
                mensajeResultadoConversion(valorUsuario, res, valorSeleccionTemperatura);
                return res.setScale(2, RoundingMode.FLOOR);
            case "Kelvin (°K) a Farenheit (°F)":
                res = valorUsuario.subtract(new BigDecimal("273.15")).multiply(
                        new BigDecimal("1.8")).add(new BigDecimal("32"));
                mensajeResultadoConversion(valorUsuario, res, valorSeleccionTemperatura);
                return res.setScale(2, RoundingMode.FLOOR);
            default:
                return null;
        }
    }

    public void mensajeResultadoConversion(BigDecimal valor, BigDecimal resultado, String valorSeleccionTemp) {
        JOptionPane.showMessageDialog(null, valor + "° " + valorSeleccionTemp
                + ": " + resultado.setScale(2, RoundingMode.FLOOR) + "°");
    }

    /**
     * @return the escalaCelsius
     */
    public BigDecimal getEscalaCelsius() {
        return escalaCelsius;
    }

    /**
     * @param escalaCelsius the escalaCelsius to set
     */
    public void setEscalaCelsius(BigDecimal escalaCelsius) {
        this.escalaCelsius = escalaCelsius;
    }

    /**
     * @return the escalaFarenheit
     */
    public BigDecimal getEscalaFarenheit() {
        return escalaFarenheit;
    }

    /**
     * @param escalaFarenheit the escalaFarenheit to set
     */
    public void setEscalaFarenheit(BigDecimal escalaFarenheit) {
        this.escalaFarenheit = escalaFarenheit;
    }

    /**
     * @return the escalaKelvin
     */
    public BigDecimal getEscalaKelvin() {
        return escalaKelvin;
    }

    /**
     * @param escalaKelvin the escalaKelvin to set
     */
    public void setEscalaKelvin(BigDecimal escalaKelvin) {
        this.escalaKelvin = escalaKelvin;

    }
}
