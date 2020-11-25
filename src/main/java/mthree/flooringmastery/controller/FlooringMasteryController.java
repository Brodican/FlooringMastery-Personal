/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mthree.flooringmastery.dao.DataPersistenceException;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.service.DataValidationException;
import mthree.flooringmastery.service.FlooringMasteryServiceLayer;
import mthree.flooringmastery.service.NoSuchOrderException;
import mthree.flooringmastery.service.OrderAlreadyCancelledException;
import mthree.flooringmastery.ui.ConsoleClearingException;
import mthree.flooringmastery.ui.FlooringMasteryView;

/**
 *
 * @author utkua
 */
public class FlooringMasteryController {
    
    // View and service layer passed to and used by this controller
    private final FlooringMasteryView view;
    private final FlooringMasteryServiceLayer service;
    
    private final String FORMAT = "MM/dd/yyyy";
    
    private final int MIN_AREA = 100;
    private final int MAX_AREA = 30000000;
    private final int MAX_SCALE = 2;
    
    /**
     * Constructor for dependency injection.
     * 
     * @param view View object used by this Controller
     * @param service Service object used by this Controller
     */
    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }
    
    /**
     * Application loop in this method, where control of application occurs.
     */
    public void run() {
        // Loop until otherwise specified by user
        boolean keepGoing = true;
        int menuSelection;
        try {
            // Ensure data in order files is valid according to tax and product files.
            service.validateData();
            // Loop will run until user inputs 6
            // setting keepGoing to false.
            while (keepGoing) {
                
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        cancelOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (DataPersistenceException | ConsoleClearingException | DataValidationException e) {
            // Ensure user is informed when error occurs
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    /**
     * Prints the menu selection and returns the users choice.
     * 
     * @return int value representing the users choice.
     */
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    /**
     * Prints all orders at a certain date, which is specified by the user.
     * 
     * @throws DataPersistenceException
     * @throws ConsoleClearingException 
     */
    private void displayOrders() throws 
            DataPersistenceException, 
            ConsoleClearingException {
        LocalDate dateChoice = view.getDateChoice(FORMAT);
        List<Order> orderList = service.getOrdersAtDate(dateChoice);
        view.displayDisplayAllAtDateBanner(dateChoice.format(DateTimeFormatter.ofPattern(FORMAT)));
        view.displayOrderList(orderList);
    }
    
    /**
     * Create a new order with user input data, and add it to our order data.
     * 
     * @throws DataPersistenceException 
     */
    private void addOrder() throws DataPersistenceException, ConsoleClearingException {
        // Make view display create banner to inform user
        view.displayCreateOrderBanner();
        boolean hasErrors;
        Order createdOrder;
        
        do {
            String newOrderDetails[] = view.getNewOrderInfo(FORMAT, MIN_AREA, MAX_AREA, MAX_SCALE);
            try {
                createdOrder = service.createOrder(newOrderDetails);
                if (view.displayOrderAndConfirm(createdOrder, "create")) {
                    view.displayCreateSuccessBanner();
                    service.addOrder(createdOrder, "created");
                    hasErrors = false;
                } else {
                    hasErrors = view.getTryAgainChoice();
                }
            } catch (DataValidationException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = view.getTryAgainChoice();
            }
        } while (hasErrors);
    }
    
    /**
     * Edit an existing order chosen by user, with user input data.
     * 
     * @throws DataPersistenceException 
     */
    private void editOrder() throws DataPersistenceException, ConsoleClearingException {
        boolean hasErrors;
        Order chosenOrder;
        Order orderToEdit;
        // Make view display edit banner to inform user
        view.displayEditOrderBanner();

        do {
            LocalDate date = view.getDateChoice(FORMAT);
            int orderNumber = view.getOrderNumber();
            try {
                chosenOrder = service.getOrder(orderNumber, date);
                String newOrderDetails[] = view.getEditOrderInfo(chosenOrder,FORMAT, MIN_AREA, MAX_AREA, MAX_SCALE);
                orderToEdit = service.editOrder(orderNumber, newOrderDetails);
                if (orderToEdit.equals(chosenOrder)) {
                    view.displayNoEditSuccessBanner();
                    hasErrors = false;
                } else if (view.displayOrderAndConfirm(orderToEdit, "edit")) {
                    view.displayEditSuccessBanner();
                    service.addOrder(orderToEdit, "edited");
                    hasErrors = false;
                } else {
                    hasErrors = view.getTryAgainChoice();
                }
            } catch (DataValidationException | NoSuchOrderException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = view.getTryAgainChoice();
            }
        } while (hasErrors);
    }
    
    /**
     * Cancel an existing order, chosen by the user.
     * 
     * @throws DataPersistenceException 
     */
    private void cancelOrder() throws DataPersistenceException, ConsoleClearingException {
        boolean hasErrors;
        Order orderToCancel;
        // Make view display create banner to inform user
        view.displayCancelOrderBanner();
        
        do {
            LocalDate date = view.getDateChoice(FORMAT);
            int orderNumber = view.getOrderNumber();
            try {
                orderToCancel = service.getOrder(orderNumber, date);
                if (view.displayOrderAndConfirm(orderToCancel, "cancel")) {
                    service.cancelOrder(orderToCancel);
                    view.displayCancelSuccessBanner();
                    hasErrors = false;
                } else {
                    hasErrors = view.getTryAgainChoice();
                }
            } catch (OrderAlreadyCancelledException | NoSuchOrderException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = view.getTryAgainChoice();
            }
        } while (hasErrors);
    }
    
    /**
     * Export order data to a specified output file.
     * 
     * @throws DataPersistenceException 
     */
    private void exportData() throws DataPersistenceException, ConsoleClearingException {
        view.displayExportDataBanner();
        service.exportData();
        view.displayExportSuccessBanner();
    }
    
    /**
     * Informs the user there was an unknown command.
     */
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
    
    /**
     * Has farewell message printed.
     */
    private void exitMessage() {
        view.displayExitMessage();
    }
    
}
