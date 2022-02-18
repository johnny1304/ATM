package com.ZinkWorks.TechnicalProblem.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CurrencyAmounts")
public class CurrencyUnit {

    public CurrencyUnit(){}

    public CurrencyUnit(Integer unit, Integer quantity) {
        this.unit = unit;
        this.quantity = quantity;
    }

    @Id
    @NotNull
    private Integer unit;

    @NotNull
    private Integer quantity;

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
