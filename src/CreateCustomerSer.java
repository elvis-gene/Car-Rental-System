import java.util.*;
import java.io.*;

public class CreateCustomerSer
{
    private ObjectOutputStream outFile;
    Customer customerObj;
    
    public void openFile()
    {
        try
        {
            outFile = new ObjectOutputStream(new FileOutputStream("Customers.ser"));
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println("File not found...");
            System.exit(1);
        }
        catch(IOException ioe){
            System.out.println("Err opening output file");
        }
        
        
    }
    
    public void writeObjects()
    {
            try
            {
                customerObj = new Customer("Luke", "Atmyass", "4512563412950", "0834561231", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Chris.P", "Bacon", "5412563412939", "0845682315", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Justin", "Case", "6712563412901", "0847878787", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Ann", "Chovey", "6812563412903", "0824569872", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Hugh", "deMann", "7612563412997", "0762189898", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Ben", "Dover", "8122563412917", "0735218968", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Brighton", "Early", "6812563412911", "0845623258", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Al", "Kaholic", "8312563412944", "0835218989", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Mike", "Rohsopht", "7912563412966", "0741234568", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Ima", "Stewpidas", "7012563412934", "0752359852", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Ivana.B", "Withew", "6912563412921", "0827878989", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Stu", "Padassol", "6612563412933", "0764512350", true);
                outFile.writeObject(customerObj);
                customerObj = new Customer("Marsha", "Mellow", "7912563412941", "0735468974", true);
                outFile.writeObject(customerObj);
                
            }
            catch(InputMismatchException ime)
            {
                System.out.print("ERROR...");
            }
            catch(IOException ioe){
                System.out.println("Err writing to file");
            }             
    }        
    
    public void closeFile()
    {
        try{ 
            outFile.close();
        }
        catch(IOException ioe){
            System.out.println("Err closing file");
        }       
    }    
}
