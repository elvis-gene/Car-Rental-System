import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class ClientSpace extends JFrame {

    private Socket connectToServer;
    private ObjectInputStream inStream;
    private ObjectOutputStream outputStream;
    private ArrayList <Vehicle> listOfVehicles = new ArrayList<>();
    private ArrayList <Customer> listOfCustomers = new ArrayList<>();
    private ArrayList <Rental> listOfRentals = new ArrayList<>();

    private ArrayList <Customer> customersList;
    private ArrayList<Vehicle> vehiclesList;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Options");
    private JMenuItem showAllRentals = new JMenuItem("Display All Rentals");
    private JMenuItem showOutRentals = new JMenuItem("Display Outstanding Rentals");
    private JMenuItem exitOp = new JMenuItem("Exit");

    // Tables
    private JTable customersListTable = new JTable();
    private JTable vehiclesListTable = new JTable();
    private JTable allRentalsTable = new JTable();
    private JTable outstandingRentalsTable = new JTable();

    private DefaultTableModel customersListModel;
    private DefaultTableModel vehiclesListModel;
    private DefaultTableModel rentalsListModel;
    private DefaultTableModel outRentalsListModel;

    //Header with name + picture
    private JLabel rentalName = new JLabel();

    //createCustomer
    private JLabel fNameLabel = new JLabel("First Name:         ");
    private JLabel lNameLabel = new JLabel("        Last Name:          ");
    private JTextField fNameTextField = new JTextField("first name", 15);
    private JTextField lNameTextField = new JTextField("last name", 15);

    private JLabel idNumLabel = new JLabel("ID Number:         ");
    private JLabel phoneNumLabel = new JLabel("        Phone Number:          ");
    private JTextField idTextField = new JTextField("ID number", 15);
    private JTextField phoneNumTextField = new JTextField("phone number", 15);

    private JButton createCustomerCancelBtn = new JButton("Cancel");
    private JButton createCustomerSubmitBtn = new JButton("Submit");
    private JButton sortCustNames = new JButton("Sort & Display");

    //createCustomer Panels
    private JPanel createCustomerPanel  = new JPanel();
    private JPanel createCustomerPanelTop = new JPanel();
    private JPanel createCustomerPanelTopT = new JPanel();
    private JPanel createCustomerPanelBottom = new JPanel();
    private JPanel createCustomerPanelTopB = new JPanel();

    //createVehicle Panels
    private JPanel createVehicleLeft = new JPanel();
    private JPanel createVehicleRight = new JPanel();
    private JScrollPane createVehicleBottomOne;
    private JPanel createVehicleBottom = new JPanel();
    private JPanel createVehicleTop = new JPanel();
    private JPanel createVehicleMain = new JPanel();

    //vehicle- dashboard : rent a vehicle
    private JPanel selectCustomer = new JPanel();
    private JPanel selectCategory = new JPanel();
    private JPanel selectCar = new JPanel();
    private JPanel rentCar = new JPanel();
    private JPanel rentDate = new JPanel();
    // JPanel panelForCustomer = new JPanel();
    JScrollPane panelForCustomer;

    private JLabel selectCustomerLabel = new JLabel("Select Customer        ");
    private JLabel selectCategoryLabel = new JLabel("Select Category        ");
    private JLabel selectCarLabel = new JLabel("Select Car                 ");
    private JLabel rentDateLabel = new JLabel("Rent date");

    private JFormattedTextField rentDateTf = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
    private JComboBox <String> selectCustomerCombo = new JComboBox<>();
    private JComboBox <String> selectCategoryCombo = new JComboBox<>();
    private JComboBox <String> selectCarCombo = new JComboBox<>();
    private JButton rentCarButton = new JButton("Rent");

    //vehicle- dashboard : return a vehicle
    JPanel returnCar = new JPanel();
    JPanel returnDetails = new JPanel();
    JPanel returnCarButtonPanel = new JPanel();
    JPanel returnDatePanel = new JPanel();
    JPanel returnCarMain = new JPanel();
    private JComboBox <String> returnCarCombo = new JComboBox<>();
    private JLabel returnCarLabel = new JLabel("Return Number");
    private JButton returnCarButton = new JButton("Return");
    private JTextArea returnCarTotal = new JTextArea(4,25);

    private JLabel returnDateLabel = new JLabel("Return date");
    private JFormattedTextField returnDateTf = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));

    // vehicle- dashboard : add vehicle
    JPanel addVehicleMain = new JPanel();
    JPanel vehicleMake = new JPanel();
    JPanel vehicleCategory = new JPanel();
    JPanel addVehicle = new JPanel();
    private JLabel vehicleMakeLabel = new JLabel("Vehicle Make");
    private JLabel vehicleCategoryLabel = new JLabel("Vehicle Category");
    private JTextField vehicleMakeTextField = new JTextField(13);

    private JComboBox <String> vehicleCategoryCombo = new JComboBox<>();
    private JButton addVehicleButton = new JButton("Add Vehicle");

    //createReports Panels
    JScrollPane createReportsTop;
    JScrollPane createReportsBottom;
    JPanel createReportsMain = new JPanel();

    //Tabs pane
    JPanel tabsPanel = new JPanel();
    JTabbedPane tabs = new JTabbedPane();


    public ClientSpace(){
        super("Rentals");


        customersListTable.setAutoCreateRowSorter(true);
        outstandingRentalsTable.setAutoCreateRowSorter(true);
        vehiclesListTable.setAutoCreateRowSorter(true);
        outstandingRentalsTable.setAutoCreateRowSorter(true);

        createMenu();
        createAndAddTabs();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        setVisible(true);

        connectToServer();
        comboBoxes();
        createTables();
        addVehicle();
        addCustomer();
        rentFunctions();
        returnCar();
        receiveData();
        implementMenu();

        exitOp.addActionListener(e -> {
            this.dispose();
        });
    }

    public void connectToServer(){
        try{
            connectToServer = new Socket("localhost",19999);
            System.out.println("Connection Established");

            outputStream = new ObjectOutputStream(connectToServer.getOutputStream());
            inStream = new ObjectInputStream(connectToServer.getInputStream());
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void createTables() {

            customersListModel = new DefaultTableModel() {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 0)
                        return Integer.class;
                    else if (columnIndex == 1)
                        return String.class;
                    else if (columnIndex == 2)
                        return String.class;
                    else if (columnIndex == 3)
                        return String.class;
                    else if (columnIndex == 4)
                        return String.class;
                    else
                        return String.class;
                }

                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };


        customersListModel.addColumn("N#");
        customersListModel.addColumn("Name");
        customersListModel.addColumn("Surname");
        customersListModel.addColumn("ID Number");
        customersListModel.addColumn("Phone Number");

        customersListTable.setModel(customersListModel);
        customersListTable.setShowGrid(false);

        // Center strings in the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        customersListTable.setDefaultRenderer(String.class, centerRenderer);
        customersListTable.setDefaultRenderer(Integer.class, centerRenderer);

        vehiclesListModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class;
                else if (columnIndex == 1)
                    return String.class;
                else if (columnIndex == 2)
                    return String.class;
                else
                    return Double.class;
            }

            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        vehiclesListModel.addColumn("N#");
        vehiclesListModel.addColumn("Make");
        vehiclesListModel.addColumn("Category");
        vehiclesListModel.addColumn("Rental Price");

        vehiclesListTable.setModel(vehiclesListModel);
        vehiclesListTable.setShowGrid(false);
        // Center strings in the cells

        vehiclesListTable.setDefaultRenderer(String.class, centerRenderer);
        vehiclesListTable.setDefaultRenderer(Double.class, centerRenderer);
        vehiclesListTable.setDefaultRenderer(Integer.class, centerRenderer);

        rentalsListModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class;
                else if (columnIndex == 1)
                    return String.class;
                else if (columnIndex == 2)
                    return String.class;
                else if (columnIndex == 3)
                    return Double.class;
                else if (columnIndex == 4)
                    return String.class; // Customer name
                else
                    return String.class; // Vehicle Make + name
            }
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        rentalsListModel.addColumn("N#");
        rentalsListModel.addColumn("Date Rented");
        rentalsListModel.addColumn("Date Returned");
        rentalsListModel.addColumn("Total Price");
        rentalsListModel.addColumn("Customer");
        rentalsListModel.addColumn("Vehicle");

        allRentalsTable.setModel(rentalsListModel);
        allRentalsTable.setShowGrid(false);
        // Center strings in the cells
        allRentalsTable.setDefaultRenderer(String.class, centerRenderer);
        allRentalsTable.setDefaultRenderer(Integer.class, centerRenderer);
        allRentalsTable.setDefaultRenderer(Double.class, centerRenderer);

        outRentalsListModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class;
                else if (columnIndex == 1)
                    return String.class;
                else if (columnIndex == 2)
                    return String.class; // Customer name
                else
                    return String.class; // Vehicle Make + name
            }
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        outRentalsListModel.addColumn("N#");
        outRentalsListModel.addColumn("Date Rented");
        outRentalsListModel.addColumn("Customer");
        outRentalsListModel.addColumn("Vehicle");

        outstandingRentalsTable.setModel(outRentalsListModel);
        outstandingRentalsTable.setShowGrid(false);
        // Center strings in the cells
        outstandingRentalsTable.setDefaultRenderer(String.class, centerRenderer);
        outstandingRentalsTable.setDefaultRenderer(Integer.class, centerRenderer);
    }


    public void createMenu(){
        //Add to frame
        setJMenuBar(menuBar);

        //Add to menu
        menu.add(showAllRentals);
        menu.add(showOutRentals);
        menu.add(exitOp);

        menuBar.add(menu);
    }

    public JPanel createCustomerTab(){
        TitledBorder createCustBorder = new TitledBorder("Create customer");
        createCustBorder.setTitleJustification(TitledBorder.LEFT);
        createCustBorder.setTitlePosition(TitledBorder.TOP);
        createCustBorder.setBorder(BorderFactory.createCompoundBorder(createCustBorder.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        TitledBorder custList = new TitledBorder("List of customers");
        custList.setTitlePosition(TitledBorder.TOP);
        custList.setTitleJustification(TitledBorder.LEFT);
        custList.setBorder(BorderFactory.createCompoundBorder(custList.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        //set padding for textfields
        fNameTextField.setBorder(BorderFactory.createCompoundBorder(lNameTextField.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));
        lNameTextField.setBorder(BorderFactory.createCompoundBorder(lNameTextField.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

//        createCustomerPanelTopT.setLayout(new FlowLayout());
        createCustomerPanelTopT.setLayout(new GridLayout(2,4));
        createCustomerPanelTop.setBorder(createCustBorder);

        createCustomerPanelTopT.add(fNameLabel);
        createCustomerPanelTopT.add(fNameTextField);
        createCustomerPanelTopT.add(lNameLabel);
        createCustomerPanelTopT.add(lNameTextField);
        createCustomerPanelTopT.add(idNumLabel);
        createCustomerPanelTopT.add(idTextField);
        createCustomerPanelTopT.add(phoneNumLabel);
        createCustomerPanelTopT.add(phoneNumTextField);

        createCustomerPanelTopB.setLayout(new FlowLayout());
        createCustomerPanelTopB.add(createCustomerCancelBtn);
        createCustomerPanelTopB.add(createCustomerSubmitBtn);

        createCustomerPanelTop.setLayout(new GridLayout(2,1));
        createCustomerPanelTop.add(createCustomerPanelTopT);
        createCustomerPanelTop.add(createCustomerPanelTopB);

        //panel for list of customers
        createCustomerPanelBottom.setLayout(new BorderLayout());

        createCustomerPanelBottom.setBorder(custList);

        panelForCustomer = new JScrollPane(customersListTable);

        /**Just added so long in the panel so it doesnt look empty, but the table with the list of customers must be in there*/
        sortCustNames.setSize(5,5);
        createCustomerPanelBottom.add(sortCustNames, BorderLayout.SOUTH);

        createCustomerPanel.setLayout(new BorderLayout());
        createCustomerPanel.add(createCustomerPanelTop, BorderLayout.NORTH);

        createCustomerPanel.add(panelForCustomer, BorderLayout.CENTER);

        return createCustomerPanel;
    }

    public JPanel createVehiclePanel(){
        TitledBorder createVehicleLeftBorder = new TitledBorder("Rent a vehicle");
        createVehicleLeftBorder.setTitleJustification(TitledBorder.LEFT);
        createVehicleLeftBorder.setTitlePosition(TitledBorder.TOP);
        createVehicleLeftBorder.setBorder(BorderFactory.createCompoundBorder(createVehicleLeftBorder.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        TitledBorder createVehicleRightBorder = new TitledBorder("Return a vehicle");
        createVehicleRightBorder.setTitleJustification(TitledBorder.LEFT);
        createVehicleRightBorder.setTitlePosition(TitledBorder.TOP);
        createVehicleRightBorder.setBorder(BorderFactory.createCompoundBorder(createVehicleLeftBorder.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        TitledBorder addVehicleMainBorder = new TitledBorder("Add a vehicle");
        addVehicleMainBorder.setTitleJustification(TitledBorder.LEFT);
        addVehicleMainBorder.setTitlePosition(TitledBorder.TOP);
        addVehicleMainBorder.setBorder(BorderFactory.createCompoundBorder(addVehicleMainBorder.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        TitledBorder createVehicleBottomBorder = new TitledBorder("List of vehicles");
        createVehicleBottomBorder.setTitleJustification(TitledBorder.LEFT);
        createVehicleBottomBorder.setTitlePosition(TitledBorder.TOP);
        createVehicleBottomBorder.setBorder(BorderFactory.createCompoundBorder(createVehicleBottomBorder.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        createVehicleLeft.setPreferredSize(new Dimension(330, 250));
        createVehicleRight.setPreferredSize(new Dimension(330,250));
        createVehicleMain.setPreferredSize(new Dimension(330, 250));

        createVehicleLeft.setBorder(createVehicleLeftBorder);
        createVehicleRight.setBorder(createVehicleRightBorder);
        addVehicleMain.setBorder(addVehicleMainBorder);

        //left panel
        createVehicleLeft.setLayout(new GridLayout(5, 1));

        selectCustomer.setLayout(new FlowLayout());
        selectCustomer.add(selectCustomerLabel);
        selectCustomer.add(selectCustomerCombo);

        selectCategory.setLayout(new FlowLayout());
        selectCategory.add(selectCategoryLabel);
        selectCategory.add(selectCategoryCombo);

        selectCar.setLayout(new FlowLayout());
        selectCar.add(selectCarLabel);
        selectCar.add(selectCarCombo);
        selectCategory.setLayout(new FlowLayout());

        /**Added to see if the calender popup work**/

        rentDate.setLayout(new FlowLayout());

        rentDateTf.setValue(new Date());
        rentDateTf.setPreferredSize(new Dimension(130, 30));
        returnDateTf.setValue(new Date());

        rentDate.add(rentDateLabel);
        rentDate.add(rentDateTf);

        rentCar.setLayout(new FlowLayout());
        rentCar.add(rentCarButton);

        createVehicleLeft.add(selectCustomer);
        createVehicleLeft.add(selectCategory);
        createVehicleLeft.add(selectCar);
        createVehicleLeft.add(rentDate);
        createVehicleLeft.add(rentCar);

        /**create middle panel for add car*/
        addVehicleMain.setLayout(new GridLayout(3,1));

        vehicleMake.setLayout(new FlowLayout());
        vehicleMake.add(vehicleMakeLabel);
        vehicleMake.add(vehicleMakeTextField);
        vehicleCategory.setLayout(new FlowLayout());
        vehicleCategory.add(vehicleCategoryLabel);
        vehicleCategory.add(vehicleCategoryCombo);

        addVehicle.setLayout(new FlowLayout());
        addVehicle.add(addVehicleButton);

        addVehicleMain.add(vehicleMake);
        addVehicleMain.add(vehicleCategory);
        addVehicleMain.add(addVehicle);

        /**right panel for vehicle return*/
        createVehicleRight.setLayout(new FlowLayout());

        returnCar.setLayout(new FlowLayout());
        returnCar.add(returnCarLabel);
        returnCar.add(returnCarCombo);
        //returnCar.add(selectReturnDate);

        returnCarButtonPanel.setLayout(new FlowLayout());
        returnCarButtonPanel.add(returnCarButton);

        returnDetails.setLayout(new FlowLayout());
        returnDetails.add(returnCarTotal);

        returnCarMain.setLayout(new BorderLayout());
        returnCarMain.add(returnCar, BorderLayout.NORTH);
        returnCarMain.add(returnDetails);

        // returnDatePanel.setLayout(new FlowLayout());
        returnDatePanel.setLayout(new GridLayout(1,2));
        returnDatePanel.add(returnDateLabel);
        returnDatePanel.add(returnDateTf);

        createVehicleRight.add(returnCarMain);
        createVehicleRight.add(returnDatePanel);
        createVehicleRight.add(returnCarButtonPanel);


        //add three panels next to each other
        createVehicleTop.setLayout(new FlowLayout());
        createVehicleTop.add(createVehicleLeft);
        createVehicleTop.add(addVehicleMain);
        createVehicleTop.add(createVehicleRight);

        //add BottomOne onto Bottom panel so it can be below the other 2
        createVehicleBottomOne = new JScrollPane(vehiclesListTable);
        createVehicleBottom.setLayout(new BorderLayout());
        createVehicleBottom.add(createVehicleBottomOne);

        createVehicleMain.setLayout(new GridLayout(2,1));
        createVehicleMain.add(createVehicleTop);
        createVehicleMain.add(createVehicleBottom);

        return createVehicleMain;
    }


    public JPanel createReportsPanel(){
        TitledBorder createReportTopBorder = new TitledBorder("All rentals");
        createReportTopBorder.setTitleJustification(TitledBorder.LEFT);
        createReportTopBorder.setTitlePosition(TitledBorder.TOP);
        createReportTopBorder.setBorder(BorderFactory.createCompoundBorder(createReportTopBorder.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        TitledBorder createReportsBottomBorder = new TitledBorder("Outstanding rentals");
        createReportsBottomBorder.setTitleJustification(TitledBorder.LEFT);
        createReportsBottomBorder.setTitlePosition(TitledBorder.TOP);
        createReportsBottomBorder.setBorder(BorderFactory.createCompoundBorder(createReportsBottomBorder.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        createReportsTop = new JScrollPane(allRentalsTable);

        createReportsTop.setBorder(createReportTopBorder);

        createReportsBottom = new JScrollPane(outstandingRentalsTable);
        // createReportsBottom.setLayout(new BorderLayout());

        createReportsBottom.setBorder(createReportsBottomBorder);

        // createReportsTop.setPreferredSize(new Dimension(650,200));
        createReportsBottom.setPreferredSize(new Dimension(650, 200));

        createReportsMain.setLayout(new GridLayout(2,1));
        createReportsMain.add(createReportsTop);
        createReportsMain.add(createReportsBottom);

        // createReportsTop.setLayout(new BorderLayout());
        // createReportsTop = new JScrollPane(allRentalsTable);

        return createReportsMain;
    }

    public void createAndAddTabs(){
        tabsPanel.setLayout(new BorderLayout());
        /**try to add padding around tabbed pane
         * but i don't see a difference*/
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);

        //Tab 1
        JScrollPane createCustomerTab = new JScrollPane(createCustomerTab());
        tabs.addTab("   Customer Dashboard  ", createCustomerTab);

        //Tab 2
        JScrollPane createVehicleTab = new JScrollPane(createVehiclePanel());
        tabs.addTab("Vehicle Dashboard", createVehicleTab);

        //Tab 3
        JScrollPane createReportsTab = new JScrollPane(createReportsPanel());
        tabs.addTab("       Reports         ", createReportsTab);

        tabsPanel.add(tabs);
        add(tabsPanel, BorderLayout.CENTER);

    }

    public void comboBoxes(){
        selectCustomerCombo.setPrototypeDisplayValue("Terms Terms Cookie PPP");
        selectCarCombo.setPrototypeDisplayValue("Terms Terms Cookie PPP");
        vehicleCategoryCombo.setPrototypeDisplayValue("Terms Terms");
        returnCarCombo.setPrototypeDisplayValue("Terms Terms");
        selectCategoryCombo.setPrototypeDisplayValue("Terms Terms Cookie PPP");

        selectCategoryCombo.addItem("Select Category");
        selectCategoryCombo.addItem("SUV");
        selectCategoryCombo.addItem("Sedan");

        vehicleCategoryCombo.addItem("Select Category");
        vehicleCategoryCombo.addItem("SUV");
        vehicleCategoryCombo.addItem("Sedan");
    }

    public void receiveData(){
        try {
//              outputStream.flush();
                outputStream.writeObject("Send Customers");
                customersList = (ArrayList<Customer>) inStream.readObject();

                for (int i = 0; i < customersList.size(); i++) {
                    selectCustomerCombo.addItem(customersList.get(i).getName() + " " + customersList.get(i).getSurname());
                    listOfCustomers.add(customersList.get(i));

                    customersListModel.addRow(new Object[i]);
                    customersListModel.setValueAt(i+1,i,0);
                    customersListModel.setValueAt(customersList.get(i).getName(),i,1);
                    customersListModel.setValueAt(customersList.get(i).getSurname(),i,2);
                    customersListModel.setValueAt(customersList.get(i).getIdNum(),i,3);
                    customersListModel.setValueAt(customersList.get(i).getPhoneNum(),i,4);
                }

                outputStream.flush();
                outputStream.writeObject("Send vehicles");
                vehiclesList = (ArrayList<Vehicle>)inStream.readObject();

                for (int i = 0; i < vehiclesList.size(); i++) {
                    Vehicle vehicle = vehiclesList.get(i);
                    listOfVehicles.add(vehicle);

                    vehiclesListModel.addRow(new Object[i]);
                    vehiclesListModel.setValueAt(i+1,i,0);
                    vehiclesListModel.setValueAt(vehiclesList.get(i).getMake(),i,1);
                    vehiclesListModel.setValueAt(vehiclesList.get(i).getCategory(),i,2);
                    vehiclesListModel.setValueAt(vehiclesList.get(i).getRentalPrice(),i,3);
                }
                outputStream.flush();

        }catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public void addCustomer(){

        phoneNumTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // ignore event
                }
            }
        });

        rentDateTf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // ignore event
                }
            }
        });

        returnDateTf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // ignore event
                }
            }
        });

        fNameTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(!(Character.isAlphabetic(c) ||  (c==KeyEvent.VK_BACK_SPACE)||  c==KeyEvent.VK_DELETE ))
                    e.consume();
                 }
            });

        lNameTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(!(Character.isAlphabetic(c) ||  (c==KeyEvent.VK_BACK_SPACE)||  c==KeyEvent.VK_DELETE ))
                    e.consume();
            }
        });


        createCustomerSubmitBtn.addActionListener(e -> {
            if (!(fNameTextField.getText()).isEmpty() && !(lNameTextField.getText().isEmpty()) && !(idTextField.getText().isEmpty()
                    && !(phoneNumTextField.getText()).isEmpty())) {

                if (!fNameTextField.getText().equalsIgnoreCase("First Name") && !lNameTextField.getText().equalsIgnoreCase(
                        "Last Name") && !idTextField.getText().equalsIgnoreCase("ID Number") &&
                !phoneNumTextField.getText().equalsIgnoreCase("Phone Number")) {

                    boolean existingCustomer = false;

                    for (int i = 0; i < listOfCustomers.size(); i++) {
                        if (idTextField.getText().equalsIgnoreCase(listOfCustomers.get(i).getIdNum()))
                            existingCustomer = true;
                    }

                    if (!existingCustomer) {

                        try {
                            outputStream.writeObject("add customer");
                            outputStream.flush();

                            Customer cus = new Customer();
                            cus.setName(fNameTextField.getText());
                            cus.setSurname(lNameTextField.getText());
                            cus.setIdNum(idTextField.getText());
                            cus.setPhoneNum(phoneNumTextField.getText());
                            cus.setCanRent(true);

                            customersList.add(cus);
                            listOfCustomers.add(cus);

                            outputStream.flush();
                            outputStream.writeObject(cus);
                            selectCustomerCombo.addItem(cus.getName() + " " + cus.getSurname());

                            customersListModel.addRow(new Object[customersList.size() - 1]);

                            customersListModel.setValueAt(customersList.size(), customersList.size() - 1, 0);
                            customersListModel.setValueAt(cus.getName(), customersList.size() - 1, 1);
                            customersListModel.setValueAt(cus.getSurname(), customersList.size() - 1, 2);
                            customersListModel.setValueAt(cus.getIdNum(), customersList.size() - 1, 3);
                            customersListModel.setValueAt(cus.getPhoneNum(), customersList.size() - 1, 4);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }else
                        JOptionPane.showMessageDialog(null,"The customer with ID: "+ idTextField.getText() + " Already exists");
                }
            }
            else
                JOptionPane.showMessageDialog(null,"All fields must be filled.");
        });
    }

    public void addVehicle(){
        addVehicleButton.addActionListener(e -> {
            String category = (String)vehicleCategoryCombo.getSelectedItem();

            if (category!=null)
                if (!vehicleMakeTextField.getText().isEmpty() && !category.equalsIgnoreCase("select category")) {

                    boolean existingVehicle = false;
                    for (int i = 0; i < listOfVehicles.size(); i++) {
                        if (vehicleMakeTextField.getText().equalsIgnoreCase(listOfVehicles.get(i).getMake()))
                            existingVehicle = true;
                    }

                    if (!existingVehicle) {
                        Vehicle vehicle = new Vehicle();

                        try {
                            outputStream.writeObject("add vehicle");
                            outputStream.flush();

                            int categInt = 0;
                            double rentalPrc = 0.0;
                            if (category.equalsIgnoreCase("Sedan")) {
                                categInt = 1;
                                rentalPrc = 450;
                            } else if (category.equalsIgnoreCase("SUV")) {
                                categInt = 2;
                                rentalPrc = 500;
                            }
                            if (categInt == 1 || categInt == 2) {
                                vehicle.setMake(vehicleMakeTextField.getText());
                                vehicle.setCategory(categInt);
                                vehicle.setRentalPrice(rentalPrc);
                                vehicle.setAvailableForRent(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Category of the vehicle must be either 1 or 2");
                            }

                            outputStream.flush();
                            outputStream.writeObject(vehicle);
                            listOfVehicles.add(vehicle);
                            vehiclesList.add(vehicle);

                            vehiclesListModel.addRow(new Object[vehiclesList.size() - 1]);
                            vehiclesListModel.setValueAt(vehiclesList.size(), vehiclesList.size() - 1, 0);
                            vehiclesListModel.setValueAt(vehicle.getMake(), vehiclesList.size() - 1, 1);
                            vehiclesListModel.setValueAt(vehicle.getCategory(), vehiclesList.size() - 1, 2);
                            vehiclesListModel.setValueAt(vehicle.getRentalPrice(), vehiclesList.size() - 1, 3);

                        } catch (IOException ex) {
//                        System.out.println(ex.getMessage());
                            ex.printStackTrace();
                        }
                    }else
                        JOptionPane.showMessageDialog(null,"The vehicle "+ vehicleMakeTextField.getText() +" already exists");
                }else if (category.equalsIgnoreCase("select category") && !vehicleMakeTextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null,"Category must be selected.");

                else if (vehicleMakeTextField.getText().isEmpty() && !category.equalsIgnoreCase("select category"))
                    JOptionPane.showMessageDialog(null,"Vehicle make must be filled.");
                else {
                    JOptionPane.showMessageDialog(null,"Enter the vehicle make and select the category");
                }
        });
    }


    public void rentFunctions(){
                createCustomerCancelBtn.addActionListener(d -> {
                    phoneNumTextField.setText("phone number");
                    lNameTextField.setText("last name");
                    fNameTextField.setText("last name");
                    idTextField.setText("ID number");
                });

                    rentCarButton.addActionListener(ev -> {
                        String carCategory = (String)selectCategoryCombo.getSelectedItem();
                        if (!carCategory.equalsIgnoreCase("select category")) {

                            String customerName = (String) selectCustomerCombo.getSelectedItem();
                            int car = (Integer) selectCarCombo.getSelectedIndex() + 1;
                            String rentDate = rentDateTf.getText();

                            int custNum = 0;

                            try {
                                outputStream.writeObject("insert rental");
                                if (customerName != null)
                                    for (int i = 0; i < listOfCustomers.size(); i++) {
                                        if (customerName.equalsIgnoreCase(listOfCustomers.get(i).getName() + " " + listOfCustomers.get(i).getSurname())) {
                                            custNum = listOfCustomers.get(i).getCustNumber();
                                        }
                                    }

                                Rental rental = new Rental(rentDate, custNum, car);
                                outputStream.writeObject(rental);
                                listOfRentals.add(rental);

                                StringTokenizer st = new StringTokenizer(rental.getDateRental(), "/");
//                                System.out.println(rental.getDateRental());

                                int month = Integer.parseInt(st.nextToken());
                                int day = Integer.parseInt(st.nextToken());
                                int year = Integer.parseInt(st.nextToken());
                                String rentNumS = year + "" + month + "" + day + "" + rental.getCustNumber() + "" + rental.getVehNumber();

                                String msg = (String) inStream.readObject();
                                JOptionPane.showMessageDialog(null, msg);

                                if (msg.contains("You have rented"))
                                returnCarCombo.addItem(rentNumS);

                            } catch (IOException | ClassNotFoundException ex) {
//                            System.out.println(ex.getMessage());
                                ex.printStackTrace();
                            }
                        }else
                            JOptionPane.showMessageDialog(null,"Vehicle category must be selected");
                    });


        selectCategoryCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();

//                Working
                if (cb.getSelectedItem() == "Sedan") {
                    selectCarCombo.removeAllItems();

                    for (int i = 0; i < listOfVehicles.size(); i++) {
                        if (listOfVehicles.get(i).getCategory().equalsIgnoreCase("Sedan")) {
                            if (((DefaultComboBoxModel) selectCarCombo.getModel()).getIndexOf(listOfVehicles.get(i).getMake()) == -1) {
                                selectCarCombo.addItem(listOfVehicles.get(i).getMake());
                            }
                        }
                    }
                } else if (cb.getSelectedItem() == "SUV") {
                    selectCarCombo.removeAllItems();
                    for (int i = 0; i < listOfVehicles.size(); i++) {
                        if (listOfVehicles.get(i).getCategory().equalsIgnoreCase("SUV")) {
                            if (((DefaultComboBoxModel) selectCarCombo.getModel()).getIndexOf(listOfVehicles.get(i).getMake()) == -1) {
                                selectCarCombo.addItem(listOfVehicles.get(i).getMake());
                            }
                        }
                    }
                }

            }
        });
    }

    public void returnCar(){
        returnCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    outputStream.flush();
                    outputStream.writeObject("return car");
                    String rentN = (String) returnCarCombo.getSelectedItem();
                    outputStream.flush();
                    outputStream.writeObject(rentN);
                    outputStream.flush();

                    String returnDate = returnDateTf.getText();

                    outputStream.writeObject(returnDate);

                    outputStream.flush();
                    outputStream.writeObject(selectCategoryCombo.getSelectedItem());

                    double total = (Double)inStream.readObject();
                    returnCarTotal.setEditable(false);

                    returnCarTotal.setText("Total: "+ total);

                    String rMsg = (String)inStream.readObject();
                    JOptionPane.showMessageDialog(null,rMsg);

                }catch (IOException | ClassNotFoundException ex){
//                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    ArrayList<Rental> rentals;
    ArrayList<Rental> outRentals;
    public void implementMenu(){
        showAllRentals.addActionListener(e -> {
            try {
                outputStream.flush();
                outputStream.writeObject("send all returned rentals");

                rentals = (ArrayList<Rental>) inStream.readObject();

                System.out.println("size" + rentals.size());

                for (int i = 0; i < rentals.size(); i++){
                rentalsListModel.addRow(new Object[i]);
                rentalsListModel.setValueAt(rentals.get(i).getRentalNumber(),i,0);
                String date = rentals.get(i).getDateRental();
                date = date.replaceFirst("^0+(?!$)", "");

                rentalsListModel.setValueAt(date,i,1);

                String date1 =  rentals.get(i).getReturnDate();
                date1 = date1.replaceFirst("^0+(?!$)", "");
                rentalsListModel.setValueAt(date1,i,2);
                rentalsListModel.setValueAt(rentals.get(i).getTotalPrice(),i,3);
                rentalsListModel.setValueAt(rentals.get(i).getCustNumber(),i,4);
                rentalsListModel.setValueAt(rentals.get(i).getVehNumber(),i,5);
                }

            }catch (IOException | ClassNotFoundException ex){
                ex.printStackTrace();
            }
        });

        showOutRentals.addActionListener(e -> {
            try{
                outputStream.flush();
                outputStream.writeObject("send outstanding rentals");

                outRentals = (ArrayList<Rental>) inStream.readObject();

                for (int i = 0; i < outRentals.size(); i++){
                    outRentalsListModel.addRow(new Object[i]);
                    outRentalsListModel.setValueAt(outRentals.get(i).getRentalNumber(),i,0);
                    String date = outRentals.get(i).getDateRental();
                    date = date.replaceFirst("^0+(?!$)", "");
                    outRentalsListModel.setValueAt(date,i,1);
                    outRentalsListModel.setValueAt(outRentals.get(i).getCustNumber(),i,2);
                    outRentalsListModel.setValueAt(outRentals.get(i).getVehNumber(),i,3);
                }

            }catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        });
    }
    public static void main(String [] args){
        ClientSpace clientInterface = new ClientSpace();
    }
}

// TODO: Add avatars next to each customer and car.
// TODO: New Customers still can't rent.
// TODO: Fix date (hours - min - seconds)