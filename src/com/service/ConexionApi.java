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
import javax.swing.JOptionPane;
import com.clases.divisa.Divisa;
import java.net.UnknownHostException;

/**
 *
 * @author mcore
 */
public class ConexionApi {

    private static final OkHttpClient httpClient = new OkHttpClient();

    public static double tasaBaseCambio(String tasaBase, String tasaCambio) throws Exception {
        Request r = new Request.Builder()
                .url("https://api.apilayer.com/fixer/latest?base=" + tasaBase + "&symbols=" + tasaCambio + "")//EUR,GBP,USD,KRW,JPY
                .addHeader("apikey", "dYpjEe0JbW770473cXv6VOh0LSTxQvYx")
                .build();
        try {
            Response response = httpClient.newCall(r).execute();
            String res;
            double valorMoneda;
            if (!response.isSuccessful()) {
                throw new IOException("Código inesperado: " + response);
            }
            res = response.body().string();
            valorMoneda = obtenerValorRates(res);
            System.out.println(valorMoneda);
            return valorMoneda;

        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Obtendrá a continuación la conversión, esta será respecto al cierre del mes 29 de"
                    + " abril de 2022, según el Banco de México."
                    + "\nPosibles causas: "
                    + "\n1. Para obtener conversiones obtenidas en tiempo real, debe conectarse a una red."
                    + "\n2. El límite de consumo del servicio por la API Fixer ha excedido las 100 peticiones.",
                    "Error al conectar con la API Fixer.",
                    JOptionPane.ERROR_MESSAGE
            );
            if (tasaBase.contains(Divisa.getNOMBRE_DIVISA_USA())) {
                return 19.68550;
            } else if (tasaBase.contains(Divisa.getNOMBRE_DIVISA_EUROPA())) {
                return 21.2145;
            } else if (tasaBase.contains(Divisa.getNOMBRE_DIVISA_GRAN_BRETANA())) {
                return 25.54861;
            } else if (tasaBase.contains(Divisa.getNOMBRE_DIVISA_YEN_JAPON())) {
                return 0.15724;
            } else {
                return 16.19689;
            }
        }
    }

    public static double obtenerValorRates(String response) {
        //String currencyTest = "{\"success\":true,\"timestamp\":1657565,\"base\":\"USD\",\"date\":\"2022-05-31\",\"rates\":{\"MXN\":19.678404}}";
        JSONObject json = new JSONObject(response);
        double mxnRate = json.getJSONObject("rates").getDouble("MXN");
        return mxnRate;
    }
}
