package com.piatek.rafal.account_service.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateAccountRequest {

    @NotNull
    private String pesel;

    @NotNull
    private String currency;

    @NotNull
    private double amount;

}
