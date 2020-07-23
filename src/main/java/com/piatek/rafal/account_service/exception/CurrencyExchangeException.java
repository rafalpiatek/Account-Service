package com.piatek.rafal.account_service.exception;

public class CurrencyExchangeException extends RuntimeException {

    public static final String NOT_FOUND_ACCOUNT_MESSAGE = "not found account in %s currency";
    public static final String NOT_ENOUGH_AMOUNT_MESSAGE = "not enough amount on account (%.2f %s)";
    public static final String NOT_SUPPORTED_CURRENCY_MESSAGE = "not supported currency %s";
    public static final String NOT_SUPPORTED_CURRENCY_EXCHANGE_MESSAGE = "not supported currency exchange %s -> %s";

    public CurrencyExchangeException(String message) {
        super(message);
    }

}
