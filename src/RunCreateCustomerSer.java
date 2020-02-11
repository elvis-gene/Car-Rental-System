
/**
 * Write a description of class runSales here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RunCreateCustomerSer
{
    public static void main(String [] args)
    {
        CreateCustomerSer runCustSer = new CreateCustomerSer();
        runCustSer.openFile();
        runCustSer.writeObjects();
        runCustSer.closeFile();
    }
}
