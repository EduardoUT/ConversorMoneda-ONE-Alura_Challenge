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
package io.github.eduardout.converter.view;

import static io.github.eduardout.converter.util.EvaluateDoubleValue.*;
import io.github.eduardout.converter.GlobalLogger;
import io.github.eduardout.converter.currency.CurrencyConverter;
import io.github.eduardout.converter.currency.CurrencyConverterController;
import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.ExchangeAPIService;
import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.currency.provider.APIClient;
import io.github.eduardout.converter.currency.repository.JSONCurrencyFileRepository;
import io.github.eduardout.converter.temperature.CelsiusToFarenheit;
import io.github.eduardout.converter.temperature.TemperatureConverter;
import io.github.eduardout.converter.temperature.TemperatureConverterController;
import io.github.eduardout.converter.temperature.TemperatureSymbol;
import io.github.eduardout.converter.util.ExchangeAPIParser;
import io.github.eduardout.converter.util.ImageLoader;
import io.github.eduardout.converter.util.RateParser;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author EduardoUT
 */
public final class ConverterUI extends javax.swing.JFrame {

    private int xMouse;
    private int yMouse;
    private String inputValue;
    private CurrencyConverterController converterController;
    private TemperatureConverterController temperatureConverterController;
    private Color transparent;
    private Color darkRed;
    private Color darkGreen;
    private Color darksLateGreen;
    private Color darksLateGray;
    private Color darkCyan;
    private Color dimGray;
    private Color ghostWhite;
    private transient MouseAdapter mouseAdapterCurrency;
    private transient MouseAdapter mouseAdapterTemperature;

    /**
     * Creates new form MenuConversion
     */
    public ConverterUI() {
        initComponents();
        initialize();
    }

    private void initialize() {
        String oppositeArrows = "images/oppositeArrows.png";
        setIconImage(ImageLoader.getImage("images/logo_personal.png"));
        arrowLabelOne.setIcon(new ImageIcon(ImageLoader.getImage(oppositeArrows)));
        arrowLabelTwo.setIcon(new ImageIcon(ImageLoader.getImage(oppositeArrows)));
        setUpColors();
        setUpBackground();
        showPanelWelcome();
        hidePanelCurrency();
        hidePanelTemperature();
        setUpCurrencyConverterController();
        setUpTemperatureConverterController();
    }

    private void setUpColors() {
        Map<String, Color> colors = new HashMap<>();
        colors.put("transparent", new Color(0, 0, 0, 0));
        colors.put("darkslategray", new Color(60, 63, 65, 255));
        colors.put("darkcyan", new Color(50, 143, 143, 255));
        colors.put("darkslategreen", new Color(14, 51, 33, 255));
        colors.put("darkgreen", new Color(26, 96, 62, 255));
        colors.put("darkred", new Color(153, 0, 0, 255));
        colors.put("ghostwhite", new Color(244, 246, 252, 255));
        colors.put("dimgray", new Color(102, 102, 102, 255));
        darkCyan = colors.get("darkcyan");
        darksLateGreen = colors.get("darkslategreen");
        darkGreen = colors.get("darkgreen");
        darkRed = colors.get("darkred");
        dimGray = colors.get("dimgray");
        transparent = colors.get("transparent");
        darksLateGray = colors.get("darkslategray");
        ghostWhite = colors.get("ghostwhite");
    }

    private void setUpBackground() {
        setBackground(transparent);
        btnCerrarVentana.setBackground(darksLateGray);
        btnMinimizarVentana.setBackground(darksLateGray);
        btnConversionMoneda.setBackground(darksLateGreen);
        btnConversionTemperatura.setBackground(darksLateGreen);
        btnBienvenida.setBackground(darksLateGreen);
    }

