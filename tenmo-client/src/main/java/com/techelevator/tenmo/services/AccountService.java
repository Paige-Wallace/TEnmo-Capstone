package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


public class AccountService {

    private final String baseUrl;

    private static final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public AccountService(String url) {
        this.baseUrl = url;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getBalance(long userId) {
        Account account = null;
        try {
            String url = baseUrl + "account/" + userId;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            account = response.getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        }

        return account.getBalance();
    }
}