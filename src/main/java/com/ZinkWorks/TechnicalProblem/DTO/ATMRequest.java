package com.ZinkWorks.TechnicalProblem.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
