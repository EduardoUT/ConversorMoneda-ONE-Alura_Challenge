package com.ventanas;

import com.clases.divisa.PesoMexicano;
import com.clases.temperatura.Temperatura;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.math.BigDecimal;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Eduardo Reyes Hernández
 */
public final class Conversor extends javax.swing.JFrame {

    int xMouse;
    int yMouse;
    String valorConversion;
    double valorConversionToDouble;
    String valorSeleccionTipoConversion;
    String infoConversionRealizada;
    String resultadoConversion;
    PesoMexicano pesoMexicano;
    Temperatura temperatura = new Temperatura();
    BigDecimal valorConversionToBigDecimal;
    Color transparent = new Color(0, 0, 0, 0);
    Color darkRed = new Color(153, 0, 0, 255);
    Color verdeClaroBtnMenu = new Color(26, 96, 62, 255);
    Color verdeFuerteBtnMenu = new Color(14, 51, 33, 255);
    Color verdeAzulado = new Color(50, 143, 143, 255);
    Color verdeClaroBtnesDivisa = new Color(151, 208, 176, 255);
    Color verdeFuerteBtnesDivisa = new Color(28, 113, 81, 255);

    /**
     * Creates new form MenuConversion
     */
    public Conversor() {
        initComponents();
        setBackground(transparent);
        ventanaIngresoDivisa.setBackground(transparent);
        ventanaIngresoTemperatura.setBackground(transparent);
        btnCerrarVentana.setBackground(new Color(60, 63, 65, 255));
        btnMinimizarVentana.setBackground(new Color(60, 63, 65, 255));
        mostrarVentanaBienvenida();
        ocultarVentanaIngresoDivisa();
        ocultarPanelValorDivisa();
        ocultarPanelSeleccionDivisa();
        ocultarPanelResultadosDivisa();
        ocultarVentanaIngresoTemperatura();
        ocultarPanelValorTemperatura();
        ocultarPanelSeleccionTemperatura();
        ocultarPanelResultadosTemperatura();
    }

    public void mostrarVentanaBienvenida() {
        ventanaBienvenida.setVisible(true);
    }

    public void ocultarVentanaBienvenida() {
        ventanaBienvenida.setVisible(false);
    }

    public void mostrarVentanaIngresoDivisa() {
        ventanaIngresoDivisa.setVisible(true);
    }

    public void ocultarVentanaIngresoDivisa() {
        ventanaIngresoDivisa.setVisible(false);
    }

    public void mostrarPanelValorDivisa() {
        panelValorDivisa.setVisible(true);
        campoIngresoDivisa.setVisible(true);
        campoIngresoDivisa.requestFocus();
        instruccionCampoIngresoDivisa.setVisible(true);
        btnIngresoDivisa.setVisible(true);
        btnIngresoDivisa.setEnabled(false);
    }

    public void ocultarPanelValorDivisa() {
        panelValorDivisa.setVisible(false);
        campoIngresoDivisa.setVisible(false);
        instruccionCampoIngresoDivisa.setVisible(false);
        btnIngresoDivisa.setVisible(false);
    }

    public void mostrarPanelSeleccionDivisa() {
        panelSeleccionDivisa.setVisible(true);
        instruccionSeleccionDivisa.setVisible(true);
        campoSeleccionDivisa.setVisible(true);
        campoSeleccionDivisa.requestFocus();
        btnSeleccionDivisa.setVisible(true);
    }

    public void ocultarPanelSeleccionDivisa() {
        panelSeleccionDivisa.setVisible(false);
        instruccionSeleccionDivisa.setVisible(false);
        campoSeleccionDivisa.setVisible(false);
        btnSeleccionDivisa.setVisible(false);
    }

    public void mostrarPanelResultadosDivisa() {
        panelResultadosDivisa.setVisible(true);
        resultadoDivisa.setVisible(true);
        btnOtraConversionDivisa.setVisible(true);
    }

    public void ocultarPanelResultadosDivisa() {
        panelResultadosDivisa.setVisible(false);
        resultadoDivisa.setVisible(false);
        btnOtraConversionDivisa.setVisible(false);
    }

    public void mostrarResultadosDivisa(double valorUsuarioToDouble) {
        infoConversionUnoDivisa.setText("El ajuste de $ " + valorUsuarioToDouble);
        infoConversionDosDivisa.setText(valorSeleccionTipoConversion + " es de: ");
        resultadoDivisa.setText(String.valueOf(resultadoConversion));
    }

    public void reestablecerValoresDivisa() {
        campoIngresoDivisa.setText("");
        campoSeleccionDivisa.setSelectedItem("Peso Mexicano (MXN) a Dólar Américano (USD)");
        infoConversionUnoDivisa.setText("");
        infoConversionDosDivisa.setText("");
        resultadoDivisa.setText("");
    }

    public void mostrarVentanaIngresoTemperatura() {
        ventanaIngresoTemperatura.setVisible(true);
    }

    public void ocultarVentanaIngresoTemperatura() {
        ventanaIngresoTemperatura.setVisible(false);
    }

