package com.piatek.rafal.account_service.service;

import com.piatek.rafal.account_service.exception.CurrencyExchangeException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeService {

    private final ExchangeRateService exchangeRateService;
    private final Map<String, Double> exchangeRates = new HashMap<>();

    public ExchangeService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public double getExchangeRate(String currency) {
        if (exchangeRates.get(currency) == null) {
            try {
                exchangeRates.put(currency, exchangeRateService.getExchangeRate(currency).getRates().get(0).getMid());
            } catch (Exception exc) {
                throw new CurrencyExchangeException(
                        String.format(CurrencyExchangeException.NOT_SUPPORTED_CURRENCY_MESSAGE, currency));
            }
        }
        return exchangeRates.get(currency);
    }

}
