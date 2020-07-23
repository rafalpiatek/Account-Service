package com.piatek.rafal.account_service.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExchangeRate {

    private String currency;
    private String code;
    private List<Rate> rates;

    @Data
    public static class Rate {
        private String no;
        private double mid;
        private LocalDate effectiveDate;
    }
}
