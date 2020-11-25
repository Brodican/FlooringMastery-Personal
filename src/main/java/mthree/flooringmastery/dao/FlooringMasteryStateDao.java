/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import mthree.flooringmastery.dto.State;

/**
 * Interface with outline of State DAO for this application.
 * 
 * @author utkua
 */
public interface FlooringMasteryStateDao {
    
    /**
     * Retrieves State object with passed state abbreviation.
     * 
     * @param stateAbbreviation abbreviation of state to retrieve
     * @return Product object with passed type
     * @throws DataPersistenceException
     */
    State getState(String stateAbbreviation) throws DataPersistenceException;
    
}
