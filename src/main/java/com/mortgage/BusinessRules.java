package com.mortgage;

import com.mortgage.models.Mortgage;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for applying business rules on the mortgage data.
 */
@Singleton
public class BusinessRules {

    /**
     * Applies necessary business rules on the customer data, including filtering out invalid mortgages.
     *
     * @param mortgages The mortgage list object to which the rules should be applied.
     */
    public List<Mortgage> applyRules(List<Mortgage> mortgages) {
        List<Mortgage> validMortgages = new ArrayList<>();

        for (Mortgage mortgage : mortgages) {
            //Filter out invalid mortgages from the list
            if (mortgage.getStatus().equals("OPEN")) {
                mortgage = generateCurrentPropertyValue(mortgage); //Apply property valuation rule
                validMortgages.add(mortgage);
            }
        }
        return validMortgages;
    }

    /**
     * Calculates current property value property based on time since product start.
     *
     * @param mortgage The mortgage object to have its properties updated.
     */
    public Mortgage generateCurrentPropertyValue(Mortgage mortgage){
        Double startValue = mortgage.getPropertyStartValue();
        Double annualGrowth = 0.04;
        int yearsElapsed = Period.between(mortgage.getProductStartDate(), LocalDate.now()).getYears();
        mortgage.setPropertyCurrentValue((double) Math.round(startValue * Math.pow((1 + annualGrowth), yearsElapsed)));
        return mortgage;
    }

}
