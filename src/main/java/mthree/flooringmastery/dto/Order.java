/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Contains fields with data regarding the details of an order.
 * 
 * @author utkua
 */
public class Order {
   
    /**
     * Unique number of the order.
     */
    private final int orderNumber;
    
    /**
     * Time and day the order was created.
     */
    private final LocalDateTime creationDateTime;
    
    /**
     * Day the user wishes to complete order.
     */
    private final LocalDate orderDate;
    
    /**
     * Name of customer that placed order.
     */
    private String customerName;
    
    /**
     * Current status of order - "Active", "Cancelled", or "Fulfilled".
     */
    private Statuses status;
    
    /**
     * State object containing State information associated with this order.
     * Specifies the details of the state associated with this order.
     */
    private State state;
    
    /**
     * Product object containing product information associated with this order.
     * Specifies the details of the product type associated with this order.
     */
    private Product product;
    
    /**
     * Costs object containing cost information associated with this order.
     */
    private Costs costs;
    
    /**
     * Area of floor covered by this order.
     */
    private BigDecimal area;

    /**
     *
     * @param orderNumber order number of constructed order
     * @param creationDateTime order creation date
     * @param orderDate order completion date
     */
    public Order(int orderNumber, LocalDateTime creationDateTime, LocalDate orderDate) {
        this.orderNumber = orderNumber;
        this.creationDateTime = creationDateTime;
        this.orderDate = orderDate;
    }

    /**
     * 
     * @return order number of this order.
     */
    public int getOrderNumber() {
        return orderNumber;
    }
    
    /**
     *
     * @return creation time and date of this object
     */
    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * Converts date to string prior to returning.
     * @return order date stored in this Order object
     */
    public LocalDate getDate() {
        return orderDate;
    }

    /**
     * 
     * @return name of customer that placed this order
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @return
     */
    public Statuses getStatus() {
        return status;
    }

    /**
     *
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @return
     */
    public Product getProduct() {
        return product;
    }

    /**
     *
     * @return
     */
    public Costs getCosts() {
        return costs;
    }

    /**
     *
     * @return
     */
    public BigDecimal getArea() {
        return area;
    }
    
    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @param status
     */
    public void setStatus(Statuses status) {
        this.status = status;
    }

    /**
     *
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     *
     * @param product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     *
     * @param costs
     */
    public void setCosts(Costs costs) {
        this.costs = costs;
    }

    /**
     *
     * @param area
     */
    public void setArea(BigDecimal area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.orderNumber;
        hash = 83 * hash + Objects.hashCode(this.creationDateTime);
        hash = 83 * hash + Objects.hashCode(this.orderDate);
        hash = 83 * hash + Objects.hashCode(this.customerName);
        hash = 83 * hash + Objects.hashCode(this.status);
        hash = 83 * hash + Objects.hashCode(this.state);
        hash = 83 * hash + Objects.hashCode(this.product);
        hash = 83 * hash + Objects.hashCode(this.costs);
        hash = 83 * hash + Objects.hashCode(this.area);
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
        final Order other = (Order) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.creationDateTime, other.creationDateTime)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.costs, other.costs)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Order{" + "orderNumber=" + orderNumber + ", creationDateTime=" + creationDateTime + ", orderDate=" + orderDate + ", customerName=" + customerName + ", status=" + status + ", state=" + state + ", product=" + product + ", costs=" + costs + ", area=" + area + '}';
    }
    
}
