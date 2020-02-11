
import java.io.*;
public class Customer implements Serializable
{
    private int custNumber; //Add this
    private String firstName;
    private String surname;
    private String idNum; //Add this
    private String phoneNum; //Add this
    private boolean canRent;
//    private static final long serialVersionUID = -4222443595946933807L;

    public Customer(){}

    public Customer(String fName, String lName, String idNum, String phone, boolean can)
    {
        setName(fName);
        setSurname(lName);
        setPhoneNum(phone);
        setIdNum(idNum);
        setCanRent(can);
    }

    public Customer(int custNumber, String firstName, String surname, String idNum, String phoneNum) {
        this.custNumber = custNumber;
        this.firstName = firstName;
        this.surname = surname;
        this.idNum = idNum;
        this.phoneNum = phoneNum;
        this.canRent = canRent;
    }

    public Customer(String fName, String lName, String idNum, String phone)
    {
        setName(fName);
        setSurname(lName);
        setPhoneNum(phone);
        setIdNum(idNum);
        setCanRent(false);
    }

    public void setName(String sFName)
    {
        firstName = sFName;
    }

    public void setSurname(String sSName)
    {
        surname = sSName;
    }

    public void setPhoneNum(String sPhone)
    {
        phoneNum = sPhone;
    }
    public void setIdNum(String id)
    {
        idNum = id;
    }

    public void setCanRent(boolean c)
    {
        canRent = c;
    }

    public String getName()
    {
        return firstName;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }
    public String getIdNum()
    {
        return idNum;
    }

    public int getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(int custNumber) {
        this.custNumber = custNumber;
    }

    public boolean canRent()
    {
        return canRent;
    }

    public String toString()
    {
        return String.format("%-15s\t%-15s\t%-15s\t%-15s\t%-10s", getName(), getSurname(),
                getIdNum(),getPhoneNum(), Boolean.toString(canRent()));
    }
}
