package com.ZinkWorks.TechnicalProblem.DTO;

import java.math.BigDecimal;
import java.util.*;

public class ATMWithDrawResponse {
    private BigDecimal balance;
    private List<Integer> noteList = new ArrayList<Integer>() ;
    private List<String> errors = new ArrayList<String>();

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Integer> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Integer> noteList) {
        this.noteList = noteList;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error){
        errors.add(error);
    }
}
