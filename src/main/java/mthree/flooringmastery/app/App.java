/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.app;

import mthree.flooringmastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Class with main method through which application is accessed.
 * 
 * @author utkua
 */
public class App {
    
    /**
     * Main method of the application - starting point.
     * Loads ApplicationContext, prepares test Controller object 
     * with respective bean from said context.
     * 
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() 
        { 
          @Override
          public void run() 
          { 
            System.out.println("\nShutdown."); 
          } 
        });
        
        // Load the XML file with our application context, from resources directory by default
        ApplicationContext ctx = 
           new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // Get controller object from its bean, everything in memory is built
        FlooringMasteryController controller = 
           ctx.getBean("controller", FlooringMasteryController.class);
        
        controller.run();
    }
    
}
