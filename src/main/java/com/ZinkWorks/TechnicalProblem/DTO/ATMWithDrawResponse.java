package com.ZinkWorks.TechnicalProblem.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ATMWithDrawResponse {
    private BigDecimal balance;
    private List<Integer> noteList = new ArrayList<>() ;
    private List<String> errors = new ArrayList<>();

    public void addError(String error){
        errors.add(error);
    }
}
