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
package io.github.eduardout.converter;

import io.github.eduardout.converter.currency.provider.RateProvider;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Clase para la configuración e inizialización del archivo de registro y
 * seguimiento de errores.
 *
 * @author EduardoUT
 */
public class GlobalLogger {

    private static final Logger GLOBAL_LOGGER = Logger.getLogger("io.github.eduardout.converter");
    private static final String FILE_PATH = "logs/";
    private static final Map<String, Long> lastHealthCheckLogs = new ConcurrentHashMap<>();
    private static final long DEFAULT_INTERVAL = 30000;
    private static final String UTF_8 = "UTF-8";

    private GlobalLogger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Initialize the logger in the console and the file.
     *
     * @throws java.io.IOException Throws this exception if a problem accours
     * during the initialization of the logger.
     */
    protected static void setUpLoggerConfigurationFile() throws IOException {
        LoggerConfigurationFile loggerConfigFile = new LoggerConfigurationFile();
        loggerConfigFile.setUpLoggerFile();
    }

    /**
     * Logs the level, message and exception object cathed.
     *
     * @param level Severity level of the log.
     * @param message Personalized message detail to log.
     * @param e Exception object catched in the error.
     */
    public static void registerLogException(Level level, String message, Exception e) {
        GLOBAL_LOGGER.log(level, message, writeErrorMessage(e));
    }

    /**
     * Logs all the details of he exception with its stackTrace, ideal to
     * debugging and full monitoring.
     *
     * @param level Severity level of the log.
     * @param message Personalized message detail to log.
     * @param e Exception object catched in the error.
     */
    public static void registerLogExceptionWithStackTrace(Level level, String message, Exception e) {
        GLOBAL_LOGGER.log(level, message, printStackTrace(e));
    }

    /**
     * Logs to describe events or informs.
     *
     * @param level Level of severity.
     * @param message Content of the message.
     */
    public static void registerLog(Level level, String message) {
        GLOBAL_LOGGER.log(level, message);
    }

    /**
     * Logs and checks the frequency of a log info message that is called
     * multiple times in a rate provider, so handles the interval of time of 30
     * seconds by default to prevent spam.
     *
     * @param classIdentifier The class object of the specific rate provider to
     * log.
     * @param message The message to register in the log.
     */
    public static void logHealthCheckIfNeeded(Class<? extends RateProvider> classIdentifier, String message) {
        logHealthCheckIfNeeded(classIdentifier, message, DEFAULT_INTERVAL);
    }

    /**
     * Logs and checks the frequency of a log info message that is called
     * multiple times in a rate provider, so handles the interval of time of 30
     * seconds by default to prevent spam.
     *
     * @param classIdentifier The class object of the specific rate provider to
     * log.
     * @param message The message to register in the log.
     * @param interval Sets the miliseconds interval of time that a message info
     * log can be reegistered.
     */
    public static void logHealthCheckIfNeeded(Class<? extends RateProvider> classIdentifier, String message, long interval) {
        String className = classIdentifier.getSimpleName();
        long now = System.currentTimeMillis();
        Long lastLog = lastHealthCheckLogs.get(className);
        if (lastLog == null || (now - lastLog) > interval) {
            registerLog(Level.INFO, message);
            lastHealthCheckLogs.put(className, now);
        }
    }

    /**
     * Logs the short description of the exception.
     *
     * @param e The exception catched object.
     * @return The class and a short description of the error.
     */
    public static String writeErrorMessage(Exception e) {
        return e.toString();
    }

    /**
     * To register and debug the log exceptions in a more detailed way by using
     * the stackTrace of the exception catched.
     *
     * @param e Exception object.
     * @return The full stackTrace of the exception catched.
     */
    public static String printStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    //Class to configure and set the properties of the Logger.
    private static class LoggerConfigurationFile {

        /**
         * Configuration and initialization of the log in the file and console.
         */
        public final void setUpLoggerFile() throws IOException {
            Path customPaths = Paths.get(FILE_PATH);
            Files.createDirectories(customPaths);
            if (!Files.exists(customPaths)) {
                Files.createFile(customPaths);
            }

            try {
                Handler consoleHandler = new ConsoleHandler();
                Handler fileHandler = new FileHandler(
                        FILE_PATH + "register.log", false
                );
                fileHandler.setEncoding(UTF_8);
                consoleHandler.setEncoding(UTF_8);
                SimpleFormatter simpleFormatter = new SimpleFormatter();
                fileHandler.setFormatter(simpleFormatter);
                GLOBAL_LOGGER.setUseParentHandlers(false);
                GLOBAL_LOGGER.addHandler(consoleHandler);
                GLOBAL_LOGGER.addHandler(fileHandler);
                consoleHandler.setLevel(Level.ALL);
                fileHandler.setLevel(Level.ALL);
                GLOBAL_LOGGER.setLevel(Level.ALL);
                GLOBAL_LOGGER.log(Level.INFO, "Log de registro iniciado.");
            } catch (IOException | SecurityException e) {
                GLOBAL_LOGGER.log(Level.SEVERE, printStackTrace(e));
            }
        }
    }
}
