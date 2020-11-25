/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.service;

import java.math.BigDecimal;
import mthree.flooringmastery.dao.DataPersistenceException;
import mthree.flooringmastery.dao.FlooringMasteryProductDao;
import mthree.flooringmastery.dto.Product;

/**
 * Stub implementation of Product DAO for testing purposes.
 * 
 * @author utkua
 */
public class FlooringMasteryProductDaoStubImpl implements FlooringMasteryProductDao {
    
    private final Product testProduct;
    
    public FlooringMasteryProductDaoStubImpl() {
        testProduct = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
    }
    
    @Override
    public Product getProduct(String productType) throws DataPersistenceException {
        if (productType.equals(testProduct.getProductType())) {
            return testProduct;
        } else {
            return null;
        }
    }
    
}
