/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.service;

import mthree.flooringmastery.dao.DataPersistenceException;
import mthree.flooringmastery.dao.FlooringMasteryAuditDao;

/**
 * Stub implementation of Audit DAO for testing purposes.
 * 
 * @author utkua
 */
public class FlooringMasteryAuditDaoStubImpl implements FlooringMasteryAuditDao {

    @Override
    public void writeAuditEntry(String entry) throws DataPersistenceException {
        // Do nothing
    }
    
}
