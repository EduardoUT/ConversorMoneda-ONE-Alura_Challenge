package io.github.eduardout.converter.currency.provider;

import io.github.eduardout.converter.currency.config.PropertiesConfig;

public abstract class AbstractCurrencyProvider implements RateProvider {

    private HttpClient httpClient;
    private PropertiesConfig propertiesConfig;
    private String propertyKeyPrefix;

    protected AbstractCurrencyProvider (HttpClient httpClient,
                                     PropertiesConfig propertiesConfig,
                                     String propertyKeyPrefix) {
        if(httpClient == null) {
            throw new IllegalArgumentException("HttpClient is null");
        }
        if(propertiesConfig == null) {
            throw new IllegalArgumentException("PropertiesConfig is null.");
        }
        if(propertyKeyPrefix == null || propertyKeyPrefix.isEmpty()) {
            throw new IllegalArgumentException("The property key prefix is null or empty.");
        }
        this.httpClient = httpClient;
        this.propertiesConfig = propertiesConfig;
        this.propertyKeyPrefix = propertyKeyPrefix;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public PropertiesConfig getPropertiesConfig() {
        return propertiesConfig;
    }

    public String getPropertyKeyPrefix() {
        return propertyKeyPrefix;
    }
}
