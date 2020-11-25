/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import mthree.flooringmastery.dto.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the method of the Product DAO file implementation.
 * 
 * @author utkua
 */
public class FlooringMasteryProductDaoFileImplTest {
    
    FlooringMasteryProductDao testProductDao;
    
    public FlooringMasteryProductDaoFileImplTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    /**
     * Make test folder and file then print some dummy items for testing.
     * 
     * @throws IOException 
     */
    @BeforeEach
    public void setUp() throws IOException {
        String dataFolder = "TestData\\";
        File dataFile = new File(dataFolder);
        dataFile.mkdir();
        
        String productsDirectory = dataFolder + "Products.txt";
        
        // Use the FileWriter to quickly blank each file
        new FileWriter(productsDirectory);
        
        PrintWriter outData = new PrintWriter(new FileWriter("TestData\\Products.txt", false));
        // Print products to products file
        outData.println("ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n" +
            "Carpet,2.25,2.10\n" +
            "Laminate,1.75,2.10\n" +
            "Tile,3.50,4.15\n" +
            "Wood,5.15,4.75");
        // force PrintWriter to write line to the file
        outData.flush();
        
        testProductDao = new FlooringMasteryProductDaoFileImpl(productsDirectory);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    /**
     * Test that Product known to be within order files can be retrieved,
     * and that getting an invalid product returns null.
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testGetProduct() throws DataPersistenceException {
        
        Product testProduct = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        
        // Retrieve product that should have same fields as testProduct
        Product retrievedProduct = testProductDao.getProduct(testProduct.getProductType());
        // Check the data is equal
        assertProductsEqual(testProduct, retrievedProduct);
        
        // Ensure that attempting to retrieve a non-existant order returns null.
        retrievedProduct = testProductDao.getProduct("XX");
        assertNull(retrievedProduct, "Checking that invalid product retrieval is null.");
    }
    
    /**
     * Test whether a pair of products are equal.
     * Each field tested separately for granularity of feedback.
     * 
     * @param testProduct correct Product
     * @param retrievedProduct compared Product
     */
    private void assertProductsEqual(Product testProduct, Product retrievedProduct) {
        assertEquals(testProduct.getProductType(),
                    retrievedProduct.getProductType(),
                    "Checking product type.");
        assertEquals(testProduct.getCostPerSquareFoot(),
                    retrievedProduct.getCostPerSquareFoot(),
                    "Checking product cost per square foot.");
        assertEquals(testProduct.getLabourCostPerSquareFoot(),
                    retrievedProduct.getLabourCostPerSquareFoot(),
                    "Checking product labour cost per square foot.");
    }
    
}
