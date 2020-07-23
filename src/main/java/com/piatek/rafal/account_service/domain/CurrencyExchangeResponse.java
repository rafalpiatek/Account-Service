package com.piatek.rafal.account_service.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyExchangeResponse {

    private Status status;
    private String exception;
    private String message;

    public enum Status {
        SUCCESS, FAILED
    }

}
