package com.mortgage;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

@Singleton
public class DataApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(DataApiClient.class);
    private static final String MORTGAGES_PATH = "/data/customer";
    private final HttpClient httpClient;
    private final URI apiUrl;

    public DataApiClient(@Client("data-service") HttpClient httpClient) {
        this.httpClient = httpClient;
        this.apiUrl = UriBuilder.of("http://host.docker.internal:8082").build();
    }

    public String getMortgagesJson(String customerId) throws IOException {
        try {
            URI requestUri = UriBuilder.of(apiUrl).path(MORTGAGES_PATH + "/" + customerId + "/mortgages").build();
            LOG.info("Constructed URL for data-service: " + requestUri.toString());
            HttpRequest<?> request = HttpRequest.GET(requestUri.toString());
            HttpResponse<String> response = httpClient.toBlocking().exchange(request, String.class);

            if (response.code() == 200) {
                return response.body();
            } else {
                throw new IOException("Failed to fetch customer mortgage data. Status code: " + response.code());
            }
        } catch (Exception e) {
            LOG.error("Error fetching customer mortgage data: " + e.getMessage(), e);
            throw new IOException("Failed to fetch customer mortgage data: " + e.getMessage(), e);
        }
    }

}
