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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.stream.Collectors;

import io.github.eduardout.converter.currency.provider.RateProvider;
import org.json.JSONObject;

/**
 * @author EduardoUT
 */
public class JSONCurrencyRepository implements RateProvider, UpdateCurrencyFileRepository {

    private static final String JSON_FILE = "currency-rates.json";
    private static final String DEFAULT_DIR = "currency_data";
    private static final String ROOT_KEY = "rate-provider";
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Path jsonFilePath;
    private JSONObject defaultFormat;

    public JSONCurrencyRepository(String customPath) throws IOException {
        jsonFilePath = resolvePortablePath(customPath);
        createFileIfNeeded();
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
        Files.createDirectories(jsonFilePath.getParent());
        if (!Files.exists(jsonFilePath)) {
            String brackets = "{}";
            defaultFormat = new JSONObject(brackets);
            defaultFormat.put(ROOT_KEY, new JSONObject(brackets));
            Files.write(jsonFilePath, defaultFormat.toString().getBytes());
        }
    }

    @Override
    public void updateCurrencyRates(Map<String, BigDecimal> currencies) throws IOException {
        if(!currencies.isEmpty()) {
            registerLog(Level.INFO, "Checking for changes in repository before update...");
            JSONObject currentData = readFile();
            JSONObject jsonResponse = new JSONObject(currencies);
            if (!jsonResponse.similar(currentData.getJSONObject(ROOT_KEY))) {
                try {
                    registerLog(Level.INFO, "Changes detected updating new currency info in repository.");
                    readWriteLock.writeLock().lock();
                    currentData.put("date", LocalDateTime.now().toString());
                    currentData.put(ROOT_KEY, jsonResponse);
                    Files.write(jsonFilePath, currentData.toString().getBytes());
                } finally {
                    readWriteLock.writeLock().unlock();
                }
            } else {
                registerLog(Level.INFO, "No changes were detected...");
            }
        }
    }

    private JSONObject readFile() throws IOException {
        readWriteLock.readLock().lock();
        try {
            registerLog(Level.INFO, "Loading currencies from repository.");
            List<String> lines = Files.readAllLines(jsonFilePath);
            String line = lines.get(0);
            if (line.isEmpty()) {
                return defaultFormat;
            }
            return new JSONObject(line);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, BigDecimal> getCurrencyRates() {
        Map<String, BigDecimal> currencyRates = Collections.emptyMap();
        try {
            currencyRates = readFile()
                    .getJSONObject(ROOT_KEY)
                    .toMap()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                                    entryKey -> entryKey.getKey().toUpperCase(),
                                    entryValue -> new BigDecimal(entryValue.getValue().toString())
                            )
                    );
        } catch (IOException e) {
            registerLogException(Level.SEVERE, "An exception occurs while "
                    + "reading the JSON currencies file. {0}", e);
        }
        return currencyRates;
    }
}
