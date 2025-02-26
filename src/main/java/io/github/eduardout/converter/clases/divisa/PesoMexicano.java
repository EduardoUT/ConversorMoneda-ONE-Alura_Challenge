/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clases.divisa;

import com.service.ProveedorAPI;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Posee métodos que permiten realizar la conversión del peso mexicano con las
 * siguientes divisas: Dólar Americano, Euro, Libra Esterlina, Yuan Japones y
 * Won Sur Coreano.
 *
 * Los cálculos de conversión se realizan tomando en cuenta la descripción
 * seleccionada por el usuario en el view.
 *
 * El valor del peso mexicano respecto a otras divisas es consultado a través de
 * las siguientes APIs: ExchangeRatesAPI(Valor en tiempo real) y SIE API del
 * Banco de México(Valor de cierre de jornada del último día de cada mes).
 *
 * @author Eduardo Reyes Hernández
 */
public class PesoMexicano implements IClavesDivisas {

    private String claveDivisa;
    private BigDecimal importe;

    public PesoMexicano(BigDecimal importe) {
        if (importe == null) {
            throw new NullPointerException("Valor nulo.");
        }

        if (esImporteInvalido(importe)) {
            throw new IllegalArgumentException("No puede ser menor o igual a cero.");
        }
        this.claveDivisa = CLAVE_DIVISA_MEXICO;
        this.importe = importe;
    }

    /**
     * @return the claveDivisa
     */
    public String getClaveDivisa() {
        return claveDivisa;
    }

    /**
     * @return the importe
     */
    public BigDecimal getImporte() {
        return importe;
    }

    private boolean esImporteInvalido(BigDecimal importe) {
        if (importe.signum() == -1) {
            return true;
        } else if (importe.signum() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Realiza la operación de división entre el importe del usuario y el
     * importe equivalente cuando el peso mexicano es la divisa base.
     *
     * Por ejemplo:
     *
     * Importe Usuario | Importe Equivalente del Peso Mexicano con una Libra.
     *
     * 50 / 23.44668 = 2.13
     *
     * @param importeUsuario Importe ingresado por el usuario através del
     * constructor.
     * @param importeEquivalente Importe del peso mexicano con una divisa
     * extranjera obtenido de la API.
     * @return El importe final de la conversión.
     */
    private BigDecimal calcularPrecioMonedaLocal(BigDecimal importeUsuario, BigDecimal importeEquivalente) {
        return importeUsuario.divide(importeEquivalente, 2, RoundingMode.FLOOR);
    }

    /**
     * Realiza la operación de multiplicación entre el importe del usuario y el
     * importe equivalente cuando la divisa extranjera es la divisa base.
     *
     * Por ejemplo:
     *
     * Importe Usuario | Importe Equivalente del Peso Mexicano con una Libra.
     *
     * 50 * 23.44668 = 1172.33
     *
     * @param importeUsuario Importe ingresado por el usuario através del
     * constructor.
     * @param importeEquivalente Importe del peso mexicano con una divisa
     * extranjera obtenido de la API.
     * @return El importe final de la conversión.
     */
    private BigDecimal calcularPrecioMonedaExtranjera(BigDecimal importeUsuario, BigDecimal importeEquivalente) {
        return importeUsuario.multiply(importeEquivalente).setScale(2, RoundingMode.FLOOR);
    }

    /**
     * Obtiene el importe del peso mexicano como divisa base con la divisa
     * extranjera en tiempo real, aunque si la conexión falla o hubo un exceso
     * de peticiones a ExchangeRates API, se obtendrá entonces de la API del
     * Banco de México con un valor menos preciso y de ajuste cambiario mensual.
     *
     * Por ejemplo: 1 GBP -> 23.44668 MXN
     *
     * @param claveCambio Clave de la divisa a convertir.
     * @return Importe equivalente del peso mexicano con la divisa extranjera.
     */
    private BigDecimal getEquivalencia(String claveCambio) {
        return ProveedorAPI.valorDivisaExchangeRatesAPI(this.getClaveDivisa(), claveCambio);
    }

    /**
     *
     * @param descripcionConversion Es el valor obtenido del selector de
     * opciones swing.
     * @return La clave de la divisa de la descripción.
     */
    private String extraerClave(String descripcionConversion) {
        if (descripcionConversion.contains(CLAVE_DIVISA_USA)) {
            descripcionConversion = CLAVE_DIVISA_USA;
        } else if (descripcionConversion.contains(CLAVE_DIVISA_GRAN_BRETANA)) {
            descripcionConversion = CLAVE_DIVISA_GRAN_BRETANA;
        } else if (descripcionConversion.contains(CLAVE_DIVISA_EUROPA)) {
            descripcionConversion = CLAVE_DIVISA_EUROPA;
        } else if (descripcionConversion.contains(CLAVE_DIVISA_WON)) {
            descripcionConversion = CLAVE_DIVISA_WON;
        } else if (descripcionConversion.contains(CLAVE_DIVISA_YEN_JAPON)) {
            descripcionConversion = CLAVE_DIVISA_YEN_JAPON;
        }
        return descripcionConversion;
    }

    /**
     * Convierte al peso mexicano a otras divisas y viceversa.
     *
     * @param descripcionConversion Es el valor obtenido del selector de
     * opciones swing.
     * @return El importe final de la conversión entre la divisa base MXN y la
     * divisa extranjera GBP, USD, EUR, JPY y KRW.
     */
    public BigDecimal hacerConversion(String descripcionConversion) {
        BigDecimal resultado;
        String clave = extraerClave(descripcionConversion);
        if (descripcionConversion.startsWith("Peso Mexicano")) {
            resultado = calcularPrecioMonedaLocal(this.getImporte(),
                    this.getEquivalencia(clave));
        } else {
            resultado = calcularPrecioMonedaExtranjera(getImporte(),
                    this.getEquivalencia(clave));
        }
        return resultado;
    }
}
