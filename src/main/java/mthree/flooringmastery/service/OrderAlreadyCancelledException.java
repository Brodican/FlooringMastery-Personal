/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.service;

/**
 * Exception thrown when attempt to cancel a cancelled order occurs.
 * 
 * @author utkua
 */
public class OrderAlreadyCancelledException extends Exception {

    public OrderAlreadyCancelledException(String message) {
        super(message);
    }

    public OrderAlreadyCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
