/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.service;

import java.time.LocalDate;
import java.util.List;
import mthree.flooringmastery.dao.DataPersistenceException;
import mthree.flooringmastery.dto.Order;

/**
 * Interface for the service layer, a class that handles business logic
 * for the application.
 * 
 * @author utkua
 */
public interface FlooringMasteryServiceLayer {
    
    /**
     * Validate order data currently in files.The state and product associated 
     * with each order must exist in the taxes and products files respectively.
     * 
     * @throws mthree.flooringmastery.dao.DataPersistenceException
     * @throws mthree.flooringmastery.service.DataValidationException
     */
    public void validateData() throws 
            DataPersistenceException,
            DataValidationException;
    
    /**
     * Retrieves order with given order number and date.
     * 
     * @param orderNumber order number of order to retrieve
     * @param date date order must have
     * @return retrieved Order object
     * @throws DataPersistenceException 
     * @throws mthree.flooringmastery.service.NoSuchOrderException 
     */
    Order getOrder(int orderNumber, LocalDate date) throws 
            DataPersistenceException,
            NoSuchOrderException;
    
    /**
     * Get all orders due to be completed at given date.
     * 
     * @param date LocalDate the order date of the retrieved orders must equal
     * @return list of orders due to be completed at given date
     * @throws DataPersistenceException 
     */
    List<Order> getOrdersAtDate(LocalDate date) throws DataPersistenceException;
    
    /**
     * Create and return an order with the given details.
     * Ensure the order data is valid.
     * 
     * @param newOrderDetails array containing details retrieved from user for order creation
     * @return newly created order
     * @throws mthree.flooringmastery.dao.DataPersistenceException 
     * @throws mthree.flooringmastery.service.DataValidationException 
     */
    Order createOrder(String[] newOrderDetails) throws 
            DataPersistenceException,
            DataValidationException;
    
    /**
     * Add the passed order to memory and file.
     * 
     * @param orderToAdd order that should be added
     * @param processType indicates what occurred to this order prior to
     * its addition (either edited or created)
     * @throws DataPersistenceException 
     */
    void addOrder(Order orderToAdd, String processType) throws DataPersistenceException;
    
    /**
     * Edit and return the given order with the passed new details.Ensure new order data is valid.
     * 
     * @param orderNumber
     * @param newDetails
     * @return
     * @throws DataPersistenceException
     * @throws DataValidationException
     */
    Order editOrder(int orderNumber, String[] newDetails) throws 
            DataPersistenceException,
            DataValidationException;
    
    /**
     * Set status of given order to cancelled.
     * 
     * @param order order to cancel
     * @throws DataPersistenceException
     * @throws OrderAlreadyCancelledException
     */
    void cancelOrder(Order order) throws 
            DataPersistenceException,
            OrderAlreadyCancelledException;
    
    /**
     * Export order data to predetermined file.
     * 
     * @throws DataPersistenceException 
     */
    void exportData() throws DataPersistenceException;
    
}
