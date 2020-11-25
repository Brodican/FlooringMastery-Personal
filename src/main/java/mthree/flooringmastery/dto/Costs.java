/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Contains fields with data regarding the details an Order object's associated costs.
 * 
 * @author utkua
 */
public class Costs {
    
    private BigDecimal materialCost;
    private BigDecimal labourCost;
    private BigDecimal taxCost;
    private BigDecimal total;

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public BigDecimal getTaxCost() {
        return taxCost;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public void setTaxCost(BigDecimal taxCost) {
        this.taxCost = taxCost;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Costs{" + "materialCost=" + materialCost + ", labourCost=" + labourCost + ", taxCost=" + taxCost + ", total=" + total + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.materialCost);
        hash = 41 * hash + Objects.hashCode(this.labourCost);
        hash = 41 * hash + Objects.hashCode(this.taxCost);
        hash = 41 * hash + Objects.hashCode(this.total);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Costs other = (Costs) obj;
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.labourCost, other.labourCost)) {
            return false;
        }
        if (!Objects.equals(this.taxCost, other.taxCost)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        return true;
    }
    
    
    
}
