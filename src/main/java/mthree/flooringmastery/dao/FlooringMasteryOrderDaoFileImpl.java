/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mthree.flooringmastery.dto.Costs;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;
import mthree.flooringmastery.dto.Statuses;

/**
 * DAO for reading and writing to Order storage files,
 * and reading from taxes file and products file.
 * Converts information in these files into Objects in memory,
 * and converts Order objects in memory back to Strings when
 * writing them.
 * 
 * @author utkua
 */
public class FlooringMasteryOrderDaoFileImpl implements FlooringMasteryOrderDao {

    // Hashmap from library java.util.HashMap
    // Map from java.util.Map
    // HashMap to store orders in memory
    HashMap<Integer, Order> orders = new HashMap<>();
    
    // Directories to be used by this DAO
    // Won't change after being set in constructor
    public final String ORDER_DIR;
    public final String BACKUP_DIR;
    // Delimiter used when marshalling/unmarshalling
    // Won't change
    public final String DELIMITER = ",";

    /**
     * Constructs a FlooringMasteryDaoFileImpl object with default file names
     * for order and backup directories.
     */
    public FlooringMasteryOrderDaoFileImpl() {
        // Full directory required when
        this.ORDER_DIR = "Orders/";
        this.BACKUP_DIR = "Backup/";
    }
    
    /**
     * Constructs a FlooringMasteryDaoFileImpl object with passed custom file names
     * for order and backup directories.
     * 
     * @param ORDER_DIR
     * @param BACKUP_DIR
     */
    public FlooringMasteryOrderDaoFileImpl(String ORDER_DIR, String BACKUP_DIR) {
        this.ORDER_DIR = ORDER_DIR;
        this.BACKUP_DIR = BACKUP_DIR;
    }
    
    @Override
    public Order getOrder(int orderNumber) throws DataPersistenceException {
        loadData();
        return orders.get(orderNumber);
    }

    @Override
    public List<Order> getOrders() throws DataPersistenceException {
        loadData();
        return new ArrayList(orders.values());
    }

    @Override
    public Order addOrder(int orderNumber, Order order) throws DataPersistenceException {
        loadData();
        Order createdOrder = orders.put(orderNumber, order);
        writeData();
        return createdOrder;
    }
    
