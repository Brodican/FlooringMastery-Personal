/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import mthree.flooringmastery.dto.Costs;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;

/**
 * Implements view layer of this application.
 * Uses UserIO class to retrieve input from user and return to Controller,
 * and display necessary output to user.
 * 
 * @author utkua
 */
public class FlooringMasteryView {

    // io object used by this view
    private final UserIO io;
    
    // 
    /**
     * Constructor with UserIO object to be used by this view passed as
     * parameter for dependency injection.
     * 
     * @param io UserIO passed to constructor for dependency injection
     */
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    /**
     * Print possible user stories alongside the story choice number.
     * Get numeric choice from user, where choice can be between 1-6.
     * 
     * @return user choice from list of choices.
     */
    public int printMenuAndGetSelection() {
        
        io.print("\n***********************");
        io.print("\n* <<Flooring Program>>");
        io.print("\n* 1. Display Orders");
        io.print("\n* 2. Add an Order");
        io.print("\n* 3. Edit an Order");
        io.print("\n* 4. Cancel an Order");
        io.print("\n* 5. Export All Data");
        io.print("\n* 6. Quit");
        io.print("\n***********************\n");

        return io.readInt("Please select from the above choices: ", 1, 6);
    }
    
    /**
     * Get a choice of date from the user, in the specified format.
     * 
     * @param format format user must input date in
     * @return date input by user
     */
    public LocalDate getDateChoice(String format) {
        return io.readLocalDate("Please enter order completion date with format: " + format + " ", format);
    }
    
    /**
     * Get choice of order number from user.
     * Order numbers start at 1, and end at
     * 
     * @return order number input by user
     */
    public int getOrderNumber() {
        // Minimum order is 1, reasonable maximum is 1000000000
        return io.readIntWithMessages("Please enter order number: ", 1, 1000000000, 
                "Order numbers start at 1: ", "Order number limit is 1000000000: ");
    }
    
    /**
     * Get all info for an Order from the user.
     * 
     * @param format format user must input date in
     * @param min minimum value that can be input for the are BigDecimal
     * @param max maximum value that can be input for the area BigDecimal
     * @param maxScale maximum scale of input area BigDecimal
     * @return input values stored in an array
     * @throws mthree.flooringmastery.ui.ConsoleClearingException
     */
    public String[] getNewOrderInfo(String format, int min, int max, int maxScale) throws ConsoleClearingException {
        LocalDate date = getDateChoice(format);
        String customerName = io.readString("Please enter customer name: ");
        String stateName = io.readString("Please enter state abbreviation: ");
        String productType = io.readString("Please enter type of product for flooring: ");
        
        BigDecimal area = io.readBigDecimal("Please enter flooring area in square feet (minimum 100): ",
                "Input must be less than " + max + " and must have less than: " + maxScale + " decimal places: ", 
                min, max, maxScale);
        
        String[] valuesArr = new String[5];
        valuesArr[0] = date.format(DateTimeFormatter.ofPattern(format));
        valuesArr[1] = customerName;
        valuesArr[2] = stateName;
        valuesArr[3] = productType;
        valuesArr[4] = area.toString();
        
        io.readString("\nPlease press enter to continue: ");
        io.clearConsole();
        
        return valuesArr;
    }
    
    /**
     * Get edit info for an Order from the user, presenting user with the
     * existing value for each field they are to input a new value for.
     * 
     * @param order order that will be edited
     * @param format format user must input date in
     * @param min minimum value that can be input for the are BigDecimal
     * @param max maximum value that can be input for the area BigDecimal
     * @param maxScale maximum scale of input area BigDecimal
     * @return input values stored in an array
     * @throws mthree.flooringmastery.ui.ConsoleClearingException
     */
    public String[] getEditOrderInfo(Order order, String format, int min, int max, int maxScale) throws ConsoleClearingException {
        String customerName = io.readString("Please enter customer name (" + order.getCustomerName() + "): ");
        String stateName = io.readString("Please enter state abbreviation (" + order.getState().getStateAbbreviation() + "): ");
        String productType = io.readString("Please enter type of product for flooring (" + order.getProduct().getProductType() + "): ");
        
        BigDecimal area = io.readBigDecimalOrEmpty("Please enter flooring area in square feet (" + order.getArea() + "): ",
                "Input must be less than " + max + " and must have at most: " + maxScale + " decimal places: ", 
                min, max, maxScale);
        
        
        String[] valuesArr = new String[4];
        valuesArr[0] = customerName;
        valuesArr[1] = stateName;
        valuesArr[2] = productType;
        if (area == null) {
            valuesArr[3] = "";
        } else {
            valuesArr[3] = area.toString();
        }
        
        io.readString("\nPlease press enter to continue: ");
        io.clearConsole();
        
        return valuesArr;
    }
    
    /**
     * Get user input on whether they want try again.
     * Called upon certain input errors - user may either try a different 
     * input or move on.
     * 
     * @return user choice on whether they want to continue.
     */
    public Boolean getTryAgainChoice() throws ConsoleClearingException {
        boolean choice = io.readYesNo("Would you like to try again? y/n: ").equals("y");
        io.clearConsole();
        return choice;
    }
    
