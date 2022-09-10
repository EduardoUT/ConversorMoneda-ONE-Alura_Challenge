/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clases.divisa;

import java.math.BigDecimal;

/**
 * 
 * @author Eduardo Reyes Hernández
 */
public abstract class TestDivisa {
    
    private String claveDivisa;
    private String descripcionDivisa;
    private BigDecimal valor;
    
    public TestDivisa(String claveDivisa, String descripcionDivisa, BigDecimal valor) {
        if (claveDivisa == null || claveDivisa.isEmpty()) {
            throw new IllegalArgumentException("Valor nulo o vacío");
        }
        
        if (descripcionDivisa == null || descripcionDivisa.isEmpty()) {
            throw new IllegalArgumentException("Valor nulo o vacío");
        }
        this.claveDivisa = claveDivisa;
        this.descripcionDivisa = descripcionDivisa;
        this.valor = valor;
    }

    /**
     * @return the claveDivisa
     */
    public String getClaveDivisa() {
        return claveDivisa;
    }

    /**
     * @param claveDivisa the claveDivisa to set
     */
    public void setClaveDivisa(String claveDivisa) {
        this.claveDivisa = claveDivisa;
    }

    /**
     * @return the descripcionDivisa
     */
    public String getDescripcionDivisa() {
        return descripcionDivisa;
    }

    /**
     * @param descripcionDivisa the descripcionDivisa to set
     */
    public void setDescripcionDivisa(String descripcionDivisa) {
        this.descripcionDivisa = descripcionDivisa;
    }

    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
}