    @Override
    public void exportData() throws DataPersistenceException {
        // Printwriter from java.io.PrintWriter;
        PrintWriter out;
        
        File file = new File(BACKUP_DIR);
        file.mkdir();
        String directory = BACKUP_DIR + "DataExport.txt";

        try {
            new FileOutputStream(directory, true).close();
        } catch (FileNotFoundException e) {
            throw new DataPersistenceException("ERROR: Backup folder does not exist in current directory.");
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }
        
        try {
            // Filewriter from java.io.FileWriter
            out = new PrintWriter(new FileWriter(directory));
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }

        // Print header for order file
        out.println("OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");
        // Force PrintWriter to write line to the file
        out.flush();
        
        Collection<Order> orderObjects = orders.values();
        // Write out the Order objects to the respective orders file
        String orderAsText;
        for (Order currentOrder : orderObjects) {
            // turn a Order into a String
            orderAsText = marshallOrder(currentOrder) + currentOrder.getDate();
            // write the Order object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }
    
    /**
     * Retrieves a list of filenames at the order directory.
     * These filenames are based on the order completion date of the orders within.
     * 
     * @return list of order file names
     * @throws DataPersistenceException 
     */
    private List<String> getOrderFiles() throws DataPersistenceException {
        List<String> orderFiles = null;
        try (Stream<Path> walk = Files.walk(Paths.get(ORDER_DIR))) {

                orderFiles = walk.filter(Files::isRegularFile)
                                .map(x -> x.toString()).collect(Collectors.toList());
                
        } catch (IOException e) {
            // Translate IOException
            throw new DataPersistenceException(
                    "Could not load order data into memory with ORDER_DIR: " + ORDER_DIR + "," + e);
        }
        
        return orderFiles;
    }
    
    /**
     * Load order information from files to memory.
     * 
     * @throws DataPersistenceException 
     */
    private void loadData() throws DataPersistenceException {
        LocalDate dateLocalDate;
        
        List<String> orderFiles = getOrderFiles();
        for (int i = 0; i < orderFiles.size(); i++) {
            // File name has format Orders_date.txt
            // First split file at '_' and get date.txt from index 1 of result
            // Then split date.txt at '.' and get date string from index 0
            String dateString = orderFiles.get(i).split("_")[1].split("\\.")[0];
            
            // Convert String to Date, Date to LocalDate
            try {
                Date dateDate = new SimpleDateFormat("MMddyyyy").parse(dateString);
                dateLocalDate = dateDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            } catch (ParseException ex) {
                throw new DataPersistenceException(
                        "Error in format of Order file.", ex);
            }
            loadOrderFile(orderFiles.get(i), dateLocalDate);
        }
        
    }
    
    /**
     * Write order data, with a separate file for each order date.
     * 
     * @throws DataPersistenceException 
     */
    private void writeData() throws DataPersistenceException {
        // Group orders by date
        ArrayList<Order> orderList = new ArrayList<>(orders.values());
        Stream<Order> orderStream = orderList.stream();
        Map<String, List<Order>> groupedMap = orderStream.collect(Collectors.groupingBy((o) -> o.getDate().format(DateTimeFormatter.ofPattern("MMddyyyy"))));
        // Get each date
        Set<String> dateSet = groupedMap.keySet();
        
        // For each date
        for (String date: dateSet) {
            // File name needs format Orders_date.txt
            String fileName = "Orders_" + date + ".txt";
            writeOrderFile(ORDER_DIR + fileName, groupedMap.get(date));
        }        
    }
    
    /**
     * Load orders in single order file to memory, converting strings
     * of order fields to Order objects.
     * Each order retrieved from this file will be given the passed
     * order date as its order date field - this date was retrieved from
     * the filename.
     * 
     * @param directory path to file orders are to be read from
     * @param orderDate date to be passed to order files in this directory
     * @throws DataPersistenceException 
     */
    private void loadOrderFile(String directory, LocalDate orderDate) throws DataPersistenceException {
        // Scanner from java.util.Scanner
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    // BuferredRead from java.io.BufferedReader
                    new BufferedReader(
                            // Filereader from java.io.FileReader
                            new FileReader(directory)));
        } catch (FileNotFoundException e) { 
            // Translate FileNotFoundException
            throw new DataPersistenceException(
                    "Could not load order data into memory with directory: " + directory + ".", e);
        }
        
        // currentLine holds the most recent line read from the file
        String currentLine;
        
        Order currentOrder;
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into an Order
            currentOrder = unmarshallOrder(currentLine, orderDate);
            // Put currentOrder into the map using order number as key
            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        // close scanner
        scanner.close();
    }
    