    /**
     * Display List of all Orders at a date.
     * 
     * @param orderList list of orders to be printed
     * @throws mthree.flooringmastery.ui.ConsoleClearingException
     */
    public void displayOrderList(List<Order> orderList) throws ConsoleClearingException {
        if (orderList.isEmpty()) {
            io.print("\nNo orders to be completed at given date.");
            io.readString("\nPlease press enter to continue: ");
            io.clearConsole();
            return;
        }
        
        String headerTitles = "OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,"
                + "Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
        io.print(headerTitles);
        
        orderList.forEach((currentOrder) -> {
            displayOrderHorizontal(currentOrder);
        });
        
        io.readString("\nPlease press enter to continue: ");
        io.clearConsole();
    }
    
    /**
     * Displays data of given order, and returns user confirmation of given process on order.
     * 
     * @param order order to display data of
     * @param processType type of process that is happening to this order - edit, create, cancel
     * @return 
     */
    public boolean displayOrderAndConfirm(Order order, String processType) {
        displayOrderVertical(order);
        String confirmation = io.readYesNo("Are you sure you wish to " + processType + " this order (y/n): ");
        return confirmation.equals("y");
    }
    
    /**
     * Display passed Order object's data as a single horizontal line.
     * 
     * @param order order to display
     */
    private void displayOrderHorizontal(Order order) {
        Product product = order.getProduct();
        State state = order.getState();
        Costs costs = order.getCosts();
        
        // Format method to avoid clunky concatenation
        String orderInfo = String.format("\n%s %s %s %s %s %s %s %s %s %s %s %s %s %s",
            order.getOrderNumber(),
            order.getCustomerName(),
            order.getCreationDateTime(),
            order.getStatus(),
            state.getStateAbbreviation(),
            state.getStateTaxRate(),
            product.getProductType(),
            order.getArea(),
            product.getCostPerSquareFoot(),
            product.getLabourCostPerSquareFoot(),
            costs.getMaterialCost(),
            costs.getLabourCost(),
            costs.getTaxCost(),
            costs.getTotal());
        io.print(orderInfo);
    }
    
    /**
     * Displays a single order with each of its field names and contents vertically.
     * 
     * @param order order that must have its details printed
     */
    private void displayOrderVertical(Order order) {
        Product product = order.getProduct();
        State state = order.getState();
        Costs costs = order.getCosts();
        // Format method to avoid clunky concatenation
        String orderInfo = String.format("Order Number: %s\n"
                + "Customer Name: %s\n"
                + "Creation Date: %s\n"
                + "Status: %s\n"
                + "State Abbreviation: %s\n"
                + "State Tax Rate: %s\n"
                + "Product Type: %s\n"
                + "Flooring Area: %s square feet\n"
                + "Product Cost per square foot: $%s\n"
                + "Product Labour Cost per square foot: $%s\n"
                + "Material Cost: $%s\n"
                + "Labour Cost: $%s\n"
                + "Tax Cost: $%s\n"
                + "Total Cost: $%s\n",
            order.getOrderNumber(),
            order.getCustomerName(),
            order.getCreationDateTime(),
            order.getStatus(),
            state.getStateAbbreviation(),
            state.getStateTaxRate(),
            product.getProductType(),
            order.getArea(),
            product.getCostPerSquareFoot(),
            product.getLabourCostPerSquareFoot(),
            costs.getMaterialCost(),
            costs.getLabourCost(),
            costs.getTaxCost(),
            costs.getTotal());
        io.print(orderInfo);
    }
    
    /**
     * Inform user of error with descriptive message.
     * 
     * @param message description of error.
     */
    public void displayErrorMessage(String message) {
        io.print("=== ERROR ===\n" + message + "\n");
    }
    
    /**
     * Inform user of wrong input, with descriptive message.
     * Useful when UserIO cannot automatically catch wrong input,
     * or if UserIO wrong input message is not sufficiently descriptive.
     * 
     * @param message describes the nature of the user's wrong input.
     */
    public void displayWrongInputMessage(String message) {
        io.print("Wrong input: " + message + "\n");
    }
    
    /**
     * Inform user application is to exit, and thank them.
     */
    public void displayExitMessage() {
        io.print("\nThank you for using this TSG Corp application.\n");
    }
    
    // Various banners to inform user of what is occuring
    public void displayDisplayAllAtDateBanner(String date) {
        io.print("\n=== Displaying Orders at Date: " + date + " ===\n");
    }
    
    public void displayCreateOrderBanner() {
        io.print("\n=== Create Order ===\n");
    }
    
    public void displayCreateSuccessBanner() throws ConsoleClearingException {
        io.readString("Order successfully created. Please hit enter to continue");
        io.clearConsole();
    }
    
    public void displayEditOrderBanner() {
        io.print("\n=== Edit Order ===\n");
    }
    
    public void displayEditSuccessBanner() throws ConsoleClearingException {
        io.readString("Order successfully edited. Please hit enter to continue");
        io.clearConsole();
    }
    
    public void displayNoEditSuccessBanner() throws ConsoleClearingException {
        io.readString("No changes to order as inputs blank.  Please hit enter to continue");
        io.clearConsole();
    }
    
    public void displayCancelOrderBanner() {
        io.print("\n=== Cancel Order ===\n");
    }
    
    public void displayCancelSuccessBanner() throws ConsoleClearingException {
        io.readString("Order successfully cancelled. Please hit enter to continue");
        io.clearConsole();
    }
    
    public void displayExportDataBanner() {
        io.print("\n=== Export Data ===\n");
    }
    
    public void displayExportSuccessBanner() throws ConsoleClearingException {
        io.readString("Data successfully exported. Please hit enter to continue");
        io.clearConsole();
    }
    
    public void displayUnknownCommandBanner() {
        io.print("\n=== Unknown command ===\n");
    }
    
}
