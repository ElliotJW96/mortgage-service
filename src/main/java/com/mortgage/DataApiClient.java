package com.mortgage;

import com.mortgage.models.Mortgage;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.core.type.Argument;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Singleton
public class DataApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(DataApiClient.class);
    private static final String DATA_SERVICE_URL = "http://host.docker.internal:8082/data/customer/";

    HttpClient client;

    @Inject
    public DataApiClient(HttpClient client) {
        this.client = client;
    }

    public ArrayList<Mortgage> getMortgages(String customerId) {
        try {
            String constructedUrl = DATA_SERVICE_URL + customerId + "/mortgages";
            LOG.info("Constructed URL for data-service: " + constructedUrl);
            HttpRequest<?> request = HttpRequest.GET(constructedUrl);
            LOG.info(request.toString());

            // Log raw response
            HttpResponse<String> rawResponse = client.toBlocking().exchange(request, String.class);
            LOG.info("Raw Response Body: " + rawResponse.getBody().orElse("EMPTY"));

            // Deserialize using explicit type information
            HttpResponse<?> genericResponse = client.toBlocking().exchange(request, Argument.of(ArrayList.class, Mortgage.class));
            HttpResponse<ArrayList<Mortgage>> response = (HttpResponse<ArrayList<Mortgage>>) genericResponse;

            if (response.getStatus().getCode() >= 400) {
                LOG.error("Received an error response: " + response.getStatus().getCode() + " - " + response.getStatus().getReason());
            } else if (!response.getBody().isPresent()) {
                LOG.error("Response body is empty or null.");
            } else {
                ArrayList<Mortgage> mortgages = response.getBody().get();
                if (mortgages.isEmpty()) {
                    LOG.warn("Deserialized mortgage list is empty.");
                } else {
                    for (Mortgage mortgage : mortgages) {
                        LOG.info("Deserialized Mortgage: " + mortgage.toString());
                    }
                    return mortgages;
                }
            }
        } catch (Exception e) {
            LOG.error("Error fetching mortgage data.", e);
            throw e;
        }
        return new ArrayList<>(); // Return an empty list in case of issues to avoid NullPointerException
    }
}
