package com.piatek.rafal.account_service.controller;

import com.piatek.rafal.account_service.domain.Client;
import com.piatek.rafal.account_service.domain.CurrencyExchangeRequest;
import com.piatek.rafal.account_service.domain.CurrencyExchangeResponse;
import com.piatek.rafal.account_service.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createOrGetClient(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientService.createOrGetClient(client));
    }

    @GetMapping("/{pesel}")
    public ResponseEntity<Client> getClient(@PathVariable(name = "pesel") String pesel) {
        Optional<Client> client = clientService.getClient(pesel);
        return client.isPresent() ? ResponseEntity.ok(client.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("/currencyExchange")
    public ResponseEntity<CurrencyExchangeResponse> currencyExchange(@Valid @RequestBody CurrencyExchangeRequest currencyExchangeRequest) {
        try {
            clientService.currencyExchange(currencyExchangeRequest);
            return ResponseEntity.ok(
                    CurrencyExchangeResponse
                            .builder()
                            .status(CurrencyExchangeResponse.Status.SUCCESS)
                            .build());
        } catch (Exception exc) {
            return ResponseEntity.ok(
                    CurrencyExchangeResponse
                            .builder()
                            .status(CurrencyExchangeResponse.Status.FAILED)
                            .exception(exc.getClass().getSimpleName())
                            .message(exc.getMessage())
                            .build());
        }
    }

    @PostMapping("/currencyExchangeFull")
    public ResponseEntity<CurrencyExchangeResponse> currencyExchangeFull(@Valid @RequestBody CurrencyExchangeRequest currencyExchangeRequest) {
        try {
            clientService.currencyExchangeFull(currencyExchangeRequest);
            return ResponseEntity.ok(
                    CurrencyExchangeResponse
                            .builder()
                            .status(CurrencyExchangeResponse.Status.SUCCESS)
                            .build());
        } catch (Exception exc) {
            return ResponseEntity.ok(
                    CurrencyExchangeResponse
                            .builder()
                            .status(CurrencyExchangeResponse.Status.FAILED)
                            .exception(exc.getClass().getSimpleName())
                            .message(exc.getMessage())
                            .build());
        }
    }

}
