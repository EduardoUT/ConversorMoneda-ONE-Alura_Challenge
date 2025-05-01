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
import io.github.eduardout.converter.currency.CurrencyUnit;
import io.github.eduardout.converter.currency.provider.RateProvider;
import io.github.eduardout.converter.util.DefaultRateParser;
import io.github.eduardout.converter.util.RateParser;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import org.json.JSONObject;

/**
 *
 * @author EduardoUT
 */
public class JSONCurrencyFileRepository implements RateProvider,
        UpdateCurrencyFileRepository {

    private static final String JSON_FILE = "currency-rates.json";
    private static final String DEFAULT_DIR = "currency_data";
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Path jsonFilePath;
    private final RateParser rateParser;

    public JSONCurrencyFileRepository(String customPath) throws IOException {
        jsonFilePath = resolvePortablePath(customPath);
        createFileIfNeeded();
        rateParser = new DefaultRateParser();
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
     * Creates a directory and a JSON file to store currencies locally, creates
     * a new directory if not exists and same with the json file.
     *
     * @throws IOException When an I/O error occurs with the given file.
     */
    private void createFileIfNeeded() throws IOException {
        Files.createDirectories(jsonFilePath.getParent());
        if (!Files.exists(jsonFilePath)) {
            Files.write(jsonFilePath, "{}".getBytes());
        }
    }

    @Override
    public Optional<Map<String, BigDecimal>> getCurrencyRates(CurrencyUnit base, CurrencyUnit target) {
        Map<String, BigDecimal> rate = null;
        try {
            JSONObject jsonObject = readFile();
            rate = rateParser.parseRate(jsonObject, base, target);
        } catch (IOException ex) {
            registerLogException(Level.SEVERE, "An exception occurs while "
                    + "reading the JSON currencies file. {0}", ex);
        }
        return Optional.ofNullable(rate);
    }

    @Override
    public void updateCurrencyRates(JSONObject response) throws IOException {
        registerLog(Level.INFO, "Checking for changes...");
        JSONObject currentData = readFile();
        if (!response.similar(currentData)) {
            readWriteLock.writeLock().lock();
            try {
                registerLog(Level.INFO, "Updating currencies from local JSON file.");
                Path tempFile = Files.createTempFile("temp", ".json");
                Files.write(tempFile, response.toString().getBytes());
                Files.move(tempFile, jsonFilePath, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }
    }

    private JSONObject readFile() throws IOException {
        readWriteLock.readLock().lock();
        try {
            registerLog(Level.INFO, "Reading local JSON currencies file.");
            List<String> content = Files.readAllLines(jsonFilePath);
            return new JSONObject(content.get(0));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
