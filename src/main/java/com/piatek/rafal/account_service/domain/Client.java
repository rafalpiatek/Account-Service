package com.piatek.rafal.account_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piatek.rafal.account_service.exception.CurrencyExchangeException;
import com.piatek.rafal.account_service.validator.Adult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.pl.PESEL;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CLIENT")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @NotNull
    @NotEmpty
    @PESEL
    @Adult
    @EqualsAndHashCode.Include
    private String pesel;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private double initializingAmount;

    @Version
    @JsonIgnore
    private Long version;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Account> accounts;

    public Client initializeClient() {
        createDefaultAccounts();
        return this;
    }

    public Account createOrGetAccount(String currency, double amount) {
        if (accounts == null) accounts = new HashSet<>();

        return accounts
                .stream()
                .filter(account -> account.getCurrency().equals(currency))
                .findFirst()
                .orElseGet(() -> {
                    Account account = Account
                            .builder()
                            .currency(currency)
                            .client(this)
                            .amount(amount)
                            .build();

                    accounts.add(account);
                    return account;
                });
    }

    private void createDefaultAccounts() {
        createOrGetAccount("PLN", initializingAmount);
        createOrGetAccount("USD", 0);
    }

    public void currencyExchange(double exchangeRate, String currencyFrom, String currencyTo, double amount) {
        Account accountFrom = getAccountWithCurrency(currencyFrom);
        Account accountTo = getAccountWithCurrency(currencyTo);

        verifyAmountOnAccount(accountFrom, amount);

        if (accountFrom.getCurrency().equals("PLN")) {
            accountFrom.setAmount(accountFrom.getAmount() - amount);
            accountTo.setAmount(
                    accountTo.getAmount() +
                            Monetary
                                    .getDefaultAmountFactory()
                                    .setCurrency(currencyTo)
                                    .setNumber(amount)
                                    .create()
                                    .divide(exchangeRate)
                                    .with(Monetary.getDefaultRounding())
                                    .getNumber()
                                    .doubleValue());
        } else {
            accountFrom.setAmount(accountFrom.getAmount() - amount);
            accountTo.setAmount(
                    accountTo.getAmount() +
                            Monetary
                                    .getDefaultAmountFactory()
                                    .setCurrency(currencyTo)
                                    .setNumber(amount)
                                    .create()
                                    .multiply(exchangeRate)
                                    .with(Monetary.getDefaultRounding())
                                    .getNumber()
                                    .doubleValue());
        }

    }

    public void currencyExchangeFull(String currencyFrom, String currencyTo, double amount) {
        Account accountFrom = getAccountWithCurrency(currencyFrom);
        Account accountTo = getAccountWithCurrency(currencyTo);

        verifyAmountOnAccount(accountFrom, amount);

        accountFrom.setAmount(accountFrom.getAmount() - amount);

        MonetaryAmount monetaryAmountFrom = Money.of(amount, currencyFrom);
        CurrencyConversion currencyConversion = MonetaryConversions.getConversion(currencyTo);
        MonetaryAmount monetaryAmountTo = monetaryAmountFrom.with(currencyConversion).with(Monetary.getDefaultRounding());

        accountTo.setAmount(accountTo.getAmount() + monetaryAmountTo.getNumber().doubleValueExact());
    }

    private Account getAccountWithCurrency(String currency) {
        return accounts
                .stream()
                .filter(account -> account.getCurrency().equals(currency))
                .findFirst()
                .orElseThrow(() ->
                        new CurrencyExchangeException(
                                String.format(CurrencyExchangeException.NOT_FOUND_ACCOUNT_MESSAGE, currency)));
    }

    private void verifyAmountOnAccount(Account account, double amount) {
        if (amount > account.getAmount())
            throw new CurrencyExchangeException(
                    String.format(CurrencyExchangeException.NOT_ENOUGH_AMOUNT_MESSAGE,
                            account.getAmount(), account.getCurrency()));
    }

}
