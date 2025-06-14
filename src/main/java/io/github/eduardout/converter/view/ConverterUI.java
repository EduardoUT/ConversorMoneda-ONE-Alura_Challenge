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

import static io.github.eduardout.converter.GlobalLogger.*;
import io.github.eduardout.converter.currency.CurrencyConverter;
import io.github.eduardout.converter.currency.CurrencyConverterController;
import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.RateProviderService;
import io.github.eduardout.converter.currency.config.PropertiesConfig;
import io.github.eduardout.converter.currency.provider.*;
import io.github.eduardout.converter.currency.repository.JSONCurrencyRepository;
import io.github.eduardout.converter.temperature.CelsiusToFarenheit;
import io.github.eduardout.converter.temperature.TemperatureConverter;
import io.github.eduardout.converter.temperature.TemperatureConverterController;
import io.github.eduardout.converter.temperature.TemperatureSymbol;
import io.github.eduardout.converter.util.ExchangeAPIParser;
import io.github.eduardout.converter.util.ImageLoader;
import io.github.eduardout.converter.util.NBPExchangeRatesParser;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author EduardoUT
 */
public final class ConverterUI extends javax.swing.JFrame {

    private int xMouse;
    private int yMouse;
    private String inputValue;
    private transient CurrencyConverterController converterController;
    private transient TemperatureConverterController temperatureConverterController;
    private RateProviderService rateProviderService;
    private Color transparent;
    private Color darkRed;
    private Color darkGreen;
    private Color darksLateGreen;
    private Color transparentBlack;
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
        setUpCurrencyConverterController();
        setUpTemperatureConverterController();
        showPanelWelcome();
        hidePanelCurrency();
        hidePanelCurrencyError();
        hidePanelTemperature();
    }

    private void setUpColors() {
        Map<String, Color> colors = getColors();
        darkCyan = colors.get("darkcyan");
        darksLateGreen = colors.get("darkslategreen");
        darkGreen = colors.get("darkgreen");
        transparentBlack = colors.get("transparentBlack");
        darkRed = colors.get("darkred");
        dimGray = colors.get("dimgray");
        transparent = colors.get("transparent");
        darksLateGray = colors.get("darkslategray");
        ghostWhite = colors.get("ghostwhite");
    }

    private static Map<String, Color> getColors() {
        Map<String, Color> colors = new HashMap<>();
        colors.put("transparent", new Color(0, 0, 0, 0));
        colors.put("darkslategray", new Color(60, 63, 65, 255));
        colors.put("darkcyan", new Color(50, 143, 143, 255));
        colors.put("darkslategreen", new Color(14, 51, 33, 255));
        colors.put("darkgreen", new Color(26, 96, 62, 255));
        colors.put("darkred", new Color(153, 0, 0, 255));
        colors.put("ghostwhite", new Color(244, 246, 252, 255));
        colors.put("dimgray", new Color(102, 102, 102, 255));
        colors.put("transparentBlack", new Color(32, 26, 29, 100));
        return colors;
    }

    private void setUpBackground() {
        setBackground(transparent);
        btnCerrarVentana.setBackground(darksLateGray);
        btnMinimizarVentana.setBackground(darksLateGray);
        btnConversionMoneda.setBackground(darksLateGreen);
        btnConversionTemperatura.setBackground(darksLateGreen);
        errorMessageLabel.setBackground(transparentBlack);
        btnBienvenida.setBackground(darksLateGreen);
        btnTryAgain.setBackground(darkGreen);
    }

    private void setUpCurrencyConverterController() {
        try {
            HttpClient httpClient = HttpClient.getInstance();
            PropertiesConfig propertiesConfig = PropertiesConfig.fromFile("config.properties");
            Set<RateProvider> rateProviders = new LinkedHashSet<>();
            rateProviders.add(new ExchangeAPI(httpClient, propertiesConfig, "free-currency-exchange-rates-api.", new ExchangeAPIParser()));
            rateProviders.add(new NBPExchangeRates(httpClient, propertiesConfig, "nbp-web-api.", new NBPExchangeRatesParser()));
            RateProviderRegistry rateProviderRegistry = new RateProviderRegistry(rateProviders);
            rateProviderService = new RateProviderService(
                    rateProviderRegistry, new JSONCurrencyRepository("")
            );
            converterController = new CurrencyConverterController(
                    new CurrencyConverter(rateProviderService),
                    rateProviderService
            );
            loadCurrencyConverterUI(rateProviderService);
        } catch (IOException ex) {
            registerLogException(Level.SEVERE, "Error: {0} ", ex);
            loadCurrencyConverterUI(rateProviderService);
        }
    }

    /**
     * Load the UI interface of the currency converter according to the
     * currencyRates returned form the rate provider, validates if the rate
     * provider has returned currency rates, if not it means that all the rate
     * providers are unavailable due to network connection or the repository has
     * no data, so we load a panel with the error message and a button to try to
     * reload the data.
     *
     * @param rateProviderService The service class to retrieve all the related
     * info from the rate provider.
     */
    private void loadCurrencyConverterUI(RateProviderService rateProviderService) {
        if (converterController.hasAvailableCurrencyUnits()) {
            showPanelCurrency();
            hidePanelCurrencyError();
            converterController.loadAvailableCurrencies(baseCurrencyComboBox, targetCurrencyComboBox);
            rateProviderService.updateCurrencyRates();
        } else {
            hidePanelCurrency();
            showPanelCurrencyError();
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

    private void showPanelCurrencyError() {
        removeExistingMouseListeners(inputTextCurrency);
        panelCurrencyError.setVisible(true);
        btnTryAgain.setBackground(darkGreen);
        btnTryAgain.setForeground(ghostWhite);

    }

    private MouseAdapter btnCalculateCurrencyClickEvent() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isValidInputValue(inputValue) && Double.parseDouble(inputValue) > 0
                        && hasEqualsComboBoxSelectedIndex(baseCurrencyComboBox, targetCurrencyComboBox)) {
                    try {
                        converterController.setConversion(
                                inputTextCurrency,
                                outputTextCurrency,
                                baseCurrencyComboBox,
                                targetCurrencyComboBox
                        );
                    } catch (IllegalStateException ex) {
                        registerLog(Level.SEVERE, "Servicio de divisas no disponible: {0} " + ex);
                        loadCurrencyConverterUI(rateProviderService);
                    }
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

    private void hidePanelCurrencyError() {
        panelCurrencyError.setVisible(false);
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
                        && hasEqualsComboBoxSelectedIndex(baseTemperatureComboBox, targetTemperatureComboBox)) {
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

    private boolean isValidInputValue(String userValue) {
        return userValue != null
                && !userValue.isEmpty();
    }

    private <T> boolean hasEqualsComboBoxSelectedIndex(JComboBox<T> baseComboBox,
            JComboBox<T> targetComboBox) {
        int baseComboBoxIndex = baseComboBox.getSelectedIndex();
        int targetComboBoxIndex = targetComboBox.getSelectedIndex();
        return !baseComboBox.getItemAt(baseComboBoxIndex)
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

        panelMenu = new JPanelImageDrawer("images/panelMenu.png");
        btnCerrarVentana = new javax.swing.JLabel();
        btnMinimizarVentana = new javax.swing.JLabel();
        btnConversionMoneda = new javax.swing.JLabel();
        btnConversionTemperatura = new javax.swing.JLabel();
        btnBienvenida = new javax.swing.JLabel();
        panelWelcome = new JPanelImageDrawer("images/fondoBienvenida.png");
        tituloBienvenida = new javax.swing.JLabel();
        nombreDesarrollador = new javax.swing.JLabel();
        panelCurrency = new JPanelImageDrawer("images/panelCurrency.png");
        instruccionCampoIngresoDivisa = new javax.swing.JLabel();
        inputTextCurrency = new javax.swing.JTextField();
        lineSeparatorOne = new javax.swing.JSeparator();
        outputTextCurrency = new javax.swing.JTextField();
        lineSeparatorTwo = new javax.swing.JSeparator();
        baseCurrencyComboBox = new javax.swing.JComboBox<>();
        arrowLabelOne = new javax.swing.JLabel();
        targetCurrencyComboBox = new javax.swing.JComboBox<>();
        btnCalculateCurrency = new javax.swing.JLabel();
        panelCurrencyError = new JPanelImageDrawer("images/unavailableCurrency.png");
        errorMessageLabel = new javax.swing.JLabel();
        btnTryAgain = new javax.swing.JLabel();
        panelTemperature = new JPanelImageDrawer("images/panelTemperatura.png");
        instruccionCampoIngresoTemperatura = new javax.swing.JLabel();
        inputTextTemperature = new javax.swing.JTextField();
        lineSeparatorThree = new javax.swing.JSeparator();
        outputTextTemperature = new javax.swing.JTextField();
        lineSeparatorFour = new javax.swing.JSeparator();
        baseTemperatureComboBox = new javax.swing.JComboBox<>();
        arrowLabelTwo = new javax.swing.JLabel();
        targetTemperatureComboBox = new javax.swing.JComboBox<>();
        btnCalculateTemperature = new javax.swing.JLabel();
        mouseHoldClicReference = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMaximumSize(new java.awt.Dimension(980, 510));
        setMinimumSize(new java.awt.Dimension(980, 510));
        setUndecorated(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelMenu.setMaximumSize(new java.awt.Dimension(240, 510));
        panelMenu.setMinimumSize(new java.awt.Dimension(240, 510));
        panelMenu.setPreferredSize(new java.awt.Dimension(240, 510));
        panelMenu.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 41;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelMenu.add(btnCerrarVentana, gridBagConstraints);

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelMenu.add(btnMinimizarVentana, gridBagConstraints);

        btnConversionMoneda.setBackground(new java.awt.Color(14, 51, 33));
        btnConversionMoneda.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnConversionMoneda.setForeground(new java.awt.Color(244, 246, 252));
        btnConversionMoneda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnConversionMoneda.setText("Conversor de Moneda");
        btnConversionMoneda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConversionMoneda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConversionMoneda.setMaximumSize(new java.awt.Dimension(240, 25));
        btnConversionMoneda.setMinimumSize(new java.awt.Dimension(240, 25));
        btnConversionMoneda.setOpaque(true);
        btnConversionMoneda.setPreferredSize(new java.awt.Dimension(240, 25));
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(270, 0, 0, 0);
        panelMenu.add(btnConversionMoneda, gridBagConstraints);

        btnConversionTemperatura.setBackground(new java.awt.Color(14, 51, 33));
        btnConversionTemperatura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnConversionTemperatura.setForeground(new java.awt.Color(244, 246, 252));
        btnConversionTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnConversionTemperatura.setText("Conversor de Temperatura");
        btnConversionTemperatura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConversionTemperatura.setMaximumSize(new java.awt.Dimension(240, 25));
        btnConversionTemperatura.setMinimumSize(new java.awt.Dimension(240, 25));
        btnConversionTemperatura.setOpaque(true);
        btnConversionTemperatura.setPreferredSize(new java.awt.Dimension(240, 25));
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelMenu.add(btnConversionTemperatura, gridBagConstraints);

        btnBienvenida.setBackground(new java.awt.Color(14, 51, 33));
        btnBienvenida.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBienvenida.setForeground(new java.awt.Color(244, 246, 252));
        btnBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnBienvenida.setText("Bienvenida");
        btnBienvenida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBienvenida.setMaximumSize(new java.awt.Dimension(240, 25));
        btnBienvenida.setMinimumSize(new java.awt.Dimension(240, 25));
        btnBienvenida.setOpaque(true);
        btnBienvenida.setPreferredSize(new java.awt.Dimension(240, 25));
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 90, 0);
        panelMenu.add(btnBienvenida, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panelMenu, gridBagConstraints);

        panelWelcome.setMaximumSize(new java.awt.Dimension(740, 510));
        panelWelcome.setMinimumSize(new java.awt.Dimension(740, 510));
        panelWelcome.setPreferredSize(new java.awt.Dimension(740, 510));
        panelWelcome.setLayout(new java.awt.GridBagLayout());

        tituloBienvenida.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tituloBienvenida.setForeground(new java.awt.Color(0, 0, 0));
        tituloBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloBienvenida.setText("Bienvenido(a)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        panelWelcome.add(tituloBienvenida, gridBagConstraints);

        nombreDesarrollador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nombreDesarrollador.setForeground(new java.awt.Color(0, 0, 0));
        nombreDesarrollador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDesarrollador.setText("Desarrollado por: EduardoUT");
        nombreDesarrollador.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 155;
        gridBagConstraints.insets = new java.awt.Insets(408, 0, 0, 0);
        panelWelcome.add(nombreDesarrollador, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panelWelcome, gridBagConstraints);

        panelCurrency.setForeground(new java.awt.Color(244, 246, 252));
        panelCurrency.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelCurrency.setMaximumSize(new java.awt.Dimension(740, 510));
        panelCurrency.setMinimumSize(new java.awt.Dimension(740, 510));
        panelCurrency.setPreferredSize(new java.awt.Dimension(740, 510));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panelCurrency, gridBagConstraints);

        panelCurrencyError.setBackground(java.awt.Color.black);
        panelCurrencyError.setMaximumSize(new java.awt.Dimension(740, 510));
        panelCurrencyError.setMinimumSize(new java.awt.Dimension(740, 510));
        panelCurrencyError.setPreferredSize(new java.awt.Dimension(740, 510));
        panelCurrencyError.setLayout(new java.awt.GridBagLayout());

        errorMessageLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        errorMessageLabel.setForeground(new java.awt.Color(244, 246, 252));
        errorMessageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        errorMessageLabel.setText("<html><head><style>p{text-align:center;padding:10px;}</style></head><p>Servicio de divisas no disponible, conectese a internet o inténtelo más tarde...</p></html>");
        errorMessageLabel.setFocusable(false);
        errorMessageLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        errorMessageLabel.setMaximumSize(new Dimension(panelCurrencyError.getMinimumSize().width - 30, 70));
        errorMessageLabel.setMinimumSize(new Dimension(panelCurrencyError.getMinimumSize().width - 30, 70));
        errorMessageLabel.setOpaque(true);
        errorMessageLabel.setPreferredSize(new Dimension(panelCurrencyError.getMinimumSize().width - 30, 70));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 15, 0);
        panelCurrencyError.add(errorMessageLabel, gridBagConstraints);

        btnTryAgain.setBackground(new java.awt.Color(28, 113, 81));
        btnTryAgain.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnTryAgain.setForeground(new java.awt.Color(244, 246, 252));
        btnTryAgain.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnTryAgain.setText("Reintentar");
        btnTryAgain.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTryAgain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTryAgain.setOpaque(true);
        btnTryAgain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTryAgainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTryAgainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTryAgainMouseExited(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 15, 0);
        panelCurrencyError.add(btnTryAgain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panelCurrencyError, gridBagConstraints);

        panelTemperature.setMaximumSize(new java.awt.Dimension(740, 510));
        panelTemperature.setMinimumSize(new java.awt.Dimension(740, 510));
        panelTemperature.setPreferredSize(new java.awt.Dimension(740, 510));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panelTemperature, gridBagConstraints);

        mouseHoldClicReference.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mouseHoldClicReferenceMouseDragged(evt);
            }
        });
        mouseHoldClicReference.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mouseHoldClicReferenceMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 980;
        gridBagConstraints.ipady = 510;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(mouseHoldClicReference, gridBagConstraints);

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
        loadCurrencyConverterUI(rateProviderService);
    }//GEN-LAST:event_btnConversionMonedaMouseClicked

    private void btnConversionTemperaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConversionTemperaturaMouseClicked
        evt.consume();
        if (panelWelcome.isVisible()) {
            hidePanelWelcome();
        }
        if (panelCurrency.isVisible()) {
            hidePanelCurrency();
        }
        if (panelCurrencyError.isVisible()) {
            hidePanelCurrencyError();
        }
        showPanelTemperature();
    }//GEN-LAST:event_btnConversionTemperaturaMouseClicked

    private void btnBienvenidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBienvenidaMouseClicked
        evt.consume();
        if (panelCurrency.isVisible()) {
            hidePanelCurrency();
        }
        if (panelCurrencyError.isVisible()) {
            hidePanelCurrencyError();
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

    private void btnTryAgainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTryAgainMouseClicked
        evt.consume();
        loadCurrencyConverterUI(rateProviderService);
    }//GEN-LAST:event_btnTryAgainMouseClicked

    private void btnTryAgainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTryAgainMouseEntered
        evt.consume();
        btnTryAgain.setBackground(darksLateGreen);
    }//GEN-LAST:event_btnTryAgainMouseEntered

    private void btnTryAgainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTryAgainMouseExited
        evt.consume();
        btnTryAgain.setBackground(darkGreen);
    }//GEN-LAST:event_btnTryAgainMouseExited

    private void mouseHoldClicReferenceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseHoldClicReferenceMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_mouseHoldClicReferenceMousePressed

    private void mouseHoldClicReferenceMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseHoldClicReferenceMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_mouseHoldClicReferenceMouseDragged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel arrowLabelOne;
    private javax.swing.JLabel arrowLabelTwo;
    private javax.swing.JComboBox<CurrencyUnit> baseCurrencyComboBox;
    private javax.swing.JComboBox<TemperatureSymbol> baseTemperatureComboBox;
    private javax.swing.JLabel btnBienvenida;
    private javax.swing.JLabel btnCalculateCurrency;
    private javax.swing.JLabel btnCalculateTemperature;
    private javax.swing.JLabel btnCerrarVentana;
    private javax.swing.JLabel btnConversionMoneda;
    private javax.swing.JLabel btnConversionTemperatura;
    private javax.swing.JLabel btnMinimizarVentana;
    private javax.swing.JLabel btnTryAgain;
    private javax.swing.JLabel errorMessageLabel;
    private javax.swing.JTextField inputTextCurrency;
    private javax.swing.JTextField inputTextTemperature;
    private javax.swing.JLabel instruccionCampoIngresoDivisa;
    private javax.swing.JLabel instruccionCampoIngresoTemperatura;
    private javax.swing.JSeparator lineSeparatorFour;
    private javax.swing.JSeparator lineSeparatorOne;
    private javax.swing.JSeparator lineSeparatorThree;
    private javax.swing.JSeparator lineSeparatorTwo;
    private javax.swing.JLabel mouseHoldClicReference;
    private javax.swing.JLabel nombreDesarrollador;
    private javax.swing.JTextField outputTextCurrency;
    private javax.swing.JTextField outputTextTemperature;
    private javax.swing.JPanel panelCurrency;
    private javax.swing.JPanel panelCurrencyError;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelTemperature;
    private javax.swing.JPanel panelWelcome;
    private javax.swing.JComboBox<CurrencyUnit> targetCurrencyComboBox;
    private javax.swing.JComboBox<TemperatureSymbol> targetTemperatureComboBox;
    private javax.swing.JLabel tituloBienvenida;
    // End of variables declaration//GEN-END:variables
    static class JPanelImageDrawer extends JPanel {

        private final String fileName;

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
}