    public void mostrarPanelValorTemperatura() {
        panelValorTemperatura.setVisible(true);
        instruccionCampoIngresoTemperatura.setVisible(true);
        campoIngresoTemperatura.setVisible(true);
        campoIngresoTemperatura.requestFocus();
        btnIngresoTemperatura.setVisible(true);
        btnIngresoTemperatura.setEnabled(false);
    }

    public void ocultarPanelValorTemperatura() {
        panelValorTemperatura.setVisible(false);
        instruccionCampoIngresoTemperatura.setVisible(false);
        campoIngresoTemperatura.setVisible(false);
        btnIngresoTemperatura.setVisible(false);
    }

    public void mostrarPanelSeleccionTemperatura() {
        panelSeleccionTemperatura.setVisible(true);
        instruccionSeleccionTemperatura.setVisible(true);
        campoSeleccionTemperatura.setVisible(true);
        campoSeleccionDivisa.requestFocus();
        btnSeleccionTemperatura.setVisible(true);
    }

    public void ocultarPanelSeleccionTemperatura() {
        panelSeleccionTemperatura.setVisible(false);
        instruccionSeleccionTemperatura.setVisible(false);
        campoSeleccionTemperatura.setVisible(false);
        btnSeleccionTemperatura.setVisible(false);
    }

    public void mostrarPanelResultadosTemperatura() {
        panelResultadosTemperatura.setVisible(true);
        infoConversionTemperaturaUno.setVisible(true);
        infoConversionTemperaturaDos.setVisible(true);
        resultadoTemperatura.setVisible(true);
        btnOtraConversionTemperatura.setVisible(true);
    }

    public void ocultarPanelResultadosTemperatura() {
        panelResultadosTemperatura.setVisible(false);
        infoConversionTemperaturaUno.setVisible(false);
        infoConversionTemperaturaDos.setVisible(false);
        resultadoTemperatura.setVisible(false);
        btnOtraConversionTemperatura.setVisible(false);
    }

    public void mostrarResultadosTemperatura(double valorUsuarioToDouble, String valorSeleccionTipoConversion) {
        infoConversionTemperaturaUno.setText(String.valueOf(valorUsuarioToDouble));
        infoConversionTemperaturaDos.setText(valorSeleccionTipoConversion);
        resultadoTemperatura.setText(String.valueOf(resultadoConversion));
    }

    public void reestablecerValoresTemperatura() {
        campoIngresoTemperatura.setText("");
        campoSeleccionTemperatura.setSelectedItem("Celcius (°C) a Farenheit (°F)");
        infoConversionTemperaturaUno.setText("");
        infoConversionTemperaturaDos.setText("");
        resultadoTemperatura.setText("");
    }

    public boolean esMayorQueCero(String valorUsuario) {
        double valorToDouble = Double.valueOf(valorUsuario);
        return valorToDouble > 0;
    }

    /**
     * Para logo de escritorio.
     */
    @Override
    public Image getIconImage() {
        Image retImage = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("Imagenes/logo_personal.png"));
        return retImage;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        menuConversiones = new FondoPanelMenu();
        btnCerrarVentana = new javax.swing.JLabel();
        btnMinimizarVentana = new javax.swing.JLabel();
        btnConversionMoneda = new javax.swing.JLabel();
        btnConversionTemperatura = new javax.swing.JLabel();
        btnBienvenida = new javax.swing.JLabel();
        ventanaBienvenida = new FondoBienvenida();
        tituloBienvenida = new javax.swing.JLabel();
        nombreDesarrollador = new javax.swing.JLabel();
        ventanaIngresoDivisa = new javax.swing.JPanel();
        panelValorDivisa = new FondoPanelesPequenosCurrency();
        instruccionCampoIngresoDivisa = new javax.swing.JLabel();
        campoIngresoDivisa = new javax.swing.JTextField();
        separadorCampoInngresoDivisa = new javax.swing.JSeparator();
        btnIngresoDivisa = new javax.swing.JLabel();
        panelSeleccionDivisa = new FondoPanelesPequenosCurrency();
        instruccionSeleccionDivisa = new javax.swing.JLabel();
        campoSeleccionDivisa = new javax.swing.JComboBox<>();
        btnSeleccionDivisa = new javax.swing.JLabel();
        panelResultadosDivisa = new FondoPanelesPequenosCurrency();
        infoConversionUnoDivisa = new javax.swing.JLabel();
        infoConversionDosDivisa = new javax.swing.JLabel();
        resultadoDivisa = new javax.swing.JLabel();
        btnOtraConversionDivisa = new javax.swing.JLabel();
        ventanaIngresoTemperatura = new javax.swing.JPanel();
        panelValorTemperatura = new FondoPanelesPequenosTemperatura();
        instruccionCampoIngresoTemperatura = new javax.swing.JLabel();
        campoIngresoTemperatura = new javax.swing.JTextField();
        separadorCampoIngresoTemperatura = new javax.swing.JSeparator();
        btnIngresoTemperatura = new javax.swing.JLabel();
        panelSeleccionTemperatura = new FondoPanelesPequenosTemperatura();
        instruccionSeleccionTemperatura = new javax.swing.JLabel();
        campoSeleccionTemperatura = new javax.swing.JComboBox<>();
        btnSeleccionTemperatura = new javax.swing.JLabel();
        panelResultadosTemperatura = new FondoPanelesPequenosTemperatura();
        infoConversionTemperaturaUno = new javax.swing.JLabel();
        infoConversionTemperaturaDos = new javax.swing.JLabel();
        resultadoTemperatura = new javax.swing.JLabel();
        btnOtraConversionTemperatura = new javax.swing.JLabel();
        fondoPrincipal = new JLabelTransparente();

