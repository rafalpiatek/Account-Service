package com.piatek.rafal.account_service.service;

import com.piatek.rafal.account_service.domain.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "exchangeRate", url = "https://api.nbp.pl/api")
public interface ExchangeRateService {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/exchangerates/rates/a/{currency}?format=json",
            consumes = "application/json")
    ExchangeRate getExchangeRate(@PathVariable("currency") String currency);

}