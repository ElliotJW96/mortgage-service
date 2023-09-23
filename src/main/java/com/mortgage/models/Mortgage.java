package com.mortgage.models;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
@Serdeable
public class Mortgage {

    String mortgageId;
    String status;
    boolean residential;
    Double balance;
    int termMonths;
    String productId;
    LocalDate productStartDate;
    Double propertyStartValue;
    Double propertyCurrentValue;
    String postcode;
    String addressLine1;
    String addressLine2;

    public String getMortgageId() {
        return mortgageId;
    }
    public void setMortgageId(String mortgageId) {
        this.mortgageId = mortgageId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isResidential() {
        return residential;
    }
    public void setResidential(boolean residential) {
        this.residential = residential;
    }

    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getTermMonths() {
        return termMonths;
    }
    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public LocalDate getProductStartDate() {
        return productStartDate;
    }
    public void setProductStartDate(LocalDate productStartDate) {
        this.productStartDate = productStartDate;
    }

    public Double getPropertyStartValue() {
        return propertyStartValue;
    }
    public void setPropertyStartValue(Double propertyStartValue) {
        this.propertyStartValue = propertyStartValue;
    }

    public Double getPropertyCurrentValue() {
        return propertyCurrentValue;
    }
    public void setPropertyCurrentValue(Double propertyCurrentValue) {
        this.propertyCurrentValue = propertyCurrentValue;
    }

    public String getPostcode() {
        return postcode;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void generateCurrentPropertyValue(){
        Double startValue = getPropertyStartValue();
        Double annualGrowth = 0.04;
        int yearsElapsed = Period.between(getProductStartDate(), LocalDate.now()).getYears();


        setPropertyCurrentValue((double) Math.round(startValue * Math.pow((1 + annualGrowth), yearsElapsed)));
    }

}
