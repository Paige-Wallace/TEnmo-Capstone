package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken  = null;

    public TransferService(String url) {
        this.baseUrl = url;
    }
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }


    public Transfer initiateTransfer(Transfer transfer){
        Transfer transfer1 = null;
        String url = baseUrl + "transfer";

        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
            transfer1 = restTemplate.postForObject(url, entity, Transfer.class);

        }
        catch(RestClientResponseException e)
        {
            BasicLogger.log(e.getMessage());
        }

        return transfer1;
    }

    public List<Transfer> getAllTransfer(long userId){
        List<Transfer> transfers = new ArrayList<>();

        try
        {
            String url = baseUrl + "transfer/" + userId;
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        }
        catch(RestClientResponseException e)
        {
            BasicLogger.log(e.getMessage());
        }

        return transfers;
    }

}