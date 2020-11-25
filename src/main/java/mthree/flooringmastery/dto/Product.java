/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Contains fields with data regarding the details of a product type.
 * 
 * @author utkua
 */
public class Product {
    
    private final String productType;
    private final BigDecimal costPerSquareFoot;
    private final BigDecimal labourCostPerSquareFoot;

    /**
     * Create a Product object with passed values for each respective field.
     * 
     * @param productType
     * @param costPerSquareFoot
     * @param labourCostPerSquareFoot
     */
    public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal labourCostPerSquareFoot) {
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
    }

    /**
     *
     * @return
     */
    public String getProductType() {
        return productType;
    }

    /**
     *
     * @return
     */
    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    /**
     *
     * @return
     */
    public BigDecimal getLabourCostPerSquareFoot() {
        return labourCostPerSquareFoot;
    }

    @Override
    public String toString() {
        return "Product{" + "productType=" + productType + ", costPerSquareFoot=" + costPerSquareFoot + ", labourCostPerSquareFoot=" + labourCostPerSquareFoot + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.productType);
        hash = 23 * hash + Objects.hashCode(this.costPerSquareFoot);
        hash = 23 * hash + Objects.hashCode(this.labourCostPerSquareFoot);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.costPerSquareFoot, other.costPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.labourCostPerSquareFoot, other.labourCostPerSquareFoot)) {
            return false;
        }
        return true;
    }
    
}
