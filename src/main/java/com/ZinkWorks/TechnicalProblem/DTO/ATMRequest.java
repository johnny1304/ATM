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

    @Override
    public String toString() {
        return "ATMRequest{" +
                "userId='" + userId + '\'' +
                ", userPin='" + userPin + '\'' +
                ", amount=" + amount +
                '}';
    }
}
