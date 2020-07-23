package com.piatek.rafal.account_service.service;

import com.piatek.rafal.account_service.domain.Account;
import com.piatek.rafal.account_service.domain.Client;
import com.piatek.rafal.account_service.domain.CreateAccountRequest;
import com.piatek.rafal.account_service.domain.CurrencyExchangeRequest;
import com.piatek.rafal.account_service.exception.ClientNotFoundException;
import com.piatek.rafal.account_service.exception.CurrencyExchangeException;
import com.piatek.rafal.account_service.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ExchangeService exchangeService;

    public ClientService(ClientRepository clientRepository, ExchangeService exchangeService) {
        this.clientRepository = clientRepository;
        this.exchangeService = exchangeService;
    }

    @Transactional
    public Client createOrGetClient(Client client) {
        return getClient(client.getPesel())
                .orElseGet(() -> clientRepository.save(client.initializeClient()));
    }

    public Optional<Client> getClient(String pesel) {
        return clientRepository.findById(pesel);
    }

    @Transactional
    public Account createOrGetAccount(CreateAccountRequest createAccountRequest) {
        return getClient(createAccountRequest.getPesel())
                .orElseThrow(() -> new ClientNotFoundException())
                .createOrGetAccount(createAccountRequest.getCurrency(), createAccountRequest.getAmount());
    }

    @Transactional
    public void currencyExchange(CurrencyExchangeRequest currencyExchangeRequest) {
        verifyCurrencyExchange(currencyExchangeRequest);
        getClient(currencyExchangeRequest.getPesel())
                .orElseThrow(() -> new ClientNotFoundException())
                .currencyExchange(
                        exchangeService.getExchangeRate(
                                currencyExchangeRequest.getCurrencyFrom().equals("PLN") ?
                                        currencyExchangeRequest.getCurrencyTo() :
                                        currencyExchangeRequest.getCurrencyFrom()),
                        currencyExchangeRequest.getCurrencyFrom(),
                        currencyExchangeRequest.getCurrencyTo(),
                        currencyExchangeRequest.getAmount()
                );
    }

    @Transactional
    public void currencyExchangeFull(CurrencyExchangeRequest currencyExchangeRequest) {
        getClient(currencyExchangeRequest.getPesel())
                .orElseThrow(() -> new ClientNotFoundException())
                .currencyExchangeFull(
                        currencyExchangeRequest.getCurrencyFrom(),
                        currencyExchangeRequest.getCurrencyTo(),
                        currencyExchangeRequest.getAmount()
                );
    }

    private void verifyCurrencyExchange(CurrencyExchangeRequest currencyExchangeRequest) {
        if (!currencyExchangeRequest.getCurrencyFrom().equals("PLN") &&
            !currencyExchangeRequest.getCurrencyTo().equals("PLN"))
            throw new CurrencyExchangeException(
                    String.format(CurrencyExchangeException.NOT_SUPPORTED_CURRENCY_EXCHANGE_MESSAGE,
                            currencyExchangeRequest.getCurrencyFrom(),
                            currencyExchangeRequest.getCurrencyTo()));
    }
}
