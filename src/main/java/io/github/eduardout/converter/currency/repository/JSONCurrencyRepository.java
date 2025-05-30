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
package io.github.eduardout.converter.currency.repository;

import static io.github.eduardout.converter.GlobalLogger.*;
import io.github.eduardout.converter.util.RateParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import org.json.JSONObject;

/**
 *
 * @author EduardoUT
 */
public class JSONCurrencyFileRepository implements UpdateCurrencyFileRepository {

    private static final String JSON_FILE = "currency-rates.json";
    private static final String DEFAULT_DIR = "currency_data";
    private static final String ROOT_KEY = "rate-providers";
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Path jsonFilePath;
    private String keyProvider;
    private JSONObject defaultFormat;
    private Map<String, Object> storedProviders;

    public JSONCurrencyFileRepository(String customPath, String keyProvider) throws IOException {
        jsonFilePath = resolvePortablePath(customPath);
        createFileIfNeeded();
        this.keyProvider = keyProvider;
    }

    private Path resolvePortablePath(String customPath) {
        if (customPath != null && !customPath.isEmpty()) {
            return Paths.get(customPath);
        }
        try {
            return Paths.get(System.getProperty("user.home"), DEFAULT_DIR, JSON_FILE);
        } catch (SecurityException e) {
            return Paths.get(System.getProperty("java.io.tmpdir"), DEFAULT_DIR, JSON_FILE);
        }
    }

    /**
     * Writes the new JSON file with the default format content
     * once has the path directories defined in
     * resolvePortablePath.
     *
     * @throws IOException When an I/O error occurs with the given file.
     */
    private void createFileIfNeeded() throws IOException {
        storedProviders = new HashMap<>();
        Files.createDirectories(jsonFilePath.getParent());
        if (!Files.exists(jsonFilePath)) {
            String brackets = "{}";
            defaultFormat = new JSONObject(brackets);
            defaultFormat.put(ROOT_KEY, new JSONObject(brackets));
            Files.write(jsonFilePath, defaultFormat.toString().getBytes());
        }
    }

    public JSONObject readCurrencyRates() {
        try {
            return readFile();
        } catch (IOException ex) {
            registerLogException(Level.SEVERE, "An exception occurs while "
                    + "reading the JSON currencies file. {0}", ex);
        }
        return null;
    }

    @Override
    public void updateCurrencyRates(String response) throws IOException {
        registerLog(Level.INFO, "Checking for changes...");
        JSONObject currentData = readFile();
        String queryRateProvider = currentData.query("/" + ROOT_KEY + "/" + keyProvider).toString();
        if(!queryRateProvider.contains(response)) {
            readWriteLock.writeLock().lock();
            try {
                registerLog(Level.INFO, "Updating currencies from key provider "
                        + keyProvider + " on local JSON file repository.");
                currentData.put(keyProvider, response);
                Files.write(jsonFilePath, currentData.toString().getBytes());
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }

        //After successful query compare to the response, otherwise do nothing
        //When query changes update the currentData with the response according to its keyProvider in a JSONObject
        //Re-write currentData with the changes.

        /*
        if (!response.contains(currentData.toString())) {
            readWriteLock.writeLock().lock();
            try {
                registerLog(Level.INFO, "Updating currencies from local JSON file.");
                Files.write(jsonFilePath, response.getBytes());

                Path tempFile = Files.createTempFile("temp", ".json");
                Files.write(tempFile, response.getBytes());
                Files.move(tempFile, jsonFilePath, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }*/
    }

    private JSONObject readFile() throws IOException {
        readWriteLock.readLock().lock();
        try {
            registerLog(Level.INFO, "Reading local JSON currencies file.");
            // Map the stored providers line by line in JSON  file here.
             List<String> lines = Files.readAllLines(jsonFilePath);
             String line = lines.get(0);
             if (line.isEmpty()) {
                 return defaultFormat;
             }
            return new JSONObject(line);
            /*
            for (String line : lines) {
                if(line.contains(keyProvider)) {
                    return line;
                }
            }*/
        } finally {
            readWriteLock.readLock().unlock();
        }
        //return Collections.emptyList();
    }
}