    private void setUpCurrencyConverterController() {
        try {
            APIClient apiClient = APIClient.getInstance();
            PropertiesConfig propertiesConfig = PropertiesConfig.fromFile("config.properties", "fcera.");
            RateParser rateParser = new ExchangeAPIParser();
            JSONCurrencyFileRepository jSONCurrencyFileRepository = new JSONCurrencyFileRepository("", rateParser);
            ExchangeAPIService currencyExchangeRatesService = new ExchangeAPIService(
                    apiClient, propertiesConfig, jSONCurrencyFileRepository, rateParser
            );
            converterController = new CurrencyConverterController(
                    new CurrencyConverter(currencyExchangeRatesService.getExchangeAPI()),
                    currencyExchangeRatesService
            );
            converterController.loadAvailableCurrencies(baseCurrencyComboBox, targetCurrencyComboBox);
        } catch (IOException ex) {
            GlobalLogger.registerLogException(Level.SEVERE, "Error {0}", ex);
            JOptionPane.showMessageDialog(this, "Servicio de divisas no disponible, "
                    + "conéctese a internet o inténtelo más tarde.",
                    "Error al obtener divisas.",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setUpTemperatureConverterController() {
        temperatureConverterController = new TemperatureConverterController(
                new TemperatureConverter(new CelsiusToFarenheit())
        );
        temperatureConverterController.loadComboBoxSymbols(
                baseTemperatureComboBox, targetTemperatureComboBox
        );
    }

    private void showInputErrorMessage() {
        JOptionPane.showMessageDialog(
                this,
                "Error al recibir el valor ingresado: \n"
                + "Verifique que los valores de la lista desplegable sean diferentes.",
                "Valor inválido.",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showPanelWelcome() {
        panelWelcome.setVisible(true);
    }

    private void hidePanelWelcome() {
        panelWelcome.setVisible(false);
    }

    private void showPanelCurrency() {
        mouseAdapterCurrency = btnCalculateCurrencyClickEvent();
        panelCurrency.setVisible(true);
        inputTextCurrency.setVisible(true);
        inputTextCurrency.requestFocus();
        baseCurrencyComboBox.setVisible(true);
        targetCurrencyComboBox.setVisible(true);
        instruccionCampoIngresoDivisa.setVisible(true);
        btnCalculateCurrency.setVisible(true);
        btnCalculateCurrency.setEnabled(false);
        btnCalculateCurrency.setBackground(darkGreen);
        btnCalculateCurrency.setForeground(ghostWhite);
    }

    private MouseAdapter btnCalculateCurrencyClickEvent() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isValidInputValue(inputValue) && Double.parseDouble(inputValue) > 0
                        && !isEqualsComboBoxSelection(baseCurrencyComboBox, targetCurrencyComboBox)) {
                    converterController.setConversion(inputTextCurrency, outputTextCurrency,
                            baseCurrencyComboBox, targetCurrencyComboBox
                    );
                } else {
                    showInputErrorMessage();
                }
                inputTextCurrency.requestFocus();
                btnCalculateCurrency.setEnabled(false);
                btnCalculateCurrency.removeMouseListener(this);
            }
        };
    }

    private void hidePanelCurrency() {
        panelCurrency.setVisible(false);
        inputTextCurrency.setVisible(false);
        instruccionCampoIngresoDivisa.setVisible(false);
        btnCalculateCurrency.setVisible(false);
    }

    private void showPanelTemperature() {
        mouseAdapterTemperature = btnCalculateTemperatureClickEvent();
        panelTemperature.setVisible(true);
        instruccionCampoIngresoTemperatura.setVisible(true);
        inputTextTemperature.setVisible(true);
        inputTextTemperature.requestFocus();
        btnCalculateTemperature.setBackground(darkCyan);
        btnCalculateTemperature.setVisible(true);
        btnCalculateTemperature.setEnabled(false);
        
    }

    private MouseAdapter btnCalculateTemperatureClickEvent() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isValidInputValue(inputValue)
                        && !isEqualsComboBoxSelection(baseTemperatureComboBox, targetTemperatureComboBox)) {
                    temperatureConverterController.setConversion(
                            baseTemperatureComboBox, targetTemperatureComboBox,
                            inputTextTemperature, outputTextTemperature
                    );
                } else {
                    showInputErrorMessage();
                }
                inputTextTemperature.requestFocus();
                btnCalculateTemperature.setEnabled(false);
                btnCalculateTemperature.removeMouseListener(this);
            }
        };
    }

    private void hidePanelTemperature() {
        panelTemperature.setVisible(false);
        instruccionCampoIngresoTemperatura.setVisible(false);
        inputTextTemperature.setVisible(false);
        btnCalculateTemperature.setVisible(false);
    }

    private boolean isValidInputValue(String valorUsuario) {
        return valorUsuario != null
                && !valorUsuario.isEmpty();
    }

    private <T> boolean isEqualsComboBoxSelection(JComboBox<T> baseComboBox,
            JComboBox<T> targetComboBox) {
        int baseComboBoxIndex = baseComboBox.getSelectedIndex();
        int targetComboBoxIndex = targetComboBox.getSelectedIndex();
        return baseComboBox.getItemAt(baseComboBoxIndex)
                .equals(targetComboBox.getItemAt(targetComboBoxIndex));
    }

    private void removeExistingMouseListeners(JTextField jTextField) {
        for (MouseListener listener : jTextField.getMouseListeners()) {
            if (listener instanceof MouseAdapter) {
                jTextField.removeMouseListener(listener);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelMenu = new JPanelImageDrawer("images/FondoMenu.png");
        btnCerrarVentana = new javax.swing.JLabel();
        btnMinimizarVentana = new javax.swing.JLabel();
        btnConversionMoneda = new javax.swing.JLabel();
        btnConversionTemperatura = new javax.swing.JLabel();
        btnBienvenida = new javax.swing.JLabel();
        panelWelcome = new JPanelImageDrawer("images/challengeImage.jpg");
        tituloBienvenida = new javax.swing.JLabel();
        nombreDesarrollador = new javax.swing.JLabel();
        panelCurrency = new JPanelImageDrawer("images/FondoPanelCurrency.png");
        instruccionCampoIngresoDivisa = new javax.swing.JLabel();
        inputTextCurrency = new javax.swing.JTextField();
        lineSeparatorOne = new javax.swing.JSeparator();
        outputTextCurrency = new javax.swing.JTextField();
        lineSeparatorTwo = new javax.swing.JSeparator();
        baseCurrencyComboBox = new javax.swing.JComboBox<>();
        arrowLabelOne = new javax.swing.JLabel();
        targetCurrencyComboBox = new javax.swing.JComboBox<>();
        btnCalculateCurrency = new javax.swing.JLabel();
        panelTemperature = new JPanelImageDrawer("images/FondoPanelTemperatura.png");
        instruccionCampoIngresoTemperatura = new javax.swing.JLabel();
        inputTextTemperature = new javax.swing.JTextField();
        lineSeparatorThree = new javax.swing.JSeparator();
        outputTextTemperature = new javax.swing.JTextField();
        lineSeparatorFour = new javax.swing.JSeparator();
        baseTemperatureComboBox = new javax.swing.JComboBox<>();
        arrowLabelTwo = new javax.swing.JLabel();
        targetTemperatureComboBox = new javax.swing.JComboBox<>();
        btnCalculateTemperature = new javax.swing.JLabel();
        backgroundReference = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMaximumSize(new java.awt.Dimension(975, 510));
        setMinimumSize(new java.awt.Dimension(975, 510));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(975, 510));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        panelMenu.add(btnCerrarVentana, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 40));

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
        panelMenu.add(btnMinimizarVentana, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 50, 40));

        btnConversionMoneda.setBackground(new java.awt.Color(14, 51, 33));
        btnConversionMoneda.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
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
        panelMenu.add(btnConversionMoneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 240, 30));

        btnConversionTemperatura.setBackground(new java.awt.Color(14, 51, 33));
        btnConversionTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
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
        panelMenu.add(btnConversionTemperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 240, 30));

        btnBienvenida.setBackground(new java.awt.Color(14, 51, 33));
        btnBienvenida.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
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
        panelMenu.add(btnBienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 240, 30));

        getContentPane().add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 510));

        panelWelcome.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        panelWelcome.setPreferredSize(new java.awt.Dimension(300, 450));
        panelWelcome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tituloBienvenida.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tituloBienvenida.setForeground(new java.awt.Color(0, 0, 0));
        tituloBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloBienvenida.setText("Bienvenido(a)");
        panelWelcome.add(tituloBienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 720, -1));

        nombreDesarrollador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nombreDesarrollador.setForeground(new java.awt.Color(0, 0, 0));
        nombreDesarrollador.setText("Desarrollado por: EduardoUT");
        panelWelcome.add(nombreDesarrollador, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 340, -1));

        getContentPane().add(panelWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 740, 510));

        panelCurrency.setForeground(new java.awt.Color(244, 246, 252));
        panelCurrency.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelCurrency.setMaximumSize(new java.awt.Dimension(400, 430));
        panelCurrency.setMinimumSize(new java.awt.Dimension(400, 430));
        panelCurrency.setPreferredSize(new java.awt.Dimension(580, 430));
        panelCurrency.setLayout(new java.awt.GridBagLayout());

        instruccionCampoIngresoDivisa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        instruccionCampoIngresoDivisa.setForeground(new java.awt.Color(244, 246, 252));
        instruccionCampoIngresoDivisa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instruccionCampoIngresoDivisa.setText("Ingresa la cantidad de dinero que deseas convertir:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panelCurrency.add(instruccionCampoIngresoDivisa, gridBagConstraints);

        inputTextCurrency.setBackground(new java.awt.Color(70, 174, 124));
        inputTextCurrency.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        inputTextCurrency.setForeground(new java.awt.Color(244, 246, 252));
        inputTextCurrency.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputTextCurrency.setBorder(null);
        inputTextCurrency.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        inputTextCurrency.setMaximumSize(new java.awt.Dimension(200, 32));
        inputTextCurrency.setMinimumSize(new java.awt.Dimension(200, 32));
        inputTextCurrency.setNextFocusableComponent(baseCurrencyComboBox);
        inputTextCurrency.setPreferredSize(new java.awt.Dimension(200, 32));
        inputTextCurrency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputTextCurrencyFocusLost(evt);
            }
        });
        inputTextCurrency.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inputTextCurrencyKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 10);
        panelCurrency.add(inputTextCurrency, gridBagConstraints);

        lineSeparatorOne.setBackground(new java.awt.Color(244, 246, 252));
        lineSeparatorOne.setForeground(new java.awt.Color(244, 246, 252));
        lineSeparatorOne.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lineSeparatorOne.setMaximumSize(new java.awt.Dimension(200, 3));
        lineSeparatorOne.setMinimumSize(new java.awt.Dimension(200, 3));
        lineSeparatorOne.setOpaque(true);
        lineSeparatorOne.setPreferredSize(new java.awt.Dimension(200, 3));
        lineSeparatorOne.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelCurrency.add(lineSeparatorOne, gridBagConstraints);

        outputTextCurrency.setEditable(false);
        outputTextCurrency.setBackground(new java.awt.Color(70, 174, 124));
        outputTextCurrency.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        outputTextCurrency.setForeground(new java.awt.Color(244, 246, 252));
        outputTextCurrency.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        outputTextCurrency.setBorder(null);
        outputTextCurrency.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        outputTextCurrency.setFocusable(false);
        outputTextCurrency.setMaximumSize(new java.awt.Dimension(200, 32));
        outputTextCurrency.setMinimumSize(new java.awt.Dimension(200, 32));
        outputTextCurrency.setPreferredSize(new java.awt.Dimension(200, 32));
        outputTextCurrency.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelCurrency.add(outputTextCurrency, gridBagConstraints);

        lineSeparatorTwo.setBackground(new java.awt.Color(244, 246, 252));
        lineSeparatorTwo.setForeground(new java.awt.Color(244, 246, 252));
        lineSeparatorTwo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lineSeparatorTwo.setMaximumSize(new java.awt.Dimension(200, 3));
        lineSeparatorTwo.setMinimumSize(new java.awt.Dimension(200, 3));
        lineSeparatorTwo.setOpaque(true);
        lineSeparatorTwo.setPreferredSize(new java.awt.Dimension(200, 3));
        lineSeparatorTwo.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelCurrency.add(lineSeparatorTwo, gridBagConstraints);

        baseCurrencyComboBox.setBackground(new java.awt.Color(70, 174, 124));
        baseCurrencyComboBox.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        baseCurrencyComboBox.setForeground(new java.awt.Color(244, 246, 252));
        baseCurrencyComboBox.setNextFocusableComponent(targetCurrencyComboBox);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 10);
        panelCurrency.add(baseCurrencyComboBox, gridBagConstraints);

        arrowLabelOne.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        arrowLabelOne.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 15, 0);
        panelCurrency.add(arrowLabelOne, gridBagConstraints);

        targetCurrencyComboBox.setBackground(new java.awt.Color(70, 174, 124));
        targetCurrencyComboBox.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        targetCurrencyComboBox.setForeground(new java.awt.Color(244, 246, 252));
        targetCurrencyComboBox.setMinimumSize(new java.awt.Dimension(72, 32));
        targetCurrencyComboBox.setPreferredSize(new java.awt.Dimension(72, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelCurrency.add(targetCurrencyComboBox, gridBagConstraints);

        btnCalculateCurrency.setBackground(new java.awt.Color(28, 113, 81));
        btnCalculateCurrency.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCalculateCurrency.setForeground(new java.awt.Color(244, 246, 252));
        btnCalculateCurrency.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCalculateCurrency.setText("Convertir");
        btnCalculateCurrency.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCalculateCurrency.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCalculateCurrency.setOpaque(true);
        btnCalculateCurrency.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCalculateCurrencyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCalculateCurrencyMouseExited(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 15, 0);
        panelCurrency.add(btnCalculateCurrency, gridBagConstraints);

        getContentPane().add(panelCurrency, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 740, 510));

        panelTemperature.setMaximumSize(new java.awt.Dimension(740, 510));
        panelTemperature.setMinimumSize(new java.awt.Dimension(740, 510));
        panelTemperature.setLayout(new java.awt.GridBagLayout());

        instruccionCampoIngresoTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        instruccionCampoIngresoTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        instruccionCampoIngresoTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instruccionCampoIngresoTemperatura.setText("Ingresa el valor de Temperatura que deseas convertir:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelTemperature.add(instruccionCampoIngresoTemperatura, gridBagConstraints);

        inputTextTemperature.setBackground(new java.awt.Color(21, 73, 255));
        inputTextTemperature.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        inputTextTemperature.setForeground(new java.awt.Color(244, 246, 252));
        inputTextTemperature.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputTextTemperature.setBorder(null);
        inputTextTemperature.setMaximumSize(new java.awt.Dimension(200, 32));
        inputTextTemperature.setMinimumSize(new java.awt.Dimension(200, 32));
        inputTextTemperature.setNextFocusableComponent(baseTemperatureComboBox);
        inputTextTemperature.setOpaque(true);
        inputTextTemperature.setPreferredSize(new java.awt.Dimension(200, 32));
        inputTextTemperature.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputTextTemperatureFocusLost(evt);
            }
        });
        inputTextTemperature.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inputTextTemperatureKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 10);
        panelTemperature.add(inputTextTemperature, gridBagConstraints);

        lineSeparatorThree.setBackground(new java.awt.Color(244, 246, 252));
        lineSeparatorThree.setForeground(new java.awt.Color(244, 246, 252));
        lineSeparatorThree.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lineSeparatorThree.setMaximumSize(new java.awt.Dimension(200, 3));
        lineSeparatorThree.setMinimumSize(new java.awt.Dimension(200, 3));
        lineSeparatorThree.setOpaque(true);
        lineSeparatorThree.setPreferredSize(new java.awt.Dimension(200, 3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelTemperature.add(lineSeparatorThree, gridBagConstraints);

        outputTextTemperature.setEditable(false);
        outputTextTemperature.setBackground(new java.awt.Color(21, 73, 255));
        outputTextTemperature.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        outputTextTemperature.setForeground(new java.awt.Color(244, 246, 252));
        outputTextTemperature.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        outputTextTemperature.setBorder(null);
        outputTextTemperature.setFocusable(false);
        outputTextTemperature.setMaximumSize(new java.awt.Dimension(200, 32));
        outputTextTemperature.setMinimumSize(new java.awt.Dimension(200, 32));
        outputTextTemperature.setPreferredSize(new java.awt.Dimension(200, 32));
        outputTextTemperature.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelTemperature.add(outputTextTemperature, gridBagConstraints);

        lineSeparatorFour.setBackground(new java.awt.Color(244, 246, 252));
        lineSeparatorFour.setForeground(new java.awt.Color(244, 246, 252));
        lineSeparatorFour.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lineSeparatorFour.setMaximumSize(new java.awt.Dimension(200, 3));
        lineSeparatorFour.setMinimumSize(new java.awt.Dimension(200, 3));
        lineSeparatorFour.setOpaque(true);
        lineSeparatorFour.setPreferredSize(new java.awt.Dimension(200, 3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelTemperature.add(lineSeparatorFour, gridBagConstraints);

        baseTemperatureComboBox.setBackground(new java.awt.Color(21, 73, 255));
        baseTemperatureComboBox.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        baseTemperatureComboBox.setForeground(new java.awt.Color(244, 246, 252));
        baseTemperatureComboBox.setNextFocusableComponent(targetTemperatureComboBox);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 10);
        panelTemperature.add(baseTemperatureComboBox, gridBagConstraints);

        arrowLabelTwo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 15, 0);
        panelTemperature.add(arrowLabelTwo, gridBagConstraints);

        targetTemperatureComboBox.setBackground(new java.awt.Color(21, 73, 255));
        targetTemperatureComboBox.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        targetTemperatureComboBox.setForeground(new java.awt.Color(244, 246, 252));
        targetTemperatureComboBox.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelTemperature.add(targetTemperatureComboBox, gridBagConstraints);

        btnCalculateTemperature.setBackground(new java.awt.Color(50, 143, 143));
        btnCalculateTemperature.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCalculateTemperature.setForeground(new java.awt.Color(244, 246, 252));
        btnCalculateTemperature.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCalculateTemperature.setText("Convertir");
        btnCalculateTemperature.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCalculateTemperature.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCalculateTemperature.setOpaque(true);
        btnCalculateTemperature.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCalculateTemperatureMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCalculateTemperatureMouseExited(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 15, 0);
        panelTemperature.add(btnCalculateTemperature, gridBagConstraints);

        getContentPane().add(panelTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 740, 510));

        backgroundReference.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                backgroundReferenceMouseDragged(evt);
            }
        });
        backgroundReference.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                backgroundReferenceMousePressed(evt);
            }
        });
        getContentPane().add(backgroundReference, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 510));

        setSize(new java.awt.Dimension(975, 510));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void inputTextCurrencyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextCurrencyKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car != '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_inputTextCurrencyKeyTyped

    private void btnConversionMonedaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionMonedaMouseClicked
        evt.consume();
        if (panelWelcome.isVisible()) {
            hidePanelWelcome();
        }
        if (panelTemperature.isVisible()) {
            hidePanelTemperature();
        }
        showPanelCurrency();
    }//GEN-LAST:event_btnConversionMonedaMouseClicked

    private void btnConversionTemperaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionTemperaturaMouseClicked
        evt.consume();
        if (panelWelcome.isVisible()) {
            hidePanelWelcome();
        }
        if (panelCurrency.isVisible()) {
            hidePanelCurrency();
        }
        showPanelTemperature();
    }//GEN-LAST:event_btnConversionTemperaturaMouseClicked

    private void btnBienvenidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBienvenidaMouseClicked
        evt.consume();
        if (panelCurrency.isVisible()) {
            hidePanelCurrency();
        }
        if (panelTemperature.isVisible()) {
            hidePanelTemperature();
        }
        showPanelWelcome();
    }//GEN-LAST:event_btnBienvenidaMouseClicked

    private void btnCerrarVentanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseClicked
        evt.consume();
        System.exit(0);
    }//GEN-LAST:event_btnCerrarVentanaMouseClicked

    private void btnMinimizarVentanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizarVentanaMouseClicked
        evt.consume();
        setExtendedState(ICONIFIED);
    }//GEN-LAST:event_btnMinimizarVentanaMouseClicked

    private void inputTextTemperatureKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextTemperatureKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car != '.') && (car != '-')) {
            evt.consume();
        }
    }//GEN-LAST:event_inputTextTemperatureKeyTyped

    private void btnCerrarVentanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseEntered
        evt.consume();
        btnCerrarVentana.setBackground(darkRed);
    }//GEN-LAST:event_btnCerrarVentanaMouseEntered

    private void btnCerrarVentanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseExited
        evt.consume();
        btnCerrarVentana.setBackground(darksLateGray);
    }//GEN-LAST:event_btnCerrarVentanaMouseExited

    private void btnMinimizarVentanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizarVentanaMouseEntered
        evt.consume();
        btnMinimizarVentana.setBackground(dimGray);
    }//GEN-LAST:event_btnMinimizarVentanaMouseEntered

    private void btnMinimizarVentanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizarVentanaMouseExited
        evt.consume();
        btnMinimizarVentana.setBackground(darksLateGray);
    }//GEN-LAST:event_btnMinimizarVentanaMouseExited

    private void btnConversionMonedaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionMonedaMouseEntered
        evt.consume();
        btnConversionMoneda.setBackground(darkGreen);

    }//GEN-LAST:event_btnConversionMonedaMouseEntered

    private void btnConversionMonedaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionMonedaMouseExited
        evt.consume();
        btnConversionMoneda.setBackground(darksLateGreen);
    }//GEN-LAST:event_btnConversionMonedaMouseExited

    private void btnConversionTemperaturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionTemperaturaMouseEntered
        evt.consume();
        btnConversionTemperatura.setBackground(darkGreen);
    }//GEN-LAST:event_btnConversionTemperaturaMouseEntered

    private void btnConversionTemperaturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionTemperaturaMouseExited
        evt.consume();
        btnConversionTemperatura.setBackground(darksLateGreen);
    }//GEN-LAST:event_btnConversionTemperaturaMouseExited

    private void btnBienvenidaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBienvenidaMouseEntered
        evt.consume();
        btnBienvenida.setBackground(darkGreen);
    }//GEN-LAST:event_btnBienvenidaMouseEntered

    private void btnBienvenidaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBienvenidaMouseExited
        evt.consume();
        btnBienvenida.setBackground(darksLateGreen);
    }//GEN-LAST:event_btnBienvenidaMouseExited

    private void btnCalculateTemperatureMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalculateTemperatureMouseEntered
        if (btnCalculateTemperature.isEnabled()) {
            evt.consume();
            btnCalculateTemperature.setBackground(Color.BLACK);
        }
    }//GEN-LAST:event_btnCalculateTemperatureMouseEntered

    private void btnCalculateTemperatureMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalculateTemperatureMouseExited
        if (btnCalculateTemperature.isEnabled()) {
            evt.consume();
            btnCalculateTemperature.setBackground(darkCyan);
        }
    }//GEN-LAST:event_btnCalculateTemperatureMouseExited

    private void inputTextTemperatureFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTextTemperatureFocusLost
        inputValue = inputTextTemperature.getText();
        removeExistingMouseListeners(inputTextTemperature);
        if (esValorDecimalOpcionalNegativo(inputValue)) {
            evt.getID();
            btnCalculateTemperature.setEnabled(true);
            btnCalculateTemperature.addMouseListener(mouseAdapterTemperature);
        } else {
            btnCalculateTemperature.setEnabled(false);
        }
    }//GEN-LAST:event_inputTextTemperatureFocusLost

    private void inputTextCurrencyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputTextCurrencyFocusLost
        inputValue = inputTextCurrency.getText();
        removeExistingMouseListeners(inputTextCurrency);
        if (esValorDecimal(inputValue)) {
            evt.getID();
            btnCalculateCurrency.addMouseListener(mouseAdapterCurrency);
            btnCalculateCurrency.setEnabled(true);
        } else {
            btnCalculateCurrency.setEnabled(false);
        }
    }//GEN-LAST:event_inputTextCurrencyFocusLost

    private void btnCalculateCurrencyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalculateCurrencyMouseEntered
        if (btnCalculateCurrency.isEnabled()) {
            evt.consume();
            btnCalculateCurrency.setBackground(darksLateGreen);
        }
    }//GEN-LAST:event_btnCalculateCurrencyMouseEntered

    private void btnCalculateCurrencyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalculateCurrencyMouseExited
        if (btnCalculateCurrency.isEnabled()) {
            evt.consume();
            btnCalculateCurrency.setBackground(darkGreen);
        }
    }//GEN-LAST:event_btnCalculateCurrencyMouseExited

    private void backgroundReferenceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backgroundReferenceMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_backgroundReferenceMousePressed

    private void backgroundReferenceMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backgroundReferenceMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_backgroundReferenceMouseDragged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel arrowLabelOne;
    private javax.swing.JLabel arrowLabelTwo;
    private javax.swing.JLabel backgroundReference;
    private javax.swing.JComboBox<CurrencyUnit> baseCurrencyComboBox;
    private javax.swing.JComboBox<TemperatureSymbol> baseTemperatureComboBox;
    private javax.swing.JLabel btnBienvenida;
    private javax.swing.JLabel btnCalculateCurrency;
    private javax.swing.JLabel btnCalculateTemperature;
    private javax.swing.JLabel btnCerrarVentana;
    private javax.swing.JLabel btnConversionMoneda;
    private javax.swing.JLabel btnConversionTemperatura;
    private javax.swing.JLabel btnMinimizarVentana;
    private javax.swing.JTextField inputTextCurrency;
    private javax.swing.JTextField inputTextTemperature;
    private javax.swing.JLabel instruccionCampoIngresoDivisa;
    private javax.swing.JLabel instruccionCampoIngresoTemperatura;
    private javax.swing.JSeparator lineSeparatorFour;
    private javax.swing.JSeparator lineSeparatorOne;
    private javax.swing.JSeparator lineSeparatorThree;
    private javax.swing.JSeparator lineSeparatorTwo;
    private javax.swing.JLabel nombreDesarrollador;
    private javax.swing.JTextField outputTextCurrency;
    private javax.swing.JTextField outputTextTemperature;
    private javax.swing.JPanel panelCurrency;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelTemperature;
    private javax.swing.JPanel panelWelcome;
    private javax.swing.JComboBox<CurrencyUnit> targetCurrencyComboBox;
    private javax.swing.JComboBox<TemperatureSymbol> targetTemperatureComboBox;
    private javax.swing.JLabel tituloBienvenida;
    // End of variables declaration//GEN-END:variables
    class JPanelImageDrawer extends JPanel {

        private String fileName;

        public JPanelImageDrawer(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void paint(Graphics g) {
            Image imagen = ImageLoader.getImage(fileName);
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }

    class JLabelImageDrawer extends JLabel {

        private String fileName;

        public JLabelImageDrawer(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void paint(Graphics g) {
            Image imagen = ImageLoader.getImage(fileName);
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }
}
