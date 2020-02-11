public class RunCreateVehicleSer
{
    public static void main(String [] args)
    {
        CreateVehicleSer runCreateVehicles = new CreateVehicleSer();
        runCreateVehicles.openFile();
        runCreateVehicles.writeObjects();
        runCreateVehicles.closeFile();
    }
}
