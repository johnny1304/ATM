package com.ZinkWorks.TechnicalProblem.DTO;

import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Data
public class ATMRequest {

    @NotBlank(message = "User ID cannot be blank")
    private String userId;
    @NotBlank(message = "User Pin cannot be blank")
    private String userPin;
    private BigDecimal amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ATMRequest{" +
                "userId='" + userId + '\'' +
                ", userPin='" + userPin + '\'' +
                ", amount=" + amount +
                '}';
    }
}
