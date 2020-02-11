import java.io.*;

public class CreateVehicleSer
{
    private ObjectOutputStream outFile;
    Vehicle vehicleObj;
    
    public void openFile()
    {
        try
        {
            outFile = new ObjectOutputStream(new FileOutputStream("Vehicles.ser"));
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
                vehicleObj = new Vehicle("Audi A8", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("BMW 750iL", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Maserati GT", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Audi S6", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Audi Q7", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Range Rover Evoque", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("BMW X5", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Jaguar F-Pace", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Mercedes E500", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Volvo XC60", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("BMW 540i", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Volvo XC90", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Mercedes S500", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Mercedes GLE300", 2, 500, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("Lexus LS460", 1, 450, true);
                outFile.writeObject(vehicleObj);
                vehicleObj = new Vehicle("LExus RX350", 2, 500, true);
                outFile.writeObject(vehicleObj);
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
