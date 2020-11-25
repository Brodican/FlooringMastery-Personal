/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.service;

import java.math.BigDecimal;
import mthree.flooringmastery.dao.FlooringMasteryStateDao;
import mthree.flooringmastery.dto.State;

/**
 * Stub implementation of State DAO for testing purposes.
 * 
 * @author utkua
 */
public class FlooringMasteryStateDaoStubImpl implements FlooringMasteryStateDao {
    
    private final State testState;
    
    public FlooringMasteryStateDaoStubImpl() {
        testState = new State("CA", "Calfornia", new BigDecimal("25.00"));
    }
    
    public State getState(String stateAbbreviation) {
        if (stateAbbreviation.equals(testState.getStateAbbreviation())) {
            return testState;
        }
        
        return null;
    }
    
}
