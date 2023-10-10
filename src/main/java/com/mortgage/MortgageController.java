package com.mortgage;

import com.mortgage.models.Mortgage;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller responsible for handling requests related to mortgages.
 */
@Controller("/mortgages")
public class MortgageController {

    private final DataApiClient client;
    private final BusinessRules businessRules;
    private static final Logger LOG = LoggerFactory.getLogger(MortgageController.class);

    /**
     * Constructor for MortgageController.
     *
     * @param client The client used to fetch mortgage data.
     * @param businessRules The business rules service for filtering and validating mortgages.
     */
    @Inject
    public MortgageController(DataApiClient client, BusinessRules businessRules) {
        this.client = client;
        this.businessRules = businessRules;
    }

    /**
     * Endpoint to fetch a list of mortgages for a given customer ID.
     *
     * @param request The incoming HTTP request. Expected to contain a "customerId" header.
     * @return A list of valid Mortgage objects associated with the customer.
     * @throws HttpStatusException If an error occurs during data retrieval or processing.
     */
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mortgage> mortgages(HttpRequest<?> request) {
        try {
            List<Mortgage> mortgages = client.getMortgages(request.getHeaders().get("customerId"));
            return businessRules.applyRules(mortgages);
        } catch (Exception e) {
            LOG.error("Error fetching mortgages.", e);
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching customer data.");
        }
    }
}
