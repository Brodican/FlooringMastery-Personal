/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * @author utkua
 */
public class UserIOConsoleImpl implements UserIO {

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.print(prompt);
        String output;
        Scanner s;
        
        // External library java.util.Scanner
        s = new Scanner(System.in);
        // Get output from use
        output = s.nextLine();
        
        return output;
    }
    
    @Override
    public String readYesNo(String prompt) {
        System.out.print(prompt);
        String output;
        Scanner s;
        
        do {   
            s = new Scanner(System.in);
            output = s.nextLine();
            if (output.equals("y") || output.equals("n")) {
                // Correct input - can now exit loop
                break;
            } else {
                System.out.print("Input must be be either y or n: ");
            }
        } while (true);
        
        return output;
    }
    
    @Override
    public LocalDate readLocalDate(String prompt, String format) {
        System.out.print(prompt);
        // External library java.time.LocalDate
        LocalDate output;
        Scanner s;
        
        do {
            try {
                s = new Scanner(System.in);
                output = LocalDate.parse(s.nextLine(), DateTimeFormatter.ofPattern(format));
                break;
            } catch (DateTimeParseException e) {
                System.out.print("Input date must be of format " + format + ": ");
            }
        } while (true);
        
        return output;
    }
    
    @Override
    public BigDecimal readBigDecimal(String prompt, String invalidInputResponse, int min, int max, int maxScale) {
        System.out.print(prompt);
        // External library java.math.BigDecimal
        BigDecimal output;
        Scanner s;
        
        do {
            try {
                s = new Scanner(System.in);
                output = new BigDecimal(s.nextLine());
                
                // If input invalid, continue to take input again
                if (output.compareTo(new BigDecimal(max)) > 0) {
                    System.out.print("Input must be less than " + max + ": ");
                    continue;
                } else if (output.compareTo(new BigDecimal(min)) < 0) {
                    System.out.print("Input must be greater than " + min + ": ");
                    continue;
                } else if (output.scale() > maxScale) {
                    System.out.print("Must have " + maxScale + " decimal points or less: ");
                    continue;
                }
                
                // Input is valid, break out of loop then return
                break;
            } catch (NumberFormatException e) {
                System.out.print("Must input a BigDecimal: ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public BigDecimal readBigDecimalOrEmpty(String prompt, String invalidInputResponse, int min, int max, int maxScale) {
        System.out.print(prompt);
        // External library java.math.BigDecimal
        String outputString;
        BigDecimal output;
        Scanner s;
        
        do {
            try {
                s = new Scanner(System.in);
                outputString = s.nextLine();
                if (outputString.equals("")) {
                    return null;
                }
                
                output = new BigDecimal(outputString);
                
                // If input invalid, continue to take input again
                if (output.compareTo(new BigDecimal(max)) > 0) {
                    System.out.print("Input must be less than " + max + ": ");
                    continue;
                } else if (output.compareTo(new BigDecimal(min)) < 0) {
                    System.out.print("Input must be greater than " + min + ": ");
                    continue;
                } else if (output.scale() > maxScale) {
                    System.out.print("Must have " + maxScale + " decimal points or less: ");
                    continue;
                }
                
                // Input is valid, break out of loop then return
                break;
            } catch (NumberFormatException e) {
                System.out.print("Must input a BigDecimal: ");
            }
        } while (true);
        
        return output;
    }
    
    @Override
    public int readInt(String prompt) {
        System.out.print(prompt);
        Scanner s;
        int output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Input must be an integer: ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        System.out.print(prompt);
        Scanner s;
        int output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextInt();
                if ((output >= min) &&(output <= max)) {
                    break;
                } else {
                    System.out.print("Input must be between " + min + " and " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Input must be an integer: ");
            }
        } while (true);
        
        return output;
    }
    
    @Override
    public int readIntWithMessages(String prompt, int min, int max, String tooSmall, String tooLarge) {
        System.out.print(prompt);
        Scanner s;
        int output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextInt();
                if (output < min) {
                    System.out.print(tooSmall);
                    continue;
                } else if (output > max) {
                    System.out.print(tooLarge);
                    continue;
                }
                
                break;
            } catch (InputMismatchException e) {
                System.out.print("Input must be an integer (in Java, an integer is at most 2,147,483,647): ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public double readDouble(String prompt) {
        System.out.print(prompt);
        Scanner s;
        double output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Input must be a double: ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        System.out.print(prompt);
        Scanner s;
        double output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextDouble();
                if ((output >= min) &&(output <= max)) {
                    break;
                } else {
                    System.out.print("Input must be between " + min + " and " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Input must be a double: ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public float readFloat(String prompt) {
        System.out.print(prompt);
        Scanner s;
        float output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextFloat();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Input must be a float: ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        System.out.print(prompt);
        Scanner s;
        float output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextFloat();
                if ((output >= min) &&(output <= max)) {
                    break;
                } else {
                    System.out.print("Input must be between " + min + " and " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Input must be a float: ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public long readLong(String prompt) {
        System.out.print(prompt);
        Scanner s;
        long output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextLong();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Input must be a long: ");
            }
        } while (true);
        
        return output;
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        System.out.print(prompt);
        Scanner s;
        long output;
        
        do {   
            try {
                s = new Scanner(System.in);
                output = s.nextLong();
                if ((output >= min) &&(output <= max)) {
                    break;
                } else {
                    System.out.print("Input must be between " + min + " and " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Input must be a long: ");
            }
        } while (true);
        
        return output;
    }
    
    @Override
    public void clearConsole() throws ConsoleClearingException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
}
