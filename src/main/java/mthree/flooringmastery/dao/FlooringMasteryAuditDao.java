/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

/**
 * Interface for the audit DAO of this application.
 * 
 * @author utkua
 */
public interface FlooringMasteryAuditDao {
    
    /**
     * Writes passed entry String to audit log file.
     * 
     * @param entry
     */
    void writeAuditEntry(String entry) throws DataPersistenceException;
    
}
