package io.github.eduardout.converter.currency.provider;

import java.math.BigDecimal;
import java.util.*;

public class RateProviderRegistry {

    private Set<RateProvider> rateProviderSet;

    public RateProviderRegistry(Set<RateProvider> rateProviderSet) {
        validateStateRegistry(rateProviderSet);
        this.rateProviderSet = rateProviderSet;
    }

    public void addRateProvider(RateProvider rateProvider) {
        if(rateProviderSet == null) {
            rateProviderSet = new LinkedHashSet<>();
        }
        rateProviderSet.add(rateProvider);
    }

    public void removeRateProvider(RateProvider rateProvider) {
        validateRateProvider(rateProvider);
        rateProviderSet.remove(rateProvider);
    }

    public Map<String, BigDecimal> filterCurrencyRatesFromAvailableProvider() {
        return rateProviderSet
                .stream()
                .map(RateProvider::getCurrencyRates)
                .filter(currencyRates -> !currencyRates.isEmpty())
                .findFirst()
                .orElseGet(Collections::emptyMap);
    }

    private void validateRateProvider(RateProvider rateProvider) {
        if(rateProvider == null) {
            throw new IllegalArgumentException("The rate provider is null.");
        }
        rateProviderSet.add(rateProvider);
    }

    private void validateStateRegistry(Set<RateProvider> rateProviderSet) {
        if(rateProviderSet.isEmpty()) {
            throw new IllegalStateException("The RateProviderRegistry needs at least one element added.");
        }
        if(!(rateProviderSet instanceof LinkedHashSet)) {
            throw new IllegalArgumentException("A LinkedHashSet must be provided in this collection.");
        }
    }
}
