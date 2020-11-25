/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import mthree.flooringmastery.dto.State;

/**
 * DAO for reading from states (taxes) file.
 * Converts information in this file into State objects in memory.
 * 
 * @author utkua
 */
public class FlooringMasteryStateDaoFileImpl implements FlooringMasteryStateDao {

    // Hashmap from library java.util.HashMap
    // Map from java.util.Map
    // HashMaps to store orders, products, states in memory
    HashMap<String, State> states = new HashMap<>();
    
    // Directory to be used by this DAO, points to States file
    public final String STATE_DIR;
    // Delimiter used when marshalling/unmarshalling
    // Won't change
    public final String DELIMITER = ",";
    
    /**
     * Constructs a FlooringMasteryStateDaoFileImpl object with default directory for Taxes file.
     */
    public FlooringMasteryStateDaoFileImpl() {
        this.STATE_DIR = "Data/Taxes.txt";
    }
    
    /**
     * Constructs a FlooringMasteryStateDaoFileImpl object with passed custom file name
     * for taxes directory.
     * 
     * @param STATE_DIR 
     */
    public FlooringMasteryStateDaoFileImpl(String STATE_DIR) {
        this.STATE_DIR = STATE_DIR;
    }
    
    @Override
    public State getState(String stateAbbreviation) throws DataPersistenceException {
        loadTaxFile();
        return states.get(stateAbbreviation);
    }
    
    /**
     * Load states in the taxes file to memory, converting strings
     * of state fields to State objects.
     * 
     * @throws DataPersistenceException 
     */
    private void loadTaxFile() throws DataPersistenceException {
        // Scanner from java.util.Scanner
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    // BuferredRead from java.io.BufferedReader
                    new BufferedReader(
                            // Filereader from java.io.FileReader
                            new FileReader(STATE_DIR)));
        } catch (FileNotFoundException e) { 
            // Translate FileNotFoundException
            throw new DataPersistenceException(
                    "Could not load state data into memory.", e);
        }
        
        // currentLine holds the most recent line read from the file
        String currentLine;
        
        State currentState;
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a State
            currentState = unmarshallState(currentLine);
            // Put currentState into the map using state abbreviation as key
            states.put(currentState.getStateAbbreviation(), currentState);
        }
        // close scanner
        scanner.close();
    }
    
    /**
     * Convert a line from the taxes file to a State object.
     * 
     * @param stateAsText line of text from taxes file
     * @return State object created with fields from file line
     * @throws DataPersistenceException 
     */
    private State unmarshallState(String stateAsText) throws DataPersistenceException {
        // split returns string array split on DELIMETER
        String[] stateFields = stateAsText.split(DELIMITER);
        
        State state;
        String stateAbbreviation;
        String stateName;
        BigDecimal stateTaxRate;
        
        try {
            
            // State abbreviation should be in index 0.
            stateAbbreviation = stateFields[0];
            
            // State name should be in index 1.
            stateName = stateFields[1];
            
            // State tax rate should be in index 2.
            stateTaxRate = new BigDecimal(stateFields[2]);
            
            // Make a new state object with input fields.
            state = new State(stateAbbreviation, stateName, stateTaxRate);
            
        } catch (IndexOutOfBoundsException e) {
            // Error translation, helpful message
            throw new DataPersistenceException(
                    "Error in format of a state: try checking field count in tax file.", e);        
        }
        catch ( NumberFormatException e) {
            throw new DataPersistenceException(
                    "Error in format of an state: a field has an incorrect format.", e);
        }

        // Return State created from file
        return state;
    }
    
}
