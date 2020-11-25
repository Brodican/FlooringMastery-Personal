/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import mthree.flooringmastery.dto.Product;

/**
 * Interface with outline of Product DAO for this application.
 * 
 * @author utkua
 */
public interface FlooringMasteryProductDao {

    /**
     * Retrieves Product object with passed product type.
     * 
     * @param productType type of product to retrieve
     * @return Product object with passed type
     * @throws DataPersistenceException
     */
    Product getProduct(String productType) throws DataPersistenceException;
    
}