        jLabel4.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setIconImage(getIconImage());
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuConversiones.setBackground(new java.awt.Color(0, 102, 153));
        menuConversiones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCerrarVentana.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCerrarVentana.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCerrarVentana.setText("x");
        btnCerrarVentana.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarVentana.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCerrarVentana.setOpaque(true);
        btnCerrarVentana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCerrarVentanaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCerrarVentanaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCerrarVentanaMouseExited(evt);
            }
        });
        menuConversiones.add(btnCerrarVentana, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 30));

        btnMinimizarVentana.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnMinimizarVentana.setForeground(new java.awt.Color(255, 255, 255));
        btnMinimizarVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnMinimizarVentana.setText("_");
        btnMinimizarVentana.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnMinimizarVentana.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMinimizarVentana.setOpaque(true);
        btnMinimizarVentana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMinimizarVentanaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinimizarVentanaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinimizarVentanaMouseExited(evt);
            }
        });
        menuConversiones.add(btnMinimizarVentana, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 50, 30));

        btnConversionMoneda.setBackground(new java.awt.Color(14, 51, 33));
        btnConversionMoneda.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnConversionMoneda.setForeground(new java.awt.Color(244, 246, 252));
        btnConversionMoneda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnConversionMoneda.setText("Conversor de Moneda");
        btnConversionMoneda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConversionMoneda.setOpaque(true);
        btnConversionMoneda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConversionMonedaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConversionMonedaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConversionMonedaMouseExited(evt);
            }
        });
        menuConversiones.add(btnConversionMoneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 240, 30));

        btnConversionTemperatura.setBackground(new java.awt.Color(14, 51, 33));
        btnConversionTemperatura.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnConversionTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        btnConversionTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnConversionTemperatura.setText("Conversor de Temperatura");
        btnConversionTemperatura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConversionTemperatura.setOpaque(true);
        btnConversionTemperatura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConversionTemperaturaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConversionTemperaturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConversionTemperaturaMouseExited(evt);
            }
        });
        menuConversiones.add(btnConversionTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 240, 30));

        btnBienvenida.setBackground(new java.awt.Color(14, 51, 33));
        btnBienvenida.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBienvenida.setForeground(new java.awt.Color(244, 246, 252));
        btnBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnBienvenida.setText("Bienvenida");
        btnBienvenida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBienvenida.setOpaque(true);
        btnBienvenida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBienvenidaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBienvenidaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBienvenidaMouseExited(evt);
            }
        });
        menuConversiones.add(btnBienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 240, 30));

        getContentPane().add(menuConversiones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 470));

        ventanaBienvenida.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tituloBienvenida.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tituloBienvenida.setForeground(new java.awt.Color(0, 0, 0));
        tituloBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloBienvenida.setText("Bienvenido(a)");
        ventanaBienvenida.add(tituloBienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 620, -1));

        nombreDesarrollador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nombreDesarrollador.setForeground(new java.awt.Color(0, 0, 0));
        nombreDesarrollador.setText("Desarrollado por: Eduardo Reyes Hernández");
        ventanaBienvenida.add(nombreDesarrollador, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 340, -1));

        getContentPane().add(ventanaBienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 630, 470));

        ventanaIngresoDivisa.setBackground(new java.awt.Color(0, 204, 204));
        ventanaIngresoDivisa.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelValorDivisa.setBackground(new java.awt.Color(153, 0, 153));
        panelValorDivisa.setForeground(new java.awt.Color(244, 246, 252));
        panelValorDivisa.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        instruccionCampoIngresoDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        instruccionCampoIngresoDivisa.setForeground(new java.awt.Color(244, 246, 252));
        instruccionCampoIngresoDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instruccionCampoIngresoDivisa.setText("Ingresa la cantidad de dinero que deseas convertir:");
        panelValorDivisa.add(instruccionCampoIngresoDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 580, 30));

        campoIngresoDivisa.setBackground(new java.awt.Color(70, 174, 124));
        campoIngresoDivisa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        campoIngresoDivisa.setForeground(new java.awt.Color(244, 246, 252));
        campoIngresoDivisa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoIngresoDivisa.setBorder(null);
        campoIngresoDivisa.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        campoIngresoDivisa.setMaximumSize(new java.awt.Dimension(2, 2));
        campoIngresoDivisa.setNextFocusableComponent(btnIngresoDivisa);
        campoIngresoDivisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoIngresoDivisaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIngresoDivisaKeyTyped(evt);
            }
        });
        panelValorDivisa.add(campoIngresoDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 240, 40));

        separadorCampoInngresoDivisa.setBackground(new java.awt.Color(244, 246, 252));
        separadorCampoInngresoDivisa.setForeground(new java.awt.Color(244, 246, 252));
        separadorCampoInngresoDivisa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        separadorCampoInngresoDivisa.setOpaque(true);
        panelValorDivisa.add(separadorCampoInngresoDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 240, -1));

        btnIngresoDivisa.setBackground(new java.awt.Color(28, 113, 81));
        btnIngresoDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnIngresoDivisa.setForeground(new java.awt.Color(244, 246, 252));
        btnIngresoDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnIngresoDivisa.setText("Aceptar");
        btnIngresoDivisa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIngresoDivisa.setOpaque(true);
        btnIngresoDivisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnIngresoDivisaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIngresoDivisaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIngresoDivisaMouseExited(evt);
            }
        });
        panelValorDivisa.add(btnIngresoDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 260, 110, 40));

        ventanaIngresoDivisa.add(panelValorDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 580, 430));

        panelSeleccionDivisa.setNextFocusableComponent(campoSeleccionDivisa);
        panelSeleccionDivisa.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        instruccionSeleccionDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        instruccionSeleccionDivisa.setForeground(new java.awt.Color(244, 246, 252));
        instruccionSeleccionDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instruccionSeleccionDivisa.setText("Selecciona la moneda a la que deseas convertir tu dinero:");
        panelSeleccionDivisa.add(instruccionSeleccionDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 580, 30));

        campoSeleccionDivisa.setBackground(new java.awt.Color(70, 174, 124));
        campoSeleccionDivisa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        campoSeleccionDivisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Peso Mexicano (MXN) a Dólar Américano (USD)", "Peso Mexicano (MXN) a Euro (EUR)", "Peso Mexicano (MXN) a Libra Esterlina (GBP)", "Peso Mexicano (MXN) a Yen (JPY)", "Peso Mexicano (MXN) a Won Coreano (KRW)", "Dólar Americano (USD) a Peso Mexicano (MXN)", "Euro (EUR) a Peso Mexicano (MXN)", "Libra Exterlina (GBP) a Peso Mexicano (MXN)", "Yen (JPY) a Peso Mexicano (MXN)", "Won Coreano (KRW) a Peso Mexicano (MXN)" }));
        panelSeleccionDivisa.add(campoSeleccionDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 350, 40));

        btnSeleccionDivisa.setBackground(new java.awt.Color(28, 113, 81));
        btnSeleccionDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSeleccionDivisa.setForeground(new java.awt.Color(244, 246, 252));
        btnSeleccionDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSeleccionDivisa.setText("Aceptar");
        btnSeleccionDivisa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSeleccionDivisa.setOpaque(true);
        btnSeleccionDivisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSeleccionDivisaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSeleccionDivisaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSeleccionDivisaMouseExited(evt);
            }
        });
        panelSeleccionDivisa.add(btnSeleccionDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 260, 110, 40));

        ventanaIngresoDivisa.add(panelSeleccionDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 580, 430));

        panelResultadosDivisa.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        infoConversionUnoDivisa.setBackground(new java.awt.Color(153, 153, 0));
        infoConversionUnoDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoConversionUnoDivisa.setForeground(new java.awt.Color(244, 246, 252));
        infoConversionUnoDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoConversionUnoDivisa.setToolTipText("");
        panelResultadosDivisa.add(infoConversionUnoDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 580, 40));

        infoConversionDosDivisa.setBackground(new java.awt.Color(0, 153, 204));
        infoConversionDosDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoConversionDosDivisa.setForeground(new java.awt.Color(244, 246, 252));
        infoConversionDosDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoConversionDosDivisa.setToolTipText("");
        panelResultadosDivisa.add(infoConversionDosDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 580, 40));

        resultadoDivisa.setBackground(new java.awt.Color(0, 204, 102));
        resultadoDivisa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        resultadoDivisa.setForeground(new java.awt.Color(244, 246, 252));
        resultadoDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelResultadosDivisa.add(resultadoDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 560, 80));

        btnOtraConversionDivisa.setBackground(new java.awt.Color(28, 113, 81));
        btnOtraConversionDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnOtraConversionDivisa.setForeground(new java.awt.Color(244, 246, 252));
        btnOtraConversionDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnOtraConversionDivisa.setText("Hacer otra conversión");
        btnOtraConversionDivisa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOtraConversionDivisa.setOpaque(true);
        btnOtraConversionDivisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOtraConversionDivisaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOtraConversionDivisaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOtraConversionDivisaMouseExited(evt);
            }
        });
        panelResultadosDivisa.add(btnOtraConversionDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 360, 190, 40));

        ventanaIngresoDivisa.add(panelResultadosDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 580, 430));

        getContentPane().add(ventanaIngresoDivisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 630, 470));

        ventanaIngresoTemperatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelValorTemperatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        instruccionCampoIngresoTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        instruccionCampoIngresoTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        instruccionCampoIngresoTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instruccionCampoIngresoTemperatura.setText("Ingresa el valor de Temperatura que deseas convertir:");
        panelValorTemperatura.add(instruccionCampoIngresoTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 580, -1));

        campoIngresoTemperatura.setBackground(new java.awt.Color(21, 73, 255));
        campoIngresoTemperatura.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        campoIngresoTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        campoIngresoTemperatura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoIngresoTemperatura.setBorder(null);
        campoIngresoTemperatura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoIngresoTemperaturaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIngresoTemperaturaKeyTyped(evt);
            }
        });
        panelValorTemperatura.add(campoIngresoTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 240, 40));

        separadorCampoIngresoTemperatura.setBackground(new java.awt.Color(244, 246, 252));
        separadorCampoIngresoTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        separadorCampoIngresoTemperatura.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        separadorCampoIngresoTemperatura.setOpaque(true);
        panelValorTemperatura.add(separadorCampoIngresoTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 240, -1));

        btnIngresoTemperatura.setBackground(new java.awt.Color(50, 143, 143));
        btnIngresoTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnIngresoTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        btnIngresoTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnIngresoTemperatura.setText("Aceptar");
        btnIngresoTemperatura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIngresoTemperatura.setOpaque(true);
        btnIngresoTemperatura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnIngresoTemperaturaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIngresoTemperaturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIngresoTemperaturaMouseExited(evt);
            }
        });
        panelValorTemperatura.add(btnIngresoTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 260, 110, 40));

        ventanaIngresoTemperatura.add(panelValorTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 580, 430));

        panelSeleccionTemperatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        instruccionSeleccionTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        instruccionSeleccionTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        instruccionSeleccionTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instruccionSeleccionTemperatura.setText("Elije el tipo de conversión de temperatura que deseas hacer:");
        panelSeleccionTemperatura.add(instruccionSeleccionTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 580, -1));

        campoSeleccionTemperatura.setBackground(new java.awt.Color(21, 73, 255));
        campoSeleccionTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        campoSeleccionTemperatura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Celcius (°C) a Farenheit (°F)", "Celcius (°C) a Kelvin (°K)", "Farenheit (°F) a Celsius (°C)", "Farenheit (°F) a Kelvin (°K)", "Kelvin (°K) a Celsius (°C)", "Kelvin (°K) a Farenheit (°F)" }));
        panelSeleccionTemperatura.add(campoSeleccionTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 240, 40));

        btnSeleccionTemperatura.setBackground(new java.awt.Color(50, 143, 143));
        btnSeleccionTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSeleccionTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        btnSeleccionTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSeleccionTemperatura.setText("Aceptar");
        btnSeleccionTemperatura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSeleccionTemperatura.setOpaque(true);
        btnSeleccionTemperatura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSeleccionTemperaturaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSeleccionTemperaturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSeleccionTemperaturaMouseExited(evt);
            }
        });
        panelSeleccionTemperatura.add(btnSeleccionTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 260, 110, 40));

        ventanaIngresoTemperatura.add(panelSeleccionTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 580, 430));

        panelResultadosTemperatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        infoConversionTemperaturaUno.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoConversionTemperaturaUno.setForeground(new java.awt.Color(244, 246, 252));
        infoConversionTemperaturaUno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelResultadosTemperatura.add(infoConversionTemperaturaUno, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 580, 40));

        infoConversionTemperaturaDos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoConversionTemperaturaDos.setForeground(new java.awt.Color(244, 246, 252));
        infoConversionTemperaturaDos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelResultadosTemperatura.add(infoConversionTemperaturaDos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 580, 40));

        resultadoTemperatura.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        resultadoTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        resultadoTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelResultadosTemperatura.add(resultadoTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 560, 80));

        btnOtraConversionTemperatura.setBackground(new java.awt.Color(50, 143, 143));
        btnOtraConversionTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnOtraConversionTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        btnOtraConversionTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnOtraConversionTemperatura.setText("Hacer otra conversión");
        btnOtraConversionTemperatura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOtraConversionTemperatura.setOpaque(true);
        btnOtraConversionTemperatura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOtraConversionTemperaturaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOtraConversionTemperaturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOtraConversionTemperaturaMouseExited(evt);
            }
        });
        panelResultadosTemperatura.add(btnOtraConversionTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 360, 190, 40));

        ventanaIngresoTemperatura.add(panelResultadosTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 580, 430));

        getContentPane().add(ventanaIngresoTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 630, 470));

        fondoPrincipal.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                fondoPrincipalMouseDragged(evt);
            }
        });
        fondoPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fondoPrincipalMousePressed(evt);
            }
        });
        getContentPane().add(fondoPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 470));

        setSize(new java.awt.Dimension(866, 470));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fondoPrincipalMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fondoPrincipalMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_fondoPrincipalMousePressed

    private void fondoPrincipalMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fondoPrincipalMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_fondoPrincipalMouseDragged

    private void campoIngresoDivisaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIngresoDivisaKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car != '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_campoIngresoDivisaKeyTyped

    private void btnConversionMonedaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionMonedaMouseClicked
        evt.consume();
        if (ventanaBienvenida.isVisible());
        ocultarVentanaBienvenida();
        if (ventanaIngresoTemperatura.isVisible());
        ocultarVentanaIngresoTemperatura();

        mostrarVentanaIngresoDivisa();
        mostrarPanelValorDivisa();
        reestablecerValoresDivisa();
    }//GEN-LAST:event_btnConversionMonedaMouseClicked

    private void btnConversionTemperaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionTemperaturaMouseClicked
        evt.consume();
        if (ventanaBienvenida.isVisible());
        ocultarVentanaBienvenida();
        if (ventanaIngresoDivisa.isVisible());
        ocultarVentanaIngresoDivisa();

        mostrarVentanaIngresoTemperatura();
        mostrarPanelValorTemperatura();
        reestablecerValoresTemperatura();
    }//GEN-LAST:event_btnConversionTemperaturaMouseClicked

    private void campoIngresoDivisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIngresoDivisaKeyReleased
        evt.consume();
        valorConversion = campoIngresoDivisa.getText();
        if (ComprobarValorNumerico.esValorDecimal(valorConversion)) {
            btnIngresoDivisa.setEnabled(true);
        } else {
            btnIngresoDivisa.setEnabled(false);
            //btnIngresoDivisa.setForeground(new Color(60, 63, 65, 255));
        }
    }//GEN-LAST:event_campoIngresoDivisaKeyReleased

    private void btnBienvenidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBienvenidaMouseClicked
        evt.consume();
        if (ventanaIngresoDivisa.isVisible());
        ocultarVentanaIngresoDivisa();
        if (ventanaIngresoTemperatura.isVisible());
        ocultarVentanaIngresoTemperatura();

        mostrarVentanaBienvenida();
    }//GEN-LAST:event_btnBienvenidaMouseClicked

    private void btnSeleccionDivisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionDivisaMouseClicked
        evt.consume();
        if (panelValorDivisa.isVisible());
        ocultarPanelValorDivisa();
        if (panelSeleccionDivisa.isVisible());
        ocultarPanelSeleccionDivisa();
        if (panelResultadosDivisa.isVisible());
        ocultarPanelResultadosDivisa();

        try {
            valorConversionToDouble = Double.valueOf(valorConversion);
            valorSeleccionTipoConversion = String.valueOf(campoSeleccionDivisa.getSelectedItem());
            ComprobarValorNumerico.esNumerico(valorConversionToDouble);
            pesoMexicano = new PesoMexicano(new BigDecimal(valorConversionToDouble));
            resultadoConversion = String.valueOf(pesoMexicano.hacerConversion(valorSeleccionTipoConversion));
            mostrarPanelResultadosDivisa();
            mostrarResultadosDivisa(valorConversionToDouble);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "El valor ingresado no es un número entero o decimal",
                    "Error al recibir los datos.",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnSeleccionDivisaMouseClicked

    private void btnOtraConversionDivisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtraConversionDivisaMouseClicked
        evt.consume();
        if (panelValorDivisa.isVisible());
        ocultarPanelValorDivisa();
        if (panelSeleccionDivisa.isVisible());
        ocultarPanelSeleccionDivisa();
        if (panelResultadosDivisa.isVisible());
        ocultarPanelResultadosDivisa();

        reestablecerValoresDivisa();
        mostrarPanelValorDivisa();
    }//GEN-LAST:event_btnOtraConversionDivisaMouseClicked

    private void btnCerrarVentanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseClicked
        evt.consume();
        System.exit(0);
    }//GEN-LAST:event_btnCerrarVentanaMouseClicked

    private void btnMinimizarVentanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizarVentanaMouseClicked
        evt.consume();
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_btnMinimizarVentanaMouseClicked

    private void campoIngresoTemperaturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIngresoTemperaturaKeyReleased
        evt.consume();
        valorConversion = campoIngresoTemperatura.getText();
        if (ComprobarValorNumerico.esValorDecimalOpcionalNegativo(valorConversion)) {
            btnIngresoTemperatura.setEnabled(true);
        } else {
            btnIngresoTemperatura.setEnabled(false);
            btnIngresoTemperatura.setBackground(verdeAzulado);
        }
    }//GEN-LAST:event_campoIngresoTemperaturaKeyReleased

    private void campoIngresoTemperaturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIngresoTemperaturaKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car != '.') && (car != '-')) {
            evt.consume();
        }
    }//GEN-LAST:event_campoIngresoTemperaturaKeyTyped

    private void btnIngresoTemperaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresoTemperaturaMouseClicked
        if (btnIngresoTemperatura.isEnabled()) {
            evt.consume();
            if (panelValorTemperatura.isVisible());
            ocultarPanelValorTemperatura();
            if (panelResultadosTemperatura.isVisible());
            ocultarPanelResultadosTemperatura();

            mostrarPanelSeleccionTemperatura();
        }

    }//GEN-LAST:event_btnIngresoTemperaturaMouseClicked

    private void btnSeleccionTemperaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionTemperaturaMouseClicked
        evt.consume();
        if (panelValorTemperatura.isVisible());
        ocultarPanelValorTemperatura();
        if (panelSeleccionTemperatura.isVisible());
        ocultarPanelSeleccionTemperatura();
        if (panelResultadosTemperatura.isVisible());
        ocultarPanelResultadosTemperatura();

        try {
            valorConversionToDouble = Double.valueOf(valorConversion);
            valorSeleccionTipoConversion = String.valueOf(campoSeleccionTemperatura.getSelectedItem());
            ComprobarValorNumerico.esNumerico(valorConversionToDouble);
            valorConversionToBigDecimal = new BigDecimal(String.valueOf(valorConversionToDouble));
            resultadoConversion = temperatura
                    .conversionTemperatura(valorConversionToBigDecimal, valorSeleccionTipoConversion);
            mostrarPanelResultadosTemperatura();
            mostrarResultadosTemperatura(valorConversionToDouble, valorSeleccionTipoConversion);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "El valor ingresado no es un número entero o decimal",
                    "Error al recibir los valores.",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnSeleccionTemperaturaMouseClicked

    private void btnOtraConversionTemperaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtraConversionTemperaturaMouseClicked
        evt.consume();
        if (panelValorTemperatura.isVisible());
        ocultarPanelValorTemperatura();
        if (panelSeleccionTemperatura.isVisible());
        ocultarPanelSeleccionTemperatura();
        if (panelResultadosTemperatura.isVisible());
        ocultarPanelResultadosTemperatura();

        reestablecerValoresTemperatura();
        mostrarPanelValorTemperatura();
    }//GEN-LAST:event_btnOtraConversionTemperaturaMouseClicked

    private void btnIngresoDivisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresoDivisaMouseClicked
        evt.consume();
        if (Double.valueOf(valorConversion) > 0) {
            if (btnIngresoDivisa.isEnabled()) {
                if (panelValorDivisa.isVisible());
                ocultarPanelValorDivisa();
                if (panelResultadosDivisa.isVisible());
                ocultarPanelResultadosDivisa();

                mostrarPanelSeleccionDivisa();
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "No es posible ingresar valores igual a 0.",
                    "Error al recibir el valor ingresado: ",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }//GEN-LAST:event_btnIngresoDivisaMouseClicked

    private void btnCerrarVentanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseEntered
        evt.consume();
        btnCerrarVentana.setBackground(darkRed);
    }//GEN-LAST:event_btnCerrarVentanaMouseEntered

    private void btnCerrarVentanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseExited
        evt.consume();
        btnCerrarVentana.setBackground(new Color(60, 63, 65, 255));
    }//GEN-LAST:event_btnCerrarVentanaMouseExited

    private void btnMinimizarVentanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizarVentanaMouseEntered
        evt.consume();
        btnMinimizarVentana.setBackground(new Color(102, 102, 102, 255));
    }//GEN-LAST:event_btnMinimizarVentanaMouseEntered

    private void btnMinimizarVentanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizarVentanaMouseExited
        evt.consume();
        btnMinimizarVentana.setBackground(new Color(60, 63, 65, 255));
    }//GEN-LAST:event_btnMinimizarVentanaMouseExited

    private void btnConversionMonedaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionMonedaMouseEntered
        evt.consume();
        btnConversionMoneda.setBackground(verdeClaroBtnMenu);

    }//GEN-LAST:event_btnConversionMonedaMouseEntered

    private void btnConversionMonedaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionMonedaMouseExited
        evt.consume();
        btnConversionMoneda.setBackground(verdeFuerteBtnMenu);
    }//GEN-LAST:event_btnConversionMonedaMouseExited

    private void btnConversionTemperaturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionTemperaturaMouseEntered
        evt.consume();
        btnConversionTemperatura.setBackground(verdeClaroBtnMenu);
    }//GEN-LAST:event_btnConversionTemperaturaMouseEntered

    private void btnConversionTemperaturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionTemperaturaMouseExited
        evt.consume();
        btnConversionTemperatura.setBackground(verdeFuerteBtnMenu);
    }//GEN-LAST:event_btnConversionTemperaturaMouseExited

    private void btnBienvenidaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBienvenidaMouseEntered
        evt.consume();
        btnBienvenida.setBackground(verdeClaroBtnMenu);
    }//GEN-LAST:event_btnBienvenidaMouseEntered

    private void btnBienvenidaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBienvenidaMouseExited
        evt.consume();
        btnBienvenida.setBackground(verdeFuerteBtnMenu);
    }//GEN-LAST:event_btnBienvenidaMouseExited

    private void btnIngresoTemperaturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresoTemperaturaMouseEntered
        if (btnIngresoTemperatura.isEnabled()) {
            evt.consume();
            btnIngresoTemperatura.setBackground(Color.BLACK);
        }

    }//GEN-LAST:event_btnIngresoTemperaturaMouseEntered

    private void btnIngresoTemperaturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresoTemperaturaMouseExited
        if (btnIngresoTemperatura.isEnabled()) {
            evt.consume();
            btnIngresoTemperatura.setBackground(verdeAzulado);
        }
    }//GEN-LAST:event_btnIngresoTemperaturaMouseExited

    private void btnSeleccionTemperaturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionTemperaturaMouseEntered
        evt.consume();
        btnSeleccionTemperatura.setBackground(Color.BLACK);
    }//GEN-LAST:event_btnSeleccionTemperaturaMouseEntered

    private void btnSeleccionTemperaturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionTemperaturaMouseExited
        evt.consume();
        btnSeleccionTemperatura.setBackground(verdeAzulado);
    }//GEN-LAST:event_btnSeleccionTemperaturaMouseExited

    private void btnOtraConversionTemperaturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtraConversionTemperaturaMouseEntered
        evt.consume();
        btnOtraConversionTemperatura.setBackground(Color.BLACK);
    }//GEN-LAST:event_btnOtraConversionTemperaturaMouseEntered

    private void btnOtraConversionTemperaturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtraConversionTemperaturaMouseExited
        evt.consume();
        btnOtraConversionTemperatura.setBackground(verdeAzulado);
    }//GEN-LAST:event_btnOtraConversionTemperaturaMouseExited

    private void btnIngresoDivisaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresoDivisaMouseEntered
        if (btnIngresoDivisa.isEnabled()) {
            evt.consume();
            btnIngresoDivisa.setBackground(verdeClaroBtnesDivisa);
            btnIngresoDivisa.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_btnIngresoDivisaMouseEntered

    private void btnIngresoDivisaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresoDivisaMouseExited
        if (btnIngresoDivisa.isEnabled()) {
            evt.consume();
            btnIngresoDivisa.setBackground(verdeFuerteBtnesDivisa);
            btnIngresoDivisa.setForeground(new Color(244, 246, 252, 255));
        }
    }//GEN-LAST:event_btnIngresoDivisaMouseExited

    private void btnSeleccionDivisaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionDivisaMouseEntered
        evt.consume();
        btnSeleccionDivisa.setBackground(verdeClaroBtnesDivisa);
        btnSeleccionDivisa.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnSeleccionDivisaMouseEntered

    private void btnSeleccionDivisaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeleccionDivisaMouseExited
        evt.consume();
        btnSeleccionDivisa.setBackground(verdeFuerteBtnesDivisa);
        btnSeleccionDivisa.setForeground(new Color(244, 246, 252, 255));
    }//GEN-LAST:event_btnSeleccionDivisaMouseExited

    private void btnOtraConversionDivisaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtraConversionDivisaMouseEntered
        evt.consume();
        btnOtraConversionDivisa.setBackground(verdeClaroBtnesDivisa);
        btnOtraConversionDivisa.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnOtraConversionDivisaMouseEntered

    private void btnOtraConversionDivisaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtraConversionDivisaMouseExited
        evt.consume();
        btnOtraConversionDivisa.setBackground(verdeFuerteBtnesDivisa);
        btnOtraConversionDivisa.setForeground(new Color(244, 246, 252, 255));
    }//GEN-LAST:event_btnOtraConversionDivisaMouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Conversor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Conversor().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnBienvenida;
    private javax.swing.JLabel btnCerrarVentana;
    private javax.swing.JLabel btnConversionMoneda;
    private javax.swing.JLabel btnConversionTemperatura;
    private javax.swing.JLabel btnIngresoDivisa;
    private javax.swing.JLabel btnIngresoTemperatura;
    private javax.swing.JLabel btnMinimizarVentana;
    private javax.swing.JLabel btnOtraConversionDivisa;
    private javax.swing.JLabel btnOtraConversionTemperatura;
    private javax.swing.JLabel btnSeleccionDivisa;
    private javax.swing.JLabel btnSeleccionTemperatura;
    private javax.swing.JTextField campoIngresoDivisa;
    private javax.swing.JTextField campoIngresoTemperatura;
    private javax.swing.JComboBox<String> campoSeleccionDivisa;
    private javax.swing.JComboBox<String> campoSeleccionTemperatura;
    private javax.swing.JLabel fondoPrincipal;
    private javax.swing.JLabel infoConversionDosDivisa;
    private javax.swing.JLabel infoConversionTemperaturaDos;
    private javax.swing.JLabel infoConversionTemperaturaUno;
    private javax.swing.JLabel infoConversionUnoDivisa;
    private javax.swing.JLabel instruccionCampoIngresoDivisa;
    private javax.swing.JLabel instruccionCampoIngresoTemperatura;
    private javax.swing.JLabel instruccionSeleccionDivisa;
    private javax.swing.JLabel instruccionSeleccionTemperatura;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel menuConversiones;
    private javax.swing.JLabel nombreDesarrollador;
    private javax.swing.JPanel panelResultadosDivisa;
    private javax.swing.JPanel panelResultadosTemperatura;
    private javax.swing.JPanel panelSeleccionDivisa;
    private javax.swing.JPanel panelSeleccionTemperatura;
    private javax.swing.JPanel panelValorDivisa;
    private javax.swing.JPanel panelValorTemperatura;
    private javax.swing.JLabel resultadoDivisa;
    private javax.swing.JLabel resultadoTemperatura;
    private javax.swing.JSeparator separadorCampoIngresoTemperatura;
    private javax.swing.JSeparator separadorCampoInngresoDivisa;
    private javax.swing.JLabel tituloBienvenida;
    private javax.swing.JPanel ventanaBienvenida;
    private javax.swing.JPanel ventanaIngresoDivisa;
    private javax.swing.JPanel ventanaIngresoTemperatura;
    // End of variables declaration//GEN-END:variables
    class BannerConversor extends JLabel {

        private Image imagen;

        @Override
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/Imagenes/bannerConversor.png")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }

    class FondoPanelMenu extends JPanel {

        private Image imagen;

        @Override
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/Imagenes/FondoMenu.png")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }

    class FondoBienvenida extends JPanel {

        private Image imagen;

        @Override
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/Imagenes/challengeImage.jpg")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }

    class FondoPanelesPequenosCurrency extends JPanel {

        private Image imagen;

        @Override
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/Imagenes/FondoPanelCurrency.png")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }

    class FondoPanelesPequenosTemperatura extends JPanel {

        private Image imagen;

        @Override
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/Imagenes/FondoPanelTemperatura.png")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }
}
