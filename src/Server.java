import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Server {
    private Connection conn;
    private Statement stm;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outStream;
    private ObjectInputStream inputStreamSer;
    private PreparedStatement pStm;
    private int customerRows = 0;
    private int vehicleRows = 0;
    private Socket client;
    private ArrayList<Rental> rentalsList = new ArrayList<>();

    public void setDbConnection(){
        String dbUrl = "jdbc:ucanaccess://Database.accdb";
        try {
            conn = DriverManager.getConnection(dbUrl);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createTables(){
        String createTableCustomer = "CREATE TABLE CUSTOMER (" +
                "custNumber AUTOINCREMENT PRIMARY KEY, " +
                "firstName VARCHAR(30), " +
                "surname VARCHAR(30), " +
                "idNumber VARCHAR(30), " +
                "phoneNumber VARCHAR(20), " +
                "canRent BOOLEAN)";

        String createTableVehicle = "CREATE TABLE VEHICLE (" +
                "vehNumber AUTOINCREMENT PRIMARY KEY, " +
                "make VARCHAR(30), " +
                "category VARCHAR(30), " +
                "rentalPrice DOUBLE, " +
                "available BOOLEAN)";

        String createTableRental = "CREATE TABLE RENTAL (" +
                "rentalNumber int NOT NULL PRIMARY KEY, " +
                "dateOfRental DATE, " +
                "dateReturned DATE, " +
                "totalRental DOUBLE, " +
                "custNumber int, " +
                "vehNumber int)";

        try {
            stm = conn.createStatement();

            // Checking if the table already exists
            DatabaseMetaData dmCustomer = conn.getMetaData();
            ResultSet rsC = dmCustomer.getTables(null,null,"CUSTOMER",null);
            if (rsC.next())
                stm.executeUpdate("DROP TABLE CUSTOMER");

            DatabaseMetaData dmVehicle = conn.getMetaData();
            ResultSet rsV = dmVehicle.getTables(null,null,"VEHICLE",null);
            if (rsV.next())
                stm.executeUpdate("DROP TABLE VEHICLE");

            DatabaseMetaData dmRental = conn.getMetaData();
            ResultSet rsR = dmRental.getTables(null,null,"RENTAL",null);
            if (rsR.next())
                stm.executeUpdate("DROP TABLE RENTAL");

            stm.executeUpdate(createTableCustomer);
            stm.executeUpdate(createTableVehicle);
            stm.executeUpdate(createTableRental);

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void loadCustomersIntoDatabase(){
        try {
            String insertCs = "INSERT INTO CUSTOMER (firstName, surname, idNumber, phoneNumber, canRent) VALUES (?, ?, ?, ?, ?)";
            pStm = conn.prepareStatement(insertCs);

            inputStreamSer = new ObjectInputStream(new FileInputStream("Customers.ser"));
            while (true){
                Customer customer = (Customer)inputStreamSer.readObject();

                pStm.setString(1,customer.getName());
                pStm.setString(2,customer.getSurname());
                pStm.setString(3,customer.getIdNum());
                pStm.setString(4,customer.getPhoneNum());
                pStm.setBoolean(5,customer.canRent());
                pStm.executeUpdate();
                customerRows++;
            }
        }catch (EOFException ex){
            return;
        }
        catch (IOException | ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void loadVehiclesIntoDatabase(){
        try {
            String insertVs = "INSERT INTO VEHICLE (make, category, rentalPrice, available) VALUES (?, ?, ?, ?)";
            pStm = conn.prepareStatement(insertVs);

            inputStreamSer = new ObjectInputStream(new FileInputStream("Vehicles.ser"));
            while (true){
                Vehicle vehicle = (Vehicle) inputStreamSer.readObject();
                pStm.setString(1,vehicle.getMake());
                pStm.setString(2,vehicle.getCategory());
                pStm.setDouble(3,vehicle.getRentalPrice());
                pStm.setBoolean(4,vehicle.isAvailableForRent());
                pStm.executeUpdate();
                vehicleRows++;
            }
        }catch (EOFException ex){
            return;
        }
        catch (SQLException | IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void connectToClient(){
        try{
            System.out.println("Waiting for client to connect");
            client = new ServerSocket(19999).accept();

            outStream = new ObjectOutputStream(client.getOutputStream());
            inputStream = new ObjectInputStream(client.getInputStream());
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void receiveData() {
            //while (true)
            try {

                // if(conn!=null)
                while (true) {
                    String in = (String) inputStream.readObject();

                    switch (in.toLowerCase()) {
                        case "send customers": {
                            returnCustomers();
                            break;
                        }
                        case "send vehicles": {
                            returnVehicles();
                            break;
                        }
                        case "add customer": {
                            addCustomer();
                            break;
                        }
                        case "add vehicle": {
                            addVehicle();
                            break;
                        }
                        case "insert rental": {
                            addRental();
                            break;
                        }
                        case "select car category": {
                            selectCarCategory();
                            break;
                        }
                        case "return car":{
                        returnACar();
                        break;
                        }
                        case "send all returned rentals":{
                            returnAllRentals(); break;
                        }
                        case "send outstanding rentals":{
                            returnOutRentals();
                        }
                    }
                }
            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
     }

    ArrayList<Rental> allOutRentals = new ArrayList<>();
     public void returnOutRentals(){
         try
         {
             pStm = conn.prepareStatement("SELECT * FROM RENTAL WHERE dateReturned IS NULL");
             ResultSet rs = pStm.executeQuery();
             while (rs.next()){
                 Rental rental = new Rental();
                 rental.setRentalNumber(rs.getInt("rentalNumber"));
                 rental.setDateRental(rs.getString("dateOfRental"));
                 rental.setCustNumber(rs.getInt("custNumber"));
                 rental.setVehNumber(rs.getInt("vehNumber"));

                 allOutRentals.add(rental);
             }

             outStream.flush();
             outStream.writeObject(allOutRentals);
             outStream.flush();
         }catch (IOException | SQLException ex){
             ex.printStackTrace();
         }
     }


     ArrayList<Rental> allRentals = new ArrayList<>();
        public void returnAllRentals(){

            try{
                pStm = conn.prepareStatement("SELECT * FROM RENTAL WHERE dateReturned IS NOT NULL");
              ResultSet rs =   pStm.executeQuery();
                        while (rs.next()){
                            Rental rental = new Rental();
                            rental.setRentalNumber(rs.getInt("rentalNumber"));
                            rental.setCustNumber(rs.getInt("custNumber"));
                            rental.setVehNumber(rs.getInt("vehNumber"));
                            rental.setDateRental(rs.getString("dateOfRental"));
                            rental.setReturnDate(rs.getString("dateReturned"));
                            rental.setTotalPrice(rs.getDouble("totalRental"));

                            allRentals.add(rental);
                        }

                        outStream.flush();
                        outStream.writeObject(allRentals);
                        outStream.flush();
            }catch (SQLException | IOException ex){
                ex.printStackTrace();
            }
        }

        public void returnACar(){
            try{
                String rNum = (String) inputStream.readObject();
                int rentalNum = Integer.parseInt(rNum);
                String returnDate = (String)inputStream.readObject();

                String category = (String)inputStream.readObject();

                double pricePerD = 0.0;
                if(category.equalsIgnoreCase("sedan"))
                    pricePerD = 450;
                else pricePerD = 500;

                pStm = conn.prepareStatement("SELECT dateOfRental, custNumber, vehNumber FROM RENTAL WHERE rentalNumber = ?");
                pStm.setInt(1,rentalNum);

                Date date = new Date(2019,10,13);
                int custNum = 0;
                int vehNumber = 0;
                ResultSet rs = pStm.executeQuery();
                while(rs.next()){
                    date = rs.getDate("dateOfRental");
                    custNum = rs.getInt("custNumber");
                    vehNumber = rs.getInt("vehNumber");
                }
                LocalDate rented = date.toLocalDate();

                System.out.println("Date rented "+ rented);

                StringTokenizer st = new StringTokenizer(returnDate, "/");
                int month = Integer.parseInt(st.nextToken());
                int day = Integer.parseInt(st.nextToken());
                int year = Integer.parseInt(st.nextToken());
                LocalDate localD = LocalDate.of(year, month, day);

                System.out.println("Date to be returned "+ localD);

                long days = ChronoUnit.DAYS.between(rented,localD);

                System.out.println(days);
                double total = (days + 1) * pricePerD;

                outStream.writeObject(total);
                outStream.flush();

                pStm = conn.prepareStatement("UPDATE CUSTOMER SET canRent = true WHERE custNumber  = ?");
                        pStm.setInt(1,custNum);
                pStm.executeUpdate();

                pStm = conn.prepareStatement("UPDATE RENTAL SET dateReturned = ? WHERE rentalNumber = ?");
                pStm.setDate(1,Date.valueOf(rented));
                pStm.setInt(2,rentalNum);
                pStm.executeUpdate();
                pStm = conn.prepareStatement("UPDATE RENTAL SET totalRental = "+ total +" WHERE rentalNumber = ?");
                pStm.setInt(1,rentalNum);
                pStm.executeUpdate();

                pStm = conn.prepareStatement("UPDATE VEHICLE SET available = true WHERE vehNumber = ?");
                pStm.setInt(1,vehNumber);
                pStm.executeUpdate();

                outStream.writeObject("Vehicle successfully returned. Thanks");
                outStream.flush();

            }catch (SQLException | ClassNotFoundException | IOException ex){
                ex.printStackTrace();
//                System.out.println(ex.getMessage());
            }
        }

        private ArrayList <Vehicle> suvs = new ArrayList<>();
        private ArrayList <Vehicle> sedans = new ArrayList<>();

        public void selectCarCategory() {
        try {
            String data = (String) inputStream.readObject();

            if (data.equalsIgnoreCase("SUV")) {
                pStm = conn.prepareStatement("SELECT * FROM VEHICLE WHERE category = ?");
                pStm.setString(1, "SUV");
                ResultSet rs = pStm.executeQuery();

                Vehicle vehicle = new Vehicle();
                while (rs.next()) {
                    vehicle.setMake(rs.getString("make"));
                    vehicle.setCategory(2);
                    vehicle.setAvailableForRent(true);
                    vehicle.setRentalPrice(rs.getDouble("rentalPrice"));
                    suvs.add(vehicle);
                }
                outStream.writeObject(suvs);
                outStream.flush();

            } else if (data.equalsIgnoreCase("Sedan")) {
                pStm = conn.prepareStatement("SELECT * FROM VEHICLE WHERE category = ?");
                pStm.setString(1, "Sedan");
                ResultSet rs = pStm.executeQuery();

                Vehicle vehicle = new Vehicle();
                while (rs.next()) {
                    vehicle.setMake(rs.getString("make"));
                    vehicle.setCategory(1);
                    vehicle.setAvailableForRent(true);
                    vehicle.setRentalPrice(rs.getDouble("rentalPrice"));
                    sedans.add(vehicle);
                }
                outStream.writeObject(sedans);
                outStream.flush();
            }
        }catch(IOException | SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        }

    public void addRental() {

        try {
            Rental rental = (Rental) inputStream.readObject();
            rentalsList.add(rental);
            pStm = conn.prepareStatement("INSERT INTO RENTAL (rentalNumber, dateOfRental, custNumber, vehNumber) VALUES (?, ?, ?, ?)");

            //THE RENTAL NUMBER IS THE CONCATENATION OF THE CUSTNUM, VEHNUMBER RENTALDATE
            int rentalNumber;

            StringTokenizer st = new StringTokenizer(rental.getDateRental(), "/");

            int month = Integer.parseInt(st.nextToken());
            int day = Integer.parseInt(st.nextToken());
            int year = Integer.parseInt(st.nextToken());
            LocalDate localD = LocalDate.of(year, month, day);
            Date date = Date.valueOf(localD); //Convert date to sql date before inserting

            String rentNumS = year + "" + month + "" + day + "" + rental.getCustNumber() + "" + rental.getVehNumber();
            rentalNumber = Integer.parseInt(rentNumS);

            PreparedStatement ps = conn.prepareStatement("SELECT canRent FROM CUSTOMER WHERE custNumber ="+ rental.getCustNumber());
            ResultSet rs = ps.executeQuery();

            boolean canRent = false;
            while (rs.next())
                canRent = rs.getBoolean("canRent");

            if (canRent) {
                pStm.setInt(1, rentalNumber);
                pStm.setDate(2, date);
                pStm.setInt(3, rental.getCustNumber());
                pStm.setInt(4, rental.getVehNumber());
                pStm.executeUpdate();

                pStm = conn.prepareStatement("UPDATE CUSTOMER SET canRent = ? where custNumber = ?");
                pStm.setBoolean(1, false);
                pStm.setInt(2, rental.getCustNumber());
                pStm.executeUpdate();

                pStm = conn.prepareStatement("UPDATE VEHICLE set available = ? where vehNumber = ?");
                pStm.setBoolean(1, false);
                pStm.setInt(2, rental.getVehNumber());
                pStm.executeUpdate();

                //c.	Display appropriate message after the server has updated the database’
                outStream.writeObject("You have rented a car on " + rental.getDateRental() + ". Rental number: " + rentNumS);
            }
                else {
                    outStream.writeObject("This customer has not returned the previous car they rented");
            }
            }catch(IOException | SQLException | ClassNotFoundException x){
                x.printStackTrace();
            }
    }
    public void addCustomer(){
        try {
            Customer cus = (Customer) inputStream.readObject();

            pStm = conn.prepareStatement("INSERT INTO CUSTOMER (firstName, surname, idNumber, phoneNumber, canRent) VALUES (?, ?, ?, ?, ?)");
            pStm.setString(1, cus.getName());
            pStm.setString(2, cus.getSurname());
            pStm.setString(3, cus.getIdNum());
            pStm.setString(4, cus.getPhoneNum());
            pStm.setBoolean(5, cus.canRent());
            pStm.executeUpdate();
            customerRows++;
        }catch (IOException | SQLException | ClassNotFoundException x){
            x.printStackTrace();
        }
    }

    public void addVehicle() {
        try {
            Vehicle veh = (Vehicle) inputStream.readObject();

            pStm = conn.prepareStatement("INSERT INTO VEHICLE (make, category, rentalPrice, available) VALUES (?, ?, ?, ?)");

            pStm.setString(1, veh.getMake());
            pStm.setString(2, veh.getCategory());
            pStm.setDouble(3, veh.getRentalPrice());
            pStm.setBoolean(4, veh.isAvailableForRent());
            pStm.executeUpdate();
            vehicleRows++;
        }catch (IOException | SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }


    private ArrayList <Customer> listOfCustomers = new ArrayList<>();
    public void returnCustomers(){
        try{
            // Only shows cars that are available
            ResultSet selCusRS = stm.executeQuery("SELECT * FROM CUSTOMER");// WHERE canRent = true

            while (selCusRS.next()){
                int custNum= selCusRS.getInt("custNumber");
                String fName = selCusRS.getString("firstName");
                String surname = selCusRS.getString("surname");
                String idNum = selCusRS.getString("idNumber");
                String phoneNumber = selCusRS.getString("phoneNumber");

                Customer customer = new Customer(custNum,fName,surname,idNum,phoneNumber);
                listOfCustomers.add(customer);
            }

            outStream.writeObject(listOfCustomers);
            outStream.flush();
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList <Vehicle> listOfVehicles = new ArrayList<>();
    public void returnVehicles(){
        try {
            // Only shows cars that are available
            ResultSet selVehsRS = stm.executeQuery("SELECT * FROM VEHICLE");// WHERE available = true

            while (selVehsRS.next()){
                int vehNum = selVehsRS.getInt("vehNumber");
                String make = selVehsRS.getString("make");
                String category = selVehsRS.getString("category");
                Double rentalPrice = selVehsRS.getDouble("rentalPrice");

                int categ = 0;
                if (category.equalsIgnoreCase("Sedan"))
                    categ = 1;
                else categ = 2;

                Vehicle vehicle = new Vehicle(make,categ,rentalPrice,true);
                listOfVehicles.add(vehicle);
            }

            outStream.writeObject(listOfVehicles);
            outStream.flush();
        }catch (IOException | SQLException ex){
            ex.printStackTrace();
        }
    }

    public void closeResources(){
        try{
            pStm.close();
            stm.close();
            conn.close();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String [] args){
        Server server = new Server();
        server.setDbConnection();
        server.connectToClient();
        server.createTables();
        server.loadCustomersIntoDatabase();
        server.loadVehiclesIntoDatabase();

        server.receiveData();

        server.closeResources();
    }
}

/*TODO:
    - One should not be allowed to add more than one Customer or Vehicle that are alike
    - Fix addVehicle and AddCustomer. They don't work after adding one entry
 */