package com.ZinkWorks.TechnicalProblem.DTO;


import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ATMResponse {
    private BigDecimal currentBalance;
    private BigDecimal totalAvailableBalance;
    private List<String> errors = new ArrayList<>();

    public void addError(String errorMessage){
        errors.add(errorMessage);
    }
}
