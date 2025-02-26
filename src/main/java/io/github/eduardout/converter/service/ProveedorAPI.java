/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service;

import java.io.*;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import org.json.JSONObject;
import com.clases.divisa.IClavesDivisas;
import com.util.SeriesID;
import com.util.Recurso;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

/**
 *
 * @author Eduardo Reyes Hernández
 */
public class ProveedorAPI implements IClavesDivisas, IKeys {

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final Recurso recursos = new Recurso();

    /**
     * Recibe como parámetros el nombre de la divisa, ej (MXN) como tasaBase y
     * el segundo parámetro la tasa extranjera (tasaCambio).
     *
     * Ejemplo: 1 euro equivale a 23.33 pesos mexicanos.
     *
     * El valor devuelto será el más
     *
     * @param serieBase Código de serie correspondiente a la divisa local.
     * @param serieCambio Código de serie correspondiente a cualquier divisa
     * extranjera.
     * @return Importe equivalente del peso mexicano con una divisa extranjera.
     *
     * ayer.com/exchangerates_data/latest?symbols=" + serieBase + "&base=" +
     * serieCambio + ""
     */
    public static BigDecimal valorDivisaExchangeRatesAPI(String serieBase, String serieCambio) {
        if (!serieBase.equals(CLAVE_DIVISA_MEXICO)) {
            throw new IllegalArgumentException("La serie base debe ser MXN.");
        }
        Request request = new Request.Builder()
                .url("https://api.apilayer.com/exchangerates_data/latest?symbols="
                        + serieBase + "&base=" + serieCambio + "")
                .header("apikey", recursos.obtenerRecurso("b8PTBmsFUWlC/2Owx56C"
                        + "cFwEdhmumvCyrQLv4kr7sf2Jnp5sEXmu4OuHDpvSOAB/", KEY_ONE))
                .build();
        return obtenerRespuesta(request, serieCambio);
    }

    /**
     * Obtiene el importe del peso mexicano de forma equivalente con una divisa
     * extranjera, los importes obtenidos de la SIE API por sus siglas Sistema
     * de Información Económica del Banco de México son menos precisos en el
     * momento de consulta ya que el valor es acorde al tipo de cambio de cierre
     * de jornada de cada fin de mes.
     *
     * Se utiliza como respaldo en caso de que Exhange Rates API exceda el
     * número de peticiones.
     *
     * @param serieCambio Corresponde al código de la divisa a convertir.
     * @return Importe de la serieBase respecto a la serieCambio.
     */
    private static BigDecimal valorDivisaBancoDeMexicoAPI(String serieCambio) {
        if (serieCambio.equals(CLAVE_DIVISA_MEXICO)) {
            throw new IllegalArgumentException("La clave de divisa MXN es "
                    + "redundante en este método.");
        }
        String serieId;
        serieId = SeriesID.getIdSerie().get(serieCambio).toString();
        Request request = new Request.Builder()
                .url("https://www.banxico.org.mx/SieAPIRest/service/v1/series/"
                        + serieId + "/datos/oportuno")
                .addHeader("Bmx-Token", recursos.obtenerRecurso("h3FyCVddZ/dd/"
                        + "9ouuyxpgGuiFnmx9QrAMq8B99u6jUf6pZ2TKLDjGGf8ug91ShL5"
                        + "HMAQxTbJs9qPV4ub3Bs8ljTqroDDXsmwHuJO+sfRxoM=",
                        KEY_TWO))
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Conexion fallida: " + response);
            }
            String responseBody = response.body().string();
            response.body().close();
            return obtenerImporte(responseBody);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe estar conectado.",
                    "No es posible conectarse actualmente "
                    + "con la SIE API del Banco de México.",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error en la conexión" + ex);
        }
    }

    /**
     * En caso de que ocurra un error al tratar de obtener el valor con la API
     * Exchange Rates, se procede a usar la SIE API del Banco de México.
     *
     * @param request Solicitud al servidor.
     * @param serieCambio Valor o importe de cambio con respecto a la divisa
     * extranjera.
     * @return
     */
    private static BigDecimal obtenerRespuesta(Request request, String serieCambio) {
        String respuestaToString;
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                respuestaToString = response.body().string();
                response.body().close();
                return obtenerImporte(respuestaToString);
            } else {
                return valorDivisaBancoDeMexicoAPI(serieCambio);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe estar conectado.",
                    "No es posible conectarse actualmente "
                    + "con la API Exchange Rates.",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error en la conexión: " + ex);
        }
    }

    /**
     *
     * @param responseBody Objeto JSON obtenido de la API.
     * @return Importe de la serieBase respecto a la serieCambio.
     */
    private static BigDecimal obtenerImporte(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        double importe;
        if (responseBody.contains("rates")) {
            importe = json.getJSONObject("rates").getDouble("MXN");

            return new BigDecimal(importe);
        } else {
            importe = json.getJSONObject("bmx")
                    .getJSONArray("series")
                    .getJSONObject(0)
                    .getJSONArray("datos")
                    .getJSONObject(0)
                    .getDouble("dato");
            return new BigDecimal(importe);
        }
    }
}
