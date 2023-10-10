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

/**
 * Client for interacting with the data service to fetch mortgage data.
 */
@Singleton
public class DataApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(DataApiClient.class);
    private static final String DATA_SERVICE_URL = "http://host.docker.internal:8082/data";

    HttpClient client;

    /**
     * Constructor for DataApiClient.
     *
     * @param client The HTTP client for making requests.
     */
    @Inject
    public DataApiClient(HttpClient client) {
        this.client = client;
    }

    /**
     * Retrieves a list of mortgages associated with the provided customer ID.
     *
     * @param customerId The unique identifier of the customer.
     * @return ArrayList of Mortgage objects associated with the customer.
     */
    public ArrayList<Mortgage> getMortgages(String customerId) {
        try {
            String constructedUrl = DATA_SERVICE_URL + "/customer/" + customerId + "/mortgages";
            LOG.info("Fetching mortgages from URL: {}", constructedUrl);
            HttpRequest<?> request = HttpRequest.GET(constructedUrl);

            // Logging the request
            LOG.debug("HTTP Request: {}", request.toString());

            // Log raw response for debugging purposes
            HttpResponse<String> rawResponse = client.toBlocking().exchange(request, String.class);
            LOG.debug("Raw Response Body: {}", rawResponse.getBody().orElse("EMPTY"));

            // Deserialize using explicit type information
            HttpResponse<?> genericResponse = client.toBlocking().exchange(request, Argument.of(ArrayList.class, Mortgage.class));
            HttpResponse<ArrayList<Mortgage>> response = (HttpResponse<ArrayList<Mortgage>>) genericResponse;

            if (response.getStatus().getCode() >= 400) {
                LOG.error("Received an error response: {} - {}", response.getStatus().getCode(), response.getStatus().getReason());
            } else if (!response.getBody().isPresent()) {
                LOG.error("Mortgage data response body is empty or null for customer ID: {}", customerId);
            } else {
                ArrayList<Mortgage> mortgages = response.getBody().get();
                if (mortgages.isEmpty()) {
                    LOG.warn("Deserialized mortgage list is empty for customer ID: {}", customerId);
                } else {
                    for (Mortgage mortgage : mortgages) {
                        LOG.debug("Deserialized Mortgage: {}", mortgage.toString());
                    }
                    return mortgages;
                }
            }
        } catch (Exception e) {
            LOG.error("Error fetching mortgage data for customer ID: {}", customerId, e);
            throw e;
        }

        LOG.warn("Returning an empty mortgage list for customer ID: {} due to issues in data retrieval.", customerId);
        return new ArrayList<>(); // Return an empty list in case of issues to avoid NullPointerException
    }
}
