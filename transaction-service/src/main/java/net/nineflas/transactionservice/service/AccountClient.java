
package net.nineflas.transactionservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.UUID;

@Component
public class AccountClient {

    private final RestClient restClient;

    public AccountClient(@Value("${account-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public AccountDto getAccount(UUID id) {
        try {
            return restClient.get()
                    .uri("/api/accounts/{id}", id)
                    .retrieve()
                    .body(AccountDto.class);

        } catch (RestClientException e) {
            throw new IllegalArgumentException(
                    "Impossible de récupérer le compte : " + id, e
            );
        }
    }

    public void updateBalance(UUID id, double delta) {
        try {
            restClient.patch()
                    .uri("/api/accounts/{id}/balance", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("delta", delta))
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientException e) {
            throw new IllegalArgumentException(
                    "Impossible de mettre à jour le solde du compte : " + id, e
            );
        }
    }
}
