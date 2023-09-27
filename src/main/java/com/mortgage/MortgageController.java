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

import java.util.ArrayList;

@Controller("/mortgages")
public class MortgageController {

    private final DataApiClient client;
    private static final Logger LOG = LoggerFactory.getLogger(DataApiClient.class);


    @Inject
    public MortgageController(DataApiClient client) {
        this.client = client;
    }

    @Get
    @Produces(MediaType.TEXT_JSON)
    public ArrayList<Mortgage> mortgages(HttpRequest<?> request) {
        try {
            ArrayList<Mortgage> mortgages = client.getMortgages(request.getHeaders().get("customerId"));
            // Apply business rules
            ArrayList<Mortgage> validMortgages = new ArrayList<Mortgage>();

            for (Mortgage mortgage : mortgages) {
                mortgage.generateCurrentPropertyValue();
                if(mortgage.getStatus().equals("OPEN")){
                    validMortgages.add(mortgage);
                }
            }
            // Return the modified list of mortgages - automatically serialised to JSON
            return validMortgages;
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching customer data.");
        }
    }
}