/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import mthree.flooringmastery.dto.Costs;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;
import mthree.flooringmastery.dto.Statuses;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for methods of the DAO file implementation.
 * 
 * @author utkua
 */
public class FlooringMasteryOrderDaoFileImplTest {
    
    FlooringMasteryOrderDao testDao;
    
    public FlooringMasteryOrderDaoFileImplTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    /**
     * Makes test folders and files then prints some dummy items for testing.
     * 
     * @throws IOException 
     */
    @BeforeEach
    public void setUp() throws IOException {
        
        String backupDirectory =  "TestBackup\\";
        
        String ordersFolder = "TestOrders\\";
        File ordersFile = new File(ordersFolder);
        ordersFile.mkdir();
        
        String orderFile1 = "Orders_06012013.txt";
        String orderFile2 = "Orders_06022013.txt";
        
        // Use the FileWriter to quickly blank each file
        new FileWriter(ordersFolder + orderFile1);
        new FileWriter(ordersFolder + orderFile2);
        
        PrintWriter out1 = new PrintWriter(new FileWriter(ordersFolder + orderFile1, false));
        // Print 2 orders to order file 1
        out1.println("OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total\n"
                + "1,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06\n" 
                + "2,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,TX,4.45,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06");
        // force PrintWriter to write line to the file
        out1.flush();
        
        PrintWriter out2 = new PrintWriter(new FileWriter(ordersFolder + orderFile2, false));
        // Print 2 order to order file 2
        out2.println("OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total\n"
                + "3,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06\n" 
                + "4,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06");
        // force PrintWriter to write line to the file
        out2.flush();
        testDao = new FlooringMasteryOrderDaoFileImpl(ordersFolder, backupDirectory);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test that Order known to be within order files can be retrieved,
     * and that getting an invalid Order returns null.
     * Also tests getState and getProduct implicitly.
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testGetOrder() throws DataPersistenceException {
        
        Order testOrder = makeTestOrder(1);
        
        // Retrieve order that should have same fields as testOrder
        Order retrievedOrder = testDao.getOrder(testOrder.getOrderNumber());
        // Check the data is equal
        assertOrdersEqual(testOrder, retrievedOrder);
        
        // Ensure that attempting to retrieve a non-existant order returns null.
        retrievedOrder = testDao.getOrder(10);
        assertNull(retrievedOrder, "Checking that invalid order retrieval is null.");
    }
    
    /**
     * Test that Order known to be within inventory can be retrieved.
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testGetOrders() throws DataPersistenceException {
        
        // Retrieve the list of all Orders within the DAO
        List<Order> retrievedOrders = testDao.getOrders();
        
        // First check the general contents of the list
        // We know what has been added upon setup
        assertNotNull(retrievedOrders, "The list of Orders must not be null.");
        assertEquals(4, retrievedOrders.size(),"List of Order should have 4 Orders. It currently has " 
                + retrievedOrders.size() + ".");
        
        // Make orders with known values
        Order testOrder1 = makeTestOrder(1);
        Order testOrder2 = makeTestOrder(2);
        // Set state differently as retrieved order should have this state
        // The makeTestOrder method makes a generic order with a different state
        // State name cannot be compared here as the Order DAO does not retrieve state name
        testOrder2.setState(new State("TX", "", new BigDecimal("4.45")));
        
        // Get orders from list
        Order retrievedOrder1 = retrievedOrders.get(0);
        Order retrievedOrder2 = retrievedOrders.get(1);
        
        // Compare the test orders to the respective retreived orders
        assertOrdersEqual(testOrder1, retrievedOrder1);
        assertOrdersEqual(testOrder2, retrievedOrder2);
    }
    
    /**
     * Test that Order known to be within inventory can be added,
     * then the same Order retrieved.
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testAddOrder() throws DataPersistenceException {
        // Make orders with known values
        Order testOrder = makeTestOrder(5);
        
        testDao.addOrder(5, testOrder);
        
        // Get the added order
        Order retrievedOrder = testDao.getOrder(5);
        
        // Compare the test order to the respective retreived order
        assertOrdersEqual(testOrder, retrievedOrder);
    }
    
    /**
     * Test that exportData produces the correct output.
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testExportData() throws DataPersistenceException {
        // Ensure existing orders are loaded into memory
        testDao.getOrders();
        // Do the export
        testDao.exportData();
        
        // Scanner from java.util.Scanner
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    // BuferredRead from java.io.BufferedReader
                    new BufferedReader(
                            // Filereader from java.io.FileReader
                            new FileReader("TestBackup\\DataExport.txt")));
        } catch (FileNotFoundException e) { 
            // Translate FileNotFoundException
            throw new DataPersistenceException(
                    "Could not load order data into memory.", e);
        }
        
        // currentLine holds the most recent line read from the file
        String currentLine = "";
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine += scanner.nextLine();
        }
        // close scanner
        scanner.close();
        
        String correctData = 
            "OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate" +
            "1,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.062013-06-01" +
            "2,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,TX,4.45,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.062013-06-01" +
            "3,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.062013-06-02" +
            "4,Ada Lovelace,2014-04-28T16:00:49,ACTIVE,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.062013-06-02";
        
        assertEquals(correctData, currentLine, "Testing that exported data is correct.");
    }
    
    /**
     * Test whether a pair of orders are equal.
     * Each field tested separately for granularity of feedback.
     * 
     * @param testOrder correct Order
     * @param retrievedOrder compared Order
     */
    private void assertOrdersEqual(Order testOrder, Order retrievedOrder) {
        assertEquals(testOrder.getOrderNumber(),
                    retrievedOrder.getOrderNumber(),
                    "Checking order number.");
        assertEquals(testOrder.getCustomerName(),
                    retrievedOrder.getCustomerName(),
                    "Checking order customer.");
        assertEquals(testOrder.getArea(),
                    retrievedOrder.getArea(),
                    "Checking order area.");
        assertEquals(testOrder.getState(), 
                    retrievedOrder.getState(),
                    "Checking order State object.");
        assertEquals(testOrder.getProduct(), 
                    retrievedOrder.getProduct(),
                    "Checking order Product object.");
        assertEquals(testOrder.getCosts(), 
                    retrievedOrder.getCosts(),
                    "Checking order Costs object.");
        assertEquals(testOrder.getDate(), 
                    retrievedOrder.getDate(),
                    "Checking order dates.");
        assertEquals(testOrder.getCreationDateTime(), 
                    retrievedOrder.getCreationDateTime(),
                    "Checking order creation times.");
        assertEquals(testOrder.getStatus(), 
                    retrievedOrder.getStatus(),
                    "Checking order statuses.");
    }
    
    /**
     * Creates and returns a generic test order with passed order number.
     * 
     * @param orderNumber order number to be given to created order
     * @return created order
     * @throws DataPersistenceException 
     */
    private Order makeTestOrder(int orderNumber) throws DataPersistenceException {
        LocalDateTime testCreationDate = LocalDateTime.parse("2014-04-28T16:00:49");
        LocalDate testOrderDate = LocalDate.parse("06012013", DateTimeFormatter.ofPattern("MMddyyyy"));
        
        // Make test order with same fields as known order in file
        Order testOrder = new Order(orderNumber, testCreationDate, testOrderDate);

        testDao.getOrders();

        State testState = new State("CA", "", new BigDecimal("25.00"));
        Product testProduct = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        
        Costs testCosts = new Costs();
        testCosts.setMaterialCost(new BigDecimal("871.50"));
        testCosts.setLabourCost(new BigDecimal("1033.35"));
        testCosts.setTaxCost(new BigDecimal("476.21"));
        testCosts.setTotal(new BigDecimal("2381.06"));
        
        testOrder.setCustomerName("Ada Lovelace");
        testOrder.setStatus(Statuses.ACTIVE);
        testOrder.setArea(new BigDecimal("249.00"));
        testOrder.setProduct(testProduct);
        testOrder.setState(testState);
        testOrder.setCosts(testCosts);
        
        return testOrder;
    }
    
}
