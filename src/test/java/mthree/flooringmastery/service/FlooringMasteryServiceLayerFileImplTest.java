/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mthree.flooringmastery.dao.DataPersistenceException;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class testing each method of the service class.
 * The addOrder() method of the service layer is a pass-through method 
 * which is already tested during the DAO test, and could not be tested here
 * without modifying the service layer to add a useless return.
 * 
 * @author utkua
 */
public class FlooringMasteryServiceLayerFileImplTest {
    
    private FlooringMasteryServiceLayer testService;
    
    /**
     * Loads ApplicationContext, prepares test service layer object 
     * with respective bean from said context.
     */
    public FlooringMasteryServiceLayerFileImplTest() {
        // Load context from resources
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        // Get controller object from its bean, everything in memory is built
        testService = 
                ctx.getBean("serviceLayer", FlooringMasteryServiceLayer.class);
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testValidateData() {
        try {
            testService.validateData();
        } catch (DataPersistenceException ex) {
            Logger.getLogger(FlooringMasteryServiceLayerFileImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataValidationException ex) {
            fail("Data should be valid.");
        }
    }
    
    /**
     * Test the getOrder method of the service layer, ensuring a valid order with the 
     * correct date is correctly retrieved, and an invalid order is not.
     * 
     * @throws DataPersistenceException
     * @throws NoSuchOrderException 
     */
    @Test
    public void testGetOrder() throws DataPersistenceException, NoSuchOrderException {
        String[] newOrderDetails = {"07/01/2022","Ada Lovelace","CA","Tile","249.00"};
        Order testOrder = null;
        Order correctOrder = makeTestOrder(1, "07012022");
        LocalDate testOrderDate = LocalDate.parse("07012022", DateTimeFormatter.ofPattern("MMddyyyy"));
        try {
            testOrder = testService.getOrder(1, testOrderDate);
        } catch (NoSuchOrderException e) {
            fail("Order retrieval should have been successful.");
        }
        
        assertOrdersEqual(correctOrder, testOrder);
        newOrderDetails[2] = "XX";
        
        // Check that correct error is printed in case of attempt to get Order
        // with invalid date.
        try {
            testOrder = testService.getOrder(1, LocalDate.MAX);
        } catch (NoSuchOrderException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: An order with the entered number exists,"
                    + "but its order completion date does not match the entered date.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
        
        // Check that correct error is printed in case of attempt to get Order
        // with non existant order number.
        try {
            testOrder = testService.getOrder(10, testOrderDate);
        } catch (NoSuchOrderException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: No order with entered details exists.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }

    /**
     * Tests that getting all orders gets a list of size 1 - the size the DAO stub gets.
     * Also test that only correct date retrieves the item
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testGetOrdersAtDate() throws DataPersistenceException {
        // Test orders to test against order in list
        Order testOrder1 = makeTestOrder(1, "07012022");
        Order testOrder2 = makeTestOrder(2, "06012022");
        
        // Get orders with date 06012013
        List<Order> orderList = testService.getOrdersAtDate(LocalDate.parse("06012022", DateTimeFormatter.ofPattern("MMddyyyy")));
        
        // Confirm correct size of order list
        assertEquals(1, orderList.size(), "Should only have 1 Order.");
        
        assertOrdersEqual(testOrder2, orderList.get(0));
        
        // Assert that only order with correct date is found in list
        assertTrue(orderList.contains(testOrder2), "List should contain test Order 2.");
        assertFalse(orderList.contains(testOrder1), "List should not contain test Order 1.");
    }
    
    /**
     * Tests that order creation only works with correct data,
     * and that the correct order is created.
     * Effectively, tests data validation of order creation.
     * Also tests the private method getCosts implicitly, as correct
     * costs of a ready order are compared to the created order costs.
     * 
     * @throws mthree.flooringmastery.dao.DataPersistenceException
     * @throws mthree.flooringmastery.service.DataValidationException
     */
    @Test
    public void testCreateOrder() throws DataPersistenceException, DataValidationException {
        String[] newOrderDetails = {"06/01/2022","Ada Lovelace","CA","Tile","249.00"};
        Order testOrder = null;
        Order correctOrder = makeTestOrder(3, "06012022");
        try {
            testOrder = testService.createOrder(newOrderDetails);
        } catch (DataValidationException e) {
            fail("Creation should have been valid.");
        }
        
        assertOrdersEqual(correctOrder, testOrder);
        newOrderDetails[2] = "XX";
        
        // Check that correct error is printed in case of attempt to create Order
        // with invalid data.
        try {
            testOrder = testService.createOrder(newOrderDetails);
        } catch (DataValidationException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: Could not create Order. State XX is not in our tax records.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }
    
    /**
     * Tests that order edit only works with correct data,
     * and that the correct order is resultant.
     * Effectively, tests data validation of order editing.
     * 
     * @throws mthree.flooringmastery.dao.DataPersistenceException
     */
    @Test
    public void testEditOrder() throws DataPersistenceException {
        String[] newOrderDetails = {"Ada Lovelace","CA","Tile","249.00"};
        Order testOrder = null;
        Order correctOrder = makeTestOrder(1, "07012022");
        try {
            testOrder = testService.editOrder(1, newOrderDetails);
        } catch (DataValidationException e) {
            fail("Edit should have been valid.");
        }
        
        assertOrdersEqual(correctOrder, testOrder);
        newOrderDetails[2] = "XX";
        
        // Check that correct error is printed in case of attempt to edit Order
        // with invalid data.
        try {
            testOrder = testService.editOrder(1, newOrderDetails);
        } catch (DataValidationException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: Could not edit Order. Product XX is not in our list of products.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }
    
    /**
     * Tests that order cancelling actually cancels order,
     * and that the error as a result of trying to cancel a
     * cancelled order is correct
     * 
     * @throws mthree.flooringmastery.dao.DataPersistenceException
     * @throws mthree.flooringmastery.service.OrderAlreadyCancelledException
     */
    @Test
    public void testCancelOrder() throws DataPersistenceException, OrderAlreadyCancelledException, NoSuchOrderException {
        Order correctOrder = makeTestOrder(1, "06012022");
        
        try {
            testService.cancelOrder(correctOrder);
        } catch (OrderAlreadyCancelledException e) {
            fail("Cancellation should have been valid.");
        }
        
        Order testOrder = makeTestOrder(1, "06012022");
        testOrder.setStatus(Statuses.CANCELLED);
        
        assertOrdersEqual(correctOrder, testOrder);
        
        // Check that correct error is printed in case of attempt to edit Order
        // with invalid data.
        try {
            testService.cancelOrder(correctOrder);
        } catch (OrderAlreadyCancelledException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: The order was already cancelled.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }
    
    /**
     * Creates and returns a generic test order with passed order number and order date.
     * 
     * @param orderNumber order number to be given to created order
     * @param dateString date order should be created with
     * @return created order
     * @throws DataPersistenceException 
     */
    private Order makeTestOrder(int orderNumber, String dateString) throws DataPersistenceException {
        LocalDateTime testCreationDate = LocalDateTime.now().withNano(0);
        LocalDate testOrderDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMddyyyy"));
        
        // Make test order with same fields as known order in file
        Order testOrder = new Order(orderNumber, testCreationDate, testOrderDate);
        
        State testState = new State("CA", "Calfornia", new BigDecimal("25.00"));
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
                    "Checking order order number.");
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
        // nano seconds must be set to zero as objects made at slightly different times
        assertEquals(testOrder.getCreationDateTime().withNano(0), 
                    retrievedOrder.getCreationDateTime().withNano(0),
                    "Checking order creation times.");
        assertEquals(testOrder.getStatus(), 
                    retrievedOrder.getStatus(),
                    "Checking order statuses.");
    }
    
}
