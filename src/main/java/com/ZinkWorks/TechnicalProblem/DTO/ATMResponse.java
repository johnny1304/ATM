package com.ZinkWorks.TechnicalProblem.DTO;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ATMResponse {
    private BigDecimal currentBalance;
    private BigDecimal totalAvailableBalance;
    private List<String> errors = new ArrayList<String>();


    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getTotalAvailableBalance() {
        return totalAvailableBalance;
    }

    public void setTotalAvailableBalance(BigDecimal totalAvailableBalance) {
        this.totalAvailableBalance = totalAvailableBalance;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String errorMessage){
        errors.add(errorMessage);
    }
}
