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
package io.github.eduardout.converter.currency.provider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

/**
 *
 * @author EduardoUT
 */
public class APIClient {

    private static APIClient instance;
    private final OkHttpClient okHttpClient;

    private APIClient() {
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * This is a thread-safe synchronized method which ensures all the threads
     * use this resource once its unused.
     *
     * @return APIClient object with the OkkhttpClient.
     */
    public static synchronized APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    /**
     * Fetch data from API Rest Service, getting a body response..
     *
     * @param url The url from the API Rest Service.
     * @return The body response in a String.
     * @throws IOException When request was not successful or body response is
     * null.
     */
    public String fetchDataAsString(String url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("Argument url is null, check if "
                    + "provided links are correct in properties file");
        }
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful()) {
                int responseCode = response.code();
                if (responseBody != null) {
                    throw new IOException("HTTP request response unsuccessful: "
                            + responseCode + " " + responseBody.string());
                }
                throw new IOException("HTTP request response not successful: "
                        + responseCode);
            }

            if (responseBody == null) {
                throw new IOException("Response body is null.");
            }
            return responseBody.string();
        }
    }

    /**
     * Request all the available currencies from Free Currency Exchange Rates
     * API.
     *
     * @param url The url source to fetch data.
     * @return A JSONObject that contains all the available curencies of this
     * API provider.
     * @throws IOException When the fetchDataAsString request method was not
     * successful, the body of the response is null or there is a problem while
     * read, write or open the local JSON file.
     */
    public JSONObject fetchDataAsJSONObject(String url) throws IOException {
        String response;
        response = fetchDataAsString(url);
        return new JSONObject(response);
    }
}
