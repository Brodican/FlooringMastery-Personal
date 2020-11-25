/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.ui;

/**
 * Exception thrown in case of problem when attempting to clear console.
 * 
 * @author utkua
 */
public class ConsoleClearingException extends Exception {

    public ConsoleClearingException(String message) {
        super(message);
    }

    public ConsoleClearingException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
