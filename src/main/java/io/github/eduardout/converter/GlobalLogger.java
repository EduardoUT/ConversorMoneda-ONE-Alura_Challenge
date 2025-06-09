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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final Logger GLOBAL_LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String FILE_PATH = "logs/";

    private GlobalLogger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Realiza la inisialización del archivo de log.
     *
     * @throws java.io.IOException Lanza esta excepción si hubo problemas para
     * crear el archivo log.
     */
    protected static void setUpLoggerConfigurationFile() throws IOException {
        LoggerConfigurationFile loggerConfigFile = new LoggerConfigurationFile();
        loggerConfigFile.setUpLoggerFile();
    }

    /**
     * Captura el mensaje y el stackTrace de la excepción configurando el nivel
     * severo.
     *
     * @param level Nivel de severidad del Logger a registrar.
     * @param message Mensaje personalizado acorde al contexto del error.
     * @param e Exception para poder obtener el stackTrace.
     */
    public static void registerLogException(Level level, String message, Exception e) {
        GLOBAL_LOGGER.log(level, message, printStackTrace(e));
    }

    /**
     * Captura logs para describir eventos o brindan información.
     *
     * @param level Nivel de severidad del error.
     * @param message Contenido o mensaje de la excepción.
     */
    public static void registerLog(Level level, String message) {
        GLOBAL_LOGGER.log(level, message);
    }

    /**
     * Impresión de la pila de ejecución donde la excepción fue interceptada.
     *
     * @param e Objeto de tipo Exception, ideal para obtener el detalle de la
     * exceptión captada.
     * @return Fragmento de la pila donde el error ocurrió.
     */
    public static String printStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    /**
     * Clase dedicada a la configuración del archivo de registro.
     */
    private static class LoggerConfigurationFile {

        /**
         * Se configura e inicializa el archivo de registro.
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
                        FILE_PATH + "register.log", true
                );
                SimpleFormatter simpleFormatter = new SimpleFormatter();
                fileHandler.setFormatter(simpleFormatter);
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
