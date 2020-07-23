package com.piatek.rafal.account_service.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CurrencyExchangeRequest {

    @NotNull
    private String pesel;

    @NotNull
    private String currencyFrom;

    @NotNull
    private String currencyTo;

    @NotNull
    private double amount;

}
