/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import java.time.LocalDate;
import java.util.List;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;

/**
 * Interface with outline of DAO for this application.
 * 
 * @author utkua
 */
public interface FlooringMasteryOrderDao {
    
    /**
     * Retrieves order Object with passed order number.
     * 
     * @param orderNumber
     * @return
     * @throws DataPersistenceException
     */
    Order getOrder(int orderNumber) throws DataPersistenceException;
    
    /**
     * Retrieves a list of all Order objects currently stored (in memory or file).
     * 
     * @return list of all Order objects
     * @throws mthree.flooringmastery.dao.DataPersistenceException
     */
    List<Order> getOrders() throws DataPersistenceException;
    
    /**
     * Adds an Order object with given order number to memory and file.
     * 
     * @param orderNumber order number of order, used as key for HashMap
     * @param order order object to be added
     * @return added Order object
     * @throws mthree.flooringmastery.dao.DataPersistenceException
     */
    Order addOrder(int orderNumber, Order order) throws DataPersistenceException;
    
    /**
     * Export order data to predetermined file.
     * 
     * @throws DataPersistenceException 
     */
    void exportData() throws DataPersistenceException;
    
}
