/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Class that handles printing output to user and getting input from user.
 * 
 * @author utkua
 */
public interface UserIO {
    
    /**
     * Prints passed message.
     * 
     * @param msg message to be printed
     */
    void print(String msg);
    

    /**
     * Prints prompt and reads user input, which must be a String for return.
     * 
     * @param prompt prompt to print to user
     * @return String input by user
     */
    String readString(String prompt);
    
    /**
     * Prints prompt and reads user input, which must be a y or n for return.
     * 'y' represents yes, 'n' represent no.
     * 
     * @param prompt prompt to print to user
     * @return String ('y' or 'n') input by user
     */
    String readYesNo(String prompt);
    
    /**
     * Prints prompt and reads user input, which must be a LocalDate of specified format for return.
     * 
     * @param prompt prompt to print to user
     * @param format format read date must adhere to
     * @return LocalDate input by user.
     */
    public LocalDate readLocalDate(String prompt, String format);
    
    /**
     * Prints prompt and reads user input, which must be a BigDecimal less than specified limit. If input above the limit, or
     * the scale of the BigDecimal is above the specified max scale, the passed invalid response is printed, 
     * and the user's input is requested again.
     * 
     * @param prompt prompt to print to user
     * @param invalidInputResponse message printed to use upon invalid input
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @param maxScale maximum scale (digits to right of decimal points) of BigDecimal
     * @return 
     */
    public BigDecimal readBigDecimal(String prompt, String invalidInputResponse, int min, int max, int maxScale);
    
    /**
     * Prints prompt and reads user input, which must be a BigDecimal less than specified limit. If input above the limit, or
     * the scale of the BigDecimal is above the specified max scale, the passed invalid response is printed, 
     * and the user's input is requested again. Can also take empty input, returning null.
     * 
     * @param prompt prompt to print to user
     * @param invalidInputResponse message printed to use upon invalid input
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @param maxScale maximum scale (digits to right of decimal points) of BigDecimal
     * @return 
     */
    public BigDecimal readBigDecimalOrEmpty(String prompt, String invalidInputResponse, int min, int max, int maxScale);

    /**
     * Prints prompt and reads user input, which must be a double for return.
     * 
     * @param prompt prompt to print to user
     * @return double input by user
     */
    double readDouble(String prompt);

    /**
     * Prints prompt and reads user input, which must be 
     * a double between min and max for return.
     * 
     * @param prompt prompt to print to user
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @return double input by user
     */
    double readDouble(String prompt, double min, double max);

    /**
     * Prints prompt and reads user input, which must be a float for return.
     * 
     * @param prompt prompt to print to user
     * @return float input by user
     */
    float readFloat(String prompt);

    /**
     * Prints prompt and reads user input, which must be a float for return.
     * 
     * @param prompt prompt to print to user
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @return float input by user
     */
    float readFloat(String prompt, float min, float max);

    /**
     * Prints prompt and reads user input, which must be a int for return.
     * 
     * @param prompt prompt to print to user
     * @return int input by user
     */
    int readInt(String prompt);

    /**
     * Prints prompt and reads user input, which must be a int for return.
     * 
     * @param prompt prompt to print to user
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @return int input by user
     */
    int readInt(String prompt, int min, int max);
    
    /**
     * Prints prompt and reads user input, which must be a int for return.
     * Additionally takes and prints messages for inputs which are outside of the given bounds.
     * 
     * @param prompt prompt to print to user
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @param tooSmall message printed if input below minimum
     * @param tooLarge message printed if input above maximum
     * @return int input by user
     */
    int readIntWithMessages(String prompt, int min, int max, String tooSmall, String tooLarge);

    /**
     * Prints prompt and reads user input, which must be a long for return.
     * 
     * @param prompt prompt to print to user
     * @return long input by user
     */
    long readLong(String prompt);

    /**
     * Prints prompt and reads user input, which must be a long for return.
     * 
     * @param prompt prompt to print to user
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @return long input by user
     */
    long readLong(String prompt, long min, long max);
    
    /**
     * Clears the console - to keep the output clutter-free.
     * 
     * @throws mthree.flooringmastery.ui.ConsoleClearingException
     */
    void clearConsole() throws ConsoleClearingException;
    
}
