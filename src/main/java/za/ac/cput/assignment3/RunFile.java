/**
 *
 * @author Demi Fazrquhar-220322104
 * Date: 8 June 2021
 */
package za.ac.cput.assignment3;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;


public class RunFile {
   
    private ObjectInputStream inputS;
    
  ArrayList<Customer> cust=  new ArrayList<Customer>();
  ArrayList<Supplier> suppl= new ArrayList<Supplier>();
  
  //open file 
  public void openFile(){
      try{
          inputS = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
          System.out.println("*** ser file opened for reading***");
      }
      catch (IOException ioe){
          System.out.println("Cannot open ser file: " +ioe.getMessage());
      }
  }
  public void closeFile(){
  try{
      inputS.close( );
  }
  catch (IOException ioe){
      System.out.println("Cannot open ser file " + ioe.getMessage());
     }    
  }
  // Add ArrayList
public void reazdFile(){
    try{
        Object obj=null;
        while(!(obj= inputS.readObject()).equals(null)){
            if(obj instanceof Customer ){
                cust.add((Customer) obj );
                System.out.println("Add Customer: "+ ((Customer) obj).getFirstName());
            }
            if (obj instanceof Supplier){
                suppl.add((Supplier) obj);
                System.out.println("Add Supplier: " + ((Supplier) obj) .getName());
            }
            
        }
        System.out.println("Complete read File");
    
    }catch (IOException ioe){
        System.out.println("File End");
    }catch (ClassNotFoundException cnfe){
        System.out.println("Class not found");
    }finally{
        closeFile();
    }
  }
public void sortCust(){
    Collections.sort(cust,(c1, c2) -> {
        return c1.getStHolderId().compareTo(c2.getStHolderId());
    });
}
public void sortSuppl(){
    Collections.sort(suppl, (c1 , c2) ->{
        return c1.getName().compareTo(c2.getName());
    });
}
private int getAgeFormat(String date ){
    LocalDate d1= LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    LocalDate d2= LocalDate.now();
    return Period.between(d1, d2).getYears();
}
public String formatBirthDate(Customer cust){
    DateFormat dateF =new SimpleDateFormat("dd MMM yyy");
    try{
        Date date =new SimpleDateFormat("yyyy-MM-dd").parse(cust.getDateOfBirth());
        return dateF.format(date);
    }catch (ParseException e){
        System.out.println("Error"+ e.getMessage());
        
    }
    return null;
}
// customerOutFile.txt
public void writeToCustomerFile(){
 try{
     FileWriter fileWriter= new FileWriter("customerOutFile.txt");
     fileWriter.write(" ================== CUSTOMERS ============================================\n");
     fileWriter.write(String.format("%-10s\t%-10s\t%-10s\t%-15s\t%-10s\n", "ID", "Name", "Surname", "Date of birth", "Age"));
     fileWriter.write("===========================================================================\n");
     for (Customer cust :cust){
          String output = String.format("%-10s\t%-10s\t%-10s\t%-15s\t%-10s", cust.getStHolderId(), cust.getFirstName(), cust.getSurName(), formatBirthDate(cust), getAgeFormat(cust.getDateOfBirth()));
                 fileWriter.write(output + "\n");
         }
     fileWriter.write("\nNumber of customers who can rent: " +cust.stream().filter(Customer::getCanRent).collect(Collectors.toList()).size() + "\n");
     fileWriter.write("\nNumber of customers who cannot rent: " + cust.stream().filter(c -> !c.getCanRent()).collect(Collectors.toList()).size());
     fileWriter.close();
 }   catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Error writing to file");  
 
}
}
 //SupplierOutFile.txt
 public void writeToSupplierFile(){
   try {
            FileWriter fileWriter = new FileWriter("supplierOutFile.txt");
            fileWriter.write("============================= SUPPLIERS  ===============================\n");
            fileWriter.write(String.format("%-15s\t%-15s\t%-15s\t%-15s\n", "ID", "Name", "Prod Type", "Description"));
            fileWriter.write("=========================================================================\n");
            for (Supplier suppl : suppl) {
                String output = String.format("%-15s\t%-20s\t%-15s\t%-15s", suppl.getStHolderId(), suppl.getName(), suppl.getProductType(), suppl.getProductDescription());
                fileWriter.write(output + "\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            System.out.println("Error writing to file");
        }
       
 }
     
 
 public static void main(String[] args) {
     RunFile run=new RunFile();
     run.openFile();
     run.reazdFile();
     run.sortCust();
     run.writeToCustomerFile();
     run.sortSuppl();
     run.writeToSupplierFile();
 }
}