    /**
     * Writes a list of orders with a particular order date to their respective order file.
     * 
     * @param directory file the orders should be written to
     * @param ordersToWrite list of orders to write to file
     * @throws DataPersistenceException 
     */
    private void writeOrderFile(String directory, List<Order> ordersToWrite) throws DataPersistenceException {
        // Printwriter from java.io.PrintWriter;
        PrintWriter out;

        try {
            new FileOutputStream(directory, true).close();
        } catch (FileNotFoundException e) {
            throw new DataPersistenceException("ERROR: Orders folder does not exist in current directory.");
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }
        
        try {
            // Filewriter from java.io.FileWriter
            out = new PrintWriter(new FileWriter(directory));
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }

        // Print header for order file
        out.println("OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        // Force PrintWriter to write line to the file
        out.flush();
        
        // Write out the Order objects to the respective orders file
        String orderAsText;
        for (Order currentOrder : ordersToWrite) {
            // turn a Order into a String
            orderAsText = marshallOrder(currentOrder);
            // write the Order object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }
    
    /**
     * Convert a line from an order file to an Order object.
     * 
     * @param orderAsText line of text from orders file
     * @param orderDate LocalDate the orderDate field of the Order should be set to
     * @return Order object created with fields from file line
     * @throws DataPersistenceException 
     */
    private Order unmarshallOrder(String orderAsText, LocalDate orderDate) throws DataPersistenceException {
        // split returns string array split on DELIMETER
        String[] orderFields = orderAsText.split(DELIMITER);
        
        Order order;
        int orderNumber;
        String customerName;
        LocalDateTime creationDate;
        Statuses status;
        BigDecimal area;
        
        Product product;
        String productType;
        BigDecimal costPerSquareFoot;
        BigDecimal labourCostPerSquareFoot;
        
        State state;
        String stateAbbreviation;
        BigDecimal stateTaxRate;
        
        Costs costs;
        BigDecimal materialCost;
        BigDecimal labourCost;
        BigDecimal taxCost;
        BigDecimal totalCost;
        
        try {
            // Order number should be in index 0 of the array.
            orderNumber = Integer.parseInt(orderFields[0]);
            
            // Order customer name should be in index 1 of the array.
            customerName = orderFields[1];
            
            // Creation date of order should be in index 2.
            creationDate = LocalDateTime.parse(orderFields[2]);
            
            // Make new object with order number and creation date from file, and passed order date.
            order = new Order(orderNumber, creationDate, orderDate);
            
            order.setCustomerName(customerName);
            
            // Status of the order should be in index 3.
            status = Statuses.valueOf(orderFields[3].toUpperCase());
            
            order.setStatus(status);
            
            // State abbreviation should be in index 4.
            stateAbbreviation = orderFields[4];
            
            // State tax rate should be in index 5.
            stateTaxRate = new BigDecimal(orderFields[5]);
            
            // Make State object with read abbreviation and tax rate from Order file.
            // Make state name empty string for now as it is not found in Order file.
            // A State with a name will be added to the Order object upon data validation in
            // the service layer.
            state = new State(stateAbbreviation, "", stateTaxRate);
            
            // Product type should be in index 6.
            productType = orderFields[6];
            
            // Area of flooring order should be in index 7.
            area = new BigDecimal(orderFields[7]);
            
            // If a read Object has invalid flooring area, thow an error
            if (area.compareTo(new BigDecimal(100)) < 0) {
                throw new DataPersistenceException("Area of flooring for a read object is too small.");
            }
            
            // Set the area of the order.
            order.setArea(area);
            
            // Cost per square foot of a product should be in index 8.
            costPerSquareFoot = new BigDecimal(orderFields[8]);
            
            // Labour cost per square foot should be in index 9.
            labourCostPerSquareFoot = new BigDecimal(orderFields[9]);
            
            // Make a new product object with input fields.
            product = new Product(productType, costPerSquareFoot, labourCostPerSquareFoot);
            
            // Material cost should be in index 10.
            materialCost = new BigDecimal(orderFields[10]);
                
            // Labour cost should be in index 11.
            labourCost = new BigDecimal(orderFields[11]);
            
            // Tax cost should be in index 12.
            taxCost = new BigDecimal(orderFields[12]);
            
            // Total cost should be in index 13.
            totalCost = new BigDecimal(orderFields[13]);
            
            // Create the new cost object and set all fields.
            costs = new Costs();
            costs.setMaterialCost(materialCost);
            costs.setLabourCost(labourCost);
            costs.setTaxCost(taxCost);
            costs.setTotal(totalCost);

            // Set necessary order fields to the respective created objects.
            order.setProduct(product);
            order.setState(state);
            order.setCosts(costs);
            
        } catch (IndexOutOfBoundsException e) {
            // Error translation, helpful message
            throw new DataPersistenceException(
                    "Error in format of an Order: try checking field count in order file.", e);        
        } catch ( NumberFormatException e) {
            throw new DataPersistenceException(
                    "Error in format of an Order: a field has an incorrect format.", e);
        } catch ( DateTimeParseException e) {
            throw new DataPersistenceException(
                    "Error in format of an Order: a creation date field has an incorrect format.", e);
        }

        // Return Order created from file
        return order;
    }
    
    /**
     * Convert a line from the taxes file to a State object.
     * 
     * @param stateAsText line of text from taxes file
     * @return State object created with fields from file line
     * @throws DataPersistenceException 
     */
    private State unmarshallState(String stateAsText) throws DataPersistenceException {
        // split returns string array split on DELIMETER
        String[] stateFields = stateAsText.split(DELIMITER);
        
        State state;
        String stateAbbreviation;
        String stateName;
        BigDecimal stateTaxRate;
        
        try {
            
            // State abbreviation should be in index 0.
            stateAbbreviation = stateFields[0];
            
            // State name should be in index 1.
            stateName = stateFields[1];
            
            // State tax rate should be in index 2.
            stateTaxRate = new BigDecimal(stateFields[2]);
            
            // Make a new state object with input fields.
            state = new State(stateAbbreviation, stateName, stateTaxRate);
            
        } catch (IndexOutOfBoundsException e) {
            // Error translation, helpful message
            throw new DataPersistenceException(
                    "Error in format of a state: try checking field count in tax file.", e);        
        }
        catch ( NumberFormatException e) {
            throw new DataPersistenceException(
                    "Error in format of an state: a field has an incorrect format.", e);
        }

        // Return State created from file
        return state;
    }
    
    /**
     * Convert a line from the Products file to a Product object.
     * 
     * @param productAsText line of text from Products file
     * @return Product object created with fields from file line
     * @throws DataPersistenceException 
     */
    private Product unmarshallProduct(String productAsText) throws DataPersistenceException {
        // split returns string array split on DELIMETER
        String[] productFields = productAsText.split(DELIMITER);
        
        Product product;
        String productType;
        BigDecimal costPerSquareFoot;
        BigDecimal labourCostPerSquareFoot;
        
        try {
            
            // Product type should be in index 0.
            productType = productFields[0];
            
            // Cost per square foot of a product should be in index 1.
            costPerSquareFoot = new BigDecimal(productFields[1]);
            
            // Labour cost per square foot should be in index 2.
            labourCostPerSquareFoot = new BigDecimal(productFields[2]);
            
            // Make a new product object with input fields.
            product = new Product(productType, costPerSquareFoot, labourCostPerSquareFoot);
            
        } catch (IndexOutOfBoundsException e) {
            // Error translation, helpful message
            throw new DataPersistenceException(
                    "Error in format of a Product: try checking field count in prodct file.", e);        
        }
        catch ( NumberFormatException e) {
            throw new DataPersistenceException(
                    "Error in format of a Product: a field has an incorrect format.", e);
        }


        // Return Product created from file
        return product;
    }
    
    /**
     * Convert the fields of an Order object (and its constituent objects) to a String
     * to print to its respective order file.
     * @param order Order object to be converted to a string
     * @return String of order fields ordered and formatted according to storage standard
     */
    private String marshallOrder(Order order) {
        
        // Get Product, State, Costs objects associated with passed order
        Product product = order.getProduct();
        State state = order.getState();
        Costs costs = order.getCosts();
        
        // Make String of each field to be printed to Order file, in order
        String orderAsText = order.getOrderNumber() + DELIMITER
                + order.getCustomerName() + DELIMITER
                + order.getCreationDateTime() + DELIMITER
                + order.getStatus() + DELIMITER
                + state.getStateAbbreviation() + DELIMITER
                + state.getStateTaxRate() + DELIMITER
                + product.getProductType() + DELIMITER
                + order.getArea() + DELIMITER
                + product.getCostPerSquareFoot() + DELIMITER
                + product.getLabourCostPerSquareFoot() + DELIMITER
                + costs.getMaterialCost() + DELIMITER
                + costs.getLabourCost() + DELIMITER
                + costs.getTaxCost() + DELIMITER
                + costs.getTotal();
        
        return orderAsText;
    }
    
}
