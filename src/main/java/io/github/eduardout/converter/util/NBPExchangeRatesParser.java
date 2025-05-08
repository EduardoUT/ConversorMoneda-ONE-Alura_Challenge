package io.github.eduardout.converter.util;

import io.github.eduardout.converter.currency.CurrencyUnit;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NBPExchangeRatesParser implements RateParser {

    @Override
    public Map<String, BigDecimal> parseRate(Object response, CurrencyUnit base, CurrencyUnit target) {
        JSONArray jsonArray = new JSONArray(response.toString());
        JSONArray rates = jsonArray.getJSONObject(0).getJSONArray("rates");
        String currencyKey = "currency";
        return rates.toList()
                .stream()
                .filter(HashMap.class::isInstance)
                .map(HashMap.class::cast)
                .filter(hashMap -> hashMap.containsValue(base.getCurrencyCode())
                        || hashMap.containsValue(target.getCurrencyCode()))
                .filter(hashMap -> hashMap.remove(currencyKey, hashMap.get(currencyKey)))
                .collect(Collectors.toMap(
                                key -> key.get("code").toString(),
                                value -> new BigDecimal(value.get("mid").toString())
                        )
                );
    }
}
