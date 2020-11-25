/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Audit DAO for this application - handles writing events such as order creation
 * to log file.
 * 
 * @author utkua
 */
public class FlooringMasteryAuditDaoFileImpl implements FlooringMasteryAuditDao {

    public final String AUDIT_FILE;
    
    /**
     * Sets file audit DAO will read from to default.
     */
    public FlooringMasteryAuditDaoFileImpl() {
        AUDIT_FILE = "Audit/audit.txt";
    }
    
    /**
     * Sets file read from to passed file name.
     * @param AUDIT_FILE file name for audit DAO to read from.
     */
    public FlooringMasteryAuditDaoFileImpl(String AUDIT_FILE) {
        this.AUDIT_FILE = AUDIT_FILE;
    }
   
    @Override
    public void writeAuditEntry(String entry) throws DataPersistenceException {
        PrintWriter out;
       
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new DataPersistenceException("Could not persist audit information.", e);
        }
 
        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }  
    
}
