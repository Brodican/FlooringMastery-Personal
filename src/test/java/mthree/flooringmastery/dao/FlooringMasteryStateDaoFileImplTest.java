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
import mthree.flooringmastery.dto.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for method of the State DAO file implementation.
 * 
 * @author utkua
 */
public class FlooringMasteryStateDaoFileImplTest {
    
    FlooringMasteryStateDao testStateDao;
    
    public FlooringMasteryStateDaoFileImplTest() {
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
        
        String taxesDirectory = dataFolder + "Taxes.txt";
        
        // Use the FileWriter to quickly blank each file
        new FileWriter(taxesDirectory);
        
        PrintWriter outData = new PrintWriter(new FileWriter("TestData\\Taxes.txt", false));
        // Print states to taxes file
        outData.println("State,StateName,TaxRate\n" +
            "TX,Texas,4.45\n" +
            "WA,Washington,9.25\n" +
            "KY,Kentucky,6.00\n" +
            "CA,Calfornia,25.00");
        // force PrintWriter to write line to the file
        outData.flush();
        
        testStateDao = new FlooringMasteryStateDaoFileImpl(taxesDirectory);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    /**
     * Test that State known to be within order files can be retrieved,
     * and that getting an invalid state returns null.
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testGetState() throws DataPersistenceException {
        
        State testState = new State("CA", "Calfornia", new BigDecimal("25.00"));
        
        // Retrieve state that should have same fields as testState
        State retrievedState = testStateDao.getState(testState.getStateAbbreviation());
        // Check the data is equal
        assertStatesEqual(testState, retrievedState);
        
        // Ensure that attempting to retrieve a non-existant order returns null.
        retrievedState = testStateDao.getState("XX");
        assertNull(retrievedState, "Checking that invalid state retrieval is null.");
    }
    
    /**
     * Test whether a pair of states are equal.
     * Each field tested separately for granularity of feedback.
     * 
     * @param testState correct State
     * @param retrievedState compared State
     */
    private void assertStatesEqual(State testState, State retrievedState) {
        assertEquals(testState.getStateAbbreviation(),
                    retrievedState.getStateAbbreviation(),
                    "Checking state abbreviation.");
        assertEquals(testState.getStateName(),
                    retrievedState.getStateName(),
                    "Checking state name.");
        assertEquals(testState.getStateTaxRate(),
                    retrievedState.getStateTaxRate(),
                    "Checking state tax rate.");
    }
    
}
