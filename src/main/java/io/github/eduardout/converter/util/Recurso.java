/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Eduardo Reyes Hernández
 */
public class Recurso {

    private SecretKeySpec generarRecurso(String clave) {
        try {
            byte[] claveEncriptacion = clave.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            claveEncriptacion = sha.digest(claveEncriptacion);
            claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);
            SecretKeySpec secretKey = new SecretKeySpec(claveEncriptacion, "AES");
            return secretKey;
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error al generar recurso: " + ex);
        }
    }

    public String asignarRecurso(String datos, String claveSecreta) {
        try {
            SecretKeySpec secretKey = this.generarRecurso(claveSecreta);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] datosEncriptar = datos.getBytes("UTF-8");
            byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
            String encriptado = Base64.getEncoder()
                    .encodeToString(bytesEncriptados);
            return encriptado;
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException
                | BadPaddingException | InvalidKeyException
                | NoSuchPaddingException | UnsupportedEncodingException ex) {
            throw new RuntimeException("Error al asignar recurso: " + ex);
        }
    }

    public String obtenerRecurso(String datosEncriptados, String claveSecreta) {
        try {
            SecretKeySpec secretKey = this.generarRecurso(claveSecreta);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] bytesEncriptados = Base64.getDecoder()
                    .decode(datosEncriptados);
            byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
            String datos = new String(datosDesencriptados);
            return datos;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException ex) {
            throw new RuntimeException("Error al obtener recurso: " + ex);
        }
    }
}
