package org.warehouse;

import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.Commodity.CommodityReception;
import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.Invoice.InvoiceCommodities;
import org.CommunicationModule.Reception.Reception;
import org.CommunicationModule.Release.Release;
import org.CommunicationModule.Stat.InvoicesStatistics;
import org.warehouse.PasswordSecurity.Argon2PasswordHashing;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    private static Database instance;
    private Connection connection = null;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:warehouse.db");
            System.out.println("Connected to the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(String databaseName) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
            System.out.println("Connected to the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        String employeeTable = "CREATE TABLE IF NOT EXISTS employee (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " first_name TEXT NOT NULL CHECK (LENGTH(first_name) <= 60),\n"
                + " last_name TEXT NOT NULL CHECK (LENGTH(last_name) <= 60),\n"
                + " position TEXT NOT NULL CHECK (LENGTH(position) <= 60),\n"
                + " country TEXT NOT NULL CHECK (LENGTH(country) <= 60),\n"
                + " city TEXT NOT NULL CHECK (LENGTH(city) <= 100),\n"
                + " street TEXT NOT NULL CHECK (LENGTH(street) <= 100),\n"
                + " house_number TEXT NOT NULL CHECK (LENGTH(house_number) <= 4),\n"
                + " gender TEXT NOT NULL CHECK (LENGTH(gender) <= 10),\n"
                + " pesel TEXT NOT NULL CHECK (LENGTH(pesel) = 11),\n"
                + " birthDate DATE NOT NULL,\n"
                + " phone TEXT NOT NULL CHECK (LENGTH(phone) <= 15),\n"
                + " username TEXT NOT NULL CHECK (LENGTH(username) <= 50),\n"
                + " password TEXT NOT NULL CHECK (LENGTH(password) <= 255),\n"
                + " status INTEGER CHECK (status IN (0, 1)) NOT NULL,\n" // 0 - inactive, 1 - active
                + " salary INTEGER NOT NULL DEFAULT 3490,\n"
                + " comments TEXT CHECK (LENGTH(comments) <= 1000)\n"
                + ");";

        String commodityTable = "CREATE TABLE IF NOT EXISTS commodity (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "name TEXT NOT NULL CHECK(LENGTH(name) <= 100), \n"
                + "category TEXT NOT NULL CHECK(LENGTH(category) <=60), \n"
                + "producer TEXT NOT NULL CHECK(LENGTH(producer) <= 100), \n"
                + "quantity_stock INTEGER NOT NULL, \n"
                + "quantity_to_sell INTEGER NOT NULL, \n"
                + "price FLOAT NOT NULL\n"
                + ");";

        String releaseTable = "CREATE TABLE IF NOT EXISTS release_table (\n"
                + "id INTEGER PRIMARY KEY, \n"
                + "data_wydania DATE NOT NULL, \n"
                + "employee_id INTEGER NOT NULL CONSTRAINT release_table_employee_fk REFERENCES employee(id), \n"
                + "invoice_id INTEGER NOT NULL CONSTRAINT release_table_invoice_fk REFERENCES invoice(id) \n"
                + ");";

        String commodityReleaseTable = "CREATE TABLE IF NOT EXISTS commodity_release (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "commodity_id INTEGER NOT NULL CONSTRAINT commodity_release_commodity_fk REFERENCES commodity(id), \n"
                + "release_id INTEGER NOT NULL CONSTRAINT commodity_release_release_fk REFERENCES release_table(id), \n"
                + "quantity INTEGER NOT NULL \n"
                + ");";

        String receptionTable = "CREATE TABLE IF NOT EXISTS reception_table (\n"
                + "id INTEGER PRIMARY KEY, \n"
                + "data_wydania DATE NOT NULL, \n"
                + "employee_id INTEGER NOT NULL CONSTRAINT reception_table_employee_fk REFERENCES employee(id) \n"
                + ");";

        String commodityReceptionTable = "CREATE TABLE IF NOT EXISTS commodity_reception (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "commodity_id INTEGER NOT NULL CONSTRAINT commodity_reception_commodity_fk REFERENCES commodity(id), \n"
                + "reception_id INTEGER NOT NULL CONSTRAINT commodity_reception_reception_fk REFERENCES reception_table(id), \n"
                + "quantity INTEGER NOT NULL \n"
                + ");";

        String invoiceTable = "CREATE TABLE IF NOT EXISTS invoice (\n"
                + "id INTEGER PRIMARY KEY, \n"
                + "wydane BOOL NOT NULL, \n"
                + "buyers_name TEXT NOT NULL, \n"
                + "buyers_surname TEXT NOT NULL, \n"
                + "buyers_pesel TEXT NOT NULL, \n"
                + "buyers_nip TEXT, \n"
                + "buyers_phone_nr TEXT NOT NULL, \n"
                + "buyers_city TEXT NOT NULL, \n"
                + "buyers_street TEXT NOT NULL, \n"
                + "buyers_house_nr TEXT NOT NULL, \n"
                + "purchase_sum FLOAT NOT NULL, \n"
                + "purchase_date DATE NOT NULL, \n"
                + "employee_id INTEGER NOT NULL CONSTRAINT invoice_employee_fk REFERENCES employee(id) \n"
                + ");";

        String invoiceCommodityTable = "CREATE TABLE IF NOT EXISTS invoice_commodity (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "invoice_id INTEGER NOT NULL CONSTRAINT invoice_commodity_invoice_fk REFERENCES invoice(id), \n"
                + "commodity_id INTEGER NOT NULL CONSTRAINT invoice_commodity_commodity_fk REFERENCES commodity(id), \n"
                + "quantity INTEGER NOT NULL \n"
                + ");";

        createTable(employeeTable);
        createTable(commodityTable);
        createTable(invoiceTable);
        createTable(releaseTable);
        createTable(commodityReleaseTable);
        createTable(receptionTable);
        createTable(commodityReceptionTable);
        createTable(invoiceCommodityTable);
    }

    private void createTable(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean addEmployee(Employee employee) {
        try {
            String insertEmployee = "INSERT INTO employee (first_name, last_name, position, country, city, street, house_number, gender, pesel, birthDate, phone, username, password, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statementEmployee = connection.prepareStatement(insertEmployee);

            statementEmployee.setString(1, employee.name());
            statementEmployee.setString(2, employee.surname());
            statementEmployee.setString(3, employee.position());
            statementEmployee.setString(4, employee.country());
            statementEmployee.setString(5, employee.city());
            statementEmployee.setString(6, employee.street());
            statementEmployee.setString(7, employee.houseNumber());
            statementEmployee.setString(8, employee.gender());
            statementEmployee.setString(9, employee.pesel());
            statementEmployee.setString(10, convertToSqlDate(employee.dateOfBrith()));
            statementEmployee.setString(11, employee.phoneNumber());
            statementEmployee.setString(12, employee.login());
            statementEmployee.setString(13, Argon2PasswordHashing.hashPassword(employee.password()));
            statementEmployee.setInt(14, Integer.parseInt(employee.status()));

            statementEmployee.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Adding a new employee failed.");
            e.printStackTrace();
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("NullPointerException during employee update. Check for null values.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Updating employee failed due to an unexpected error.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean editEmployee(Employee employee) {
        try {
            String updateEmployee = "UPDATE employee SET " +
                    "first_name = ?, " +
                    "last_name = ?, " +
                    "position = ?, " +
                    "country = ?, " +
                    "city = ?, " +
                    "street = ?, " +
                    "house_number = ?, " +
                    "gender = ?, " +
                    "pesel = ?, " +
                    "birthDate = ?, " +
                    "phone = ?, " +
                    "status = ?, " +
                    "salary = ?, " +
                    "comments = ? " +
                    "WHERE username = ?";

            PreparedStatement statementEmployee = connection.prepareStatement(updateEmployee);

            statementEmployee.setString(1, employee.name());
            statementEmployee.setString(2, employee.surname());
            statementEmployee.setString(3, employee.position());
            statementEmployee.setString(4, employee.country());
            statementEmployee.setString(5, employee.city());
            statementEmployee.setString(6, employee.street());
            statementEmployee.setString(7, employee.houseNumber());
            statementEmployee.setString(8, employee.gender());
            statementEmployee.setString(9, employee.pesel());
            statementEmployee.setString(10, convertToSqlDate(employee.dateOfBrith()));
            statementEmployee.setString(11, employee.phoneNumber());
            statementEmployee.setInt(12, Integer.parseInt(employee.status()));
            statementEmployee.setInt(13, Integer.parseInt(employee.salary()));
            statementEmployee.setString(14, employee.comments());
            statementEmployee.setString(15, employee.login());

            statementEmployee.executeUpdate();

            return true;
        }
        catch (SQLException e) {
            System.out.println("Updating employee failed. Check for SQL errors.");
            e.printStackTrace();
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("NullPointerException during employee update. Check for null values.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Updating employee failed due to an unexpected error.");
            e.printStackTrace();
            return false;
        }
    }

    private String convertToSqlDate(LocalDate localDate) {
        try {
            java.util.Date utilDate = java.sql.Date.valueOf(localDate);
            return new Date(utilDate.getTime()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Employee> listEmployees() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee");

            List<Employee> employees = new ArrayList<>();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("position"),
                        "",
                        resultSet.getString("city"),
                        "",
                        "",
                        resultSet.getString("gender"),
                        "",
                        resultSet.getString("birthDate"),
                        resultSet.getString("phone"),
                        resultSet.getString("username"),
                        "",
                        Integer.toString(resultSet.getInt("status")),
                        Integer.toString(resultSet.getInt("salary"))
                );

                employees.add(employee);
            }

            statement.close();

            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Employee> listLoginsAndNames() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT username, first_name, last_name FROM employee");

            List<Employee> employees = new ArrayList<>();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "1111-11-11",
                        "",
                        resultSet.getString("username"),
                        "",
                        "",
                        ""
                );

                employees.add(employee);
            }

            statement.close();

            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Employee getEmployeeByLogin(String employeeLogin) {
        try {
            String findEmployee = "SELECT * FROM employee WHERE username = ?";
            PreparedStatement statementEmployee = connection.prepareStatement(findEmployee);
            statementEmployee.setString(1, employeeLogin);
            ResultSet resultSet = statementEmployee.executeQuery();

            Employee employee = new Employee(
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("position"),
                    resultSet.getString("country"),
                    resultSet.getString("city"),
                    resultSet.getString("street"),
                    resultSet.getString("house_number"),
                    resultSet.getString("gender"),
                    resultSet.getString("pesel"),
                    resultSet.getString("birthDate"),
                    resultSet.getString("phone"),
                    "",
                    "",
                    resultSet.getString("status"),
                    resultSet.getString("salary"),
                    resultSet.getString("comments")
            );

            statementEmployee.close();

            return employee;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Commodity> listCommodities() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM commodity");

            List<Commodity> commodities = new ArrayList<>();

            while (resultSet.next()) {
                Commodity commodity = new Commodity(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getString("producer"),
                        Integer.toString(resultSet.getInt("quantity_stock")),
                        Integer.toString(resultSet.getInt("quantity_to_sell")),
                        Float.toString(resultSet.getInt("price"))
                );

                commodities.add(commodity);
            }

            statement.close();

            return commodities;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Commodity getCommodityById(String commodityId) {
        try {
            String findCommodity = "SELECT * FROM commodity WHERE id = ?";
            PreparedStatement statementCommodity = connection.prepareStatement(findCommodity);
            statementCommodity.setString(1, commodityId);
            ResultSet resultSet = statementCommodity.executeQuery();

            Commodity commodity = new Commodity(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("category"),
                    resultSet.getString("producer"),
                    Integer.toString(resultSet.getInt("quantity_stock")),
                    Integer.toString(resultSet.getInt("quantity_to_sell")),
                    Float.toString(resultSet.getInt("price"))
            );

            statementCommodity.close();

            return commodity;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Invoice> listInvoices() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM invoice");

            List<Invoice> invoices = new ArrayList<>();

            while (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getString("id"),
                        Boolean.toString(resultSet.getBoolean("wydane"))
                );

                invoices.add(invoice);
            }

            statement.close();

            return invoices;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Invoice> listUnreleasedInvoices() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM invoice WHERE wydane = false");

            List <Invoice> unreleasedInvoices = new ArrayList<>();

            while (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getString("id"),
                        Boolean.toString(resultSet.getBoolean("wydane"))
                );

                unreleasedInvoices.add(invoice);
            }

            statement.close();
            return unreleasedInvoices;
        }catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean updateInvoiceStatus(Invoice invoice) {
        try {
            String updateInvoice = "UPDATE invoice SET wydane = true WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(updateInvoice);
            statement.setString(1, invoice.id());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Updating invoices status failed");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Updating invoices status failed");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addReleaseRecord(Release release) {
        try {
            String insertRelease = "INSERT INTO release_table (id, data_wydania, employee_id, invoice_id) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement statementRelease = connection.prepareStatement(insertRelease);

            statementRelease.setString(1, release.primaryId());
            statementRelease.setString(2, convertToSqlDate(release.releaseDate()));
            statementRelease.setInt(3, release.employeeId());
            statementRelease.setString(4, release.invoiceId());

            statementRelease.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Adding a new release record failed.");
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            System.out.println("NullPointerException during release record insertion. Check for null values.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Inserting release record failed due to an unexpected error.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addCommodityReleaseRecord(CommodityRelease commodityRelease) {
        try {
            String insertCommodityRelease = "INSERT INTO commodity_release (commodity_id, release_id, quantity) " +
                    "VALUES (?, ?, ?)";

            try (PreparedStatement statementCommodityRelease = connection.prepareStatement(insertCommodityRelease)) {
                statementCommodityRelease.setInt(1, commodityRelease.commodityId());
                statementCommodityRelease.setString(2, commodityRelease.releaseId());
                statementCommodityRelease.setInt(3, commodityRelease.quantity());

                statementCommodityRelease.executeUpdate();
                System.out.println("Record added to commodity_release table successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding record to commodity_release table: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAddStockQuantity(CommodityReception commodityReception) {
        try {
            String updateQuantity;

            updateQuantity = "UPDATE commodity SET quantity_stock = quantity_stock + ? WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(updateQuantity);
            statement.setInt(1, commodityReception.quantity());
            statement.setInt(2, commodityReception.commodityId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Updating commodity stock quantity failed");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubstractStockQuantity(CommodityRelease commodityRelease) {
        try {
            String updateQuantity;
            updateQuantity = "UPDATE commodity SET quantity_stock = quantity_stock - ? WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(updateQuantity);
            statement.setInt(1, commodityRelease.quantity());
            statement.setInt(2, commodityRelease.commodityId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Updating commodity stock quantity failed");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAddToSellQuantity(CommodityReception commodityReception) {
        try {
            String updateQuantity;

            updateQuantity = "UPDATE commodity SET quantity_to_sell = quantity_to_sell + ? WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(updateQuantity);
            statement.setInt(1, commodityReception.quantity());
            statement.setInt(2, commodityReception.commodityId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Updating commodity to sell quantity failed");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubstractToSellQuantity(CommodityRelease commodityRelease) {
        try {
            String updateQuantity;
            updateQuantity = "UPDATE commodity SET quantity_to_sell = quantity_to_sell - ? WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(updateQuantity);
            statement.setInt(1, commodityRelease.quantity());
            statement.setString(2, commodityRelease.releaseId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Updating commodity to sell quantity failed");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addReceptionRecord(Reception reception) {
        try {
            String insertReception = "INSERT INTO reception_table (id, data_wydania, employee_id) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement statementReception = connection.prepareStatement(insertReception);

            statementReception.setInt(1, reception.id());
            statementReception.setString(2, convertToSqlDate(reception.receptionDate()));
            statementReception.setInt(3, reception.employeeId());

            statementReception.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Adding a new reception record failed.");
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            System.out.println("NullPointerException during reception record insertion. Check for null values.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Inserting reception record failed due to an unexpected error.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addCommodityReceptionRecord(CommodityReception commodityReception) {
        try {
            String insertCommodityReception = "INSERT INTO commodity_reception (commodity_id, reception_id, quantity) " +
                    "VALUES (?, ?, ?)";

            try (PreparedStatement statementCommodityReception = connection.prepareStatement(insertCommodityReception)) {
                statementCommodityReception.setInt(1, commodityReception.commodityId());
                statementCommodityReception.setInt(2, commodityReception.receptionId());
                statementCommodityReception.setInt(3, commodityReception.quantity());

                statementCommodityReception.executeUpdate();
                System.out.println("Record added to commodity_reception table successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding record to commodity_reception table: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Reception> listReceptionId() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM reception_table");

            List <Reception> receptionsId = new ArrayList<>();

            while (resultSet.next()) {
                Reception reception = new Reception(
                        resultSet.getInt("id")
                );

                receptionsId.add(reception);
            }

            statement.close();
            return receptionsId;
        }catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Employee login(Employee employee) {
        try {
            String loginQuery = "SELECT id, position, password, first_name, last_name FROM employee WHERE username = ? AND status = 1";
            try (PreparedStatement statement = connection.prepareStatement(loginQuery)) {
                statement.setString(1, employee.login());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String hashedPasswordFromDatabase = resultSet.getString("password");
                    if (Argon2PasswordHashing.checkPassword(employee.password(), hashedPasswordFromDatabase)) {
                        return new Employee(resultSet.getInt("id"), resultSet.getString("position"), resultSet.getString("first_name"), resultSet.getString("last_name"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Login failed. Check for SQL errors.");
            e.printStackTrace();
        }

        return new Employee(-1, "", "", "");
    }

    public boolean addInvoiceRecord(Invoice invoice) {
        try {
            String insertInvoice = "INSERT INTO invoice (id, wydane, buyers_name, buyers_surname, " +
                    "buyers_pesel, buyers_nip, buyers_phone_nr, buyers_city, buyers_street, buyers_house_nr, " +
                    "purchase_sum, purchase_date, employee_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statementInvoice = connection.prepareStatement(insertInvoice);

            statementInvoice.setString(1, invoice.id());
            statementInvoice.setString(2, invoice.wydane());
            statementInvoice.setString(3, invoice.buyersName());
            statementInvoice.setString(4, invoice.buyersSurname());
            statementInvoice.setString(5, invoice.buyersPesel());
            statementInvoice.setString(6, invoice.buyersNIP());
            statementInvoice.setString(7, invoice.buyersPhoneNr());
            statementInvoice.setString(8, invoice.buyersCity());
            statementInvoice.setString(9, invoice.buyersStreet());
            statementInvoice.setString(10, invoice.buyersHouseNr());
            statementInvoice.setString(11, invoice.purchaseSum());
            statementInvoice.setString(12, convertToSqlDate(invoice.purchaseDate()));
            statementInvoice.setString(13, invoice.employeeId());

            statementInvoice.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Adding a new invoice failed.");
            e.printStackTrace();
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("NullPointerException during invoice update. Check for null values.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Updating invoice failed due to an unexpected error.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addInvoiceCommodityRecord(InvoiceCommodities invoiceCommodities) {
        try {
            String insertInvoiceCommodity = "INSERT INTO invoice_commodity (invoice_id, commodity_id, quantity) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement statementInvoiceCommodity = connection.prepareStatement(insertInvoiceCommodity);

            statementInvoiceCommodity.setString(1, invoiceCommodities.invoice_id());
            statementInvoiceCommodity.setInt(2, invoiceCommodities.commodity_id());
            statementInvoiceCommodity.setInt(3, invoiceCommodities.quantity());

            statementInvoiceCommodity.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Adding a new invoice commodity record failed.");
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            System.out.println("NullPointerException during invoice commodity record insertion. Check for null values.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Inserting invoice commodity record failed due to an unexpected error.");
            e.printStackTrace();
            return false;
        }
    }

    public List<InvoicesStatistics> listInvoiceStats() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT strftime('%Y', purchase_date) AS year,\n" +
                                                             " strftime('%m', purchase_date) AS month,\n" +
                                                            " COUNT(*) AS invoice_count,\n" +
                                                            " SUM(purchase_sum) AS total_amount\n" +
                                                            " FROM invoice\n" +
                                                            " GROUP BY strftime('%Y-%m', purchase_date);\n");

            List <InvoicesStatistics> invoiceStats = new ArrayList<>();

            while (resultSet.next()) {
                InvoicesStatistics invoice = new InvoicesStatistics(
                        resultSet.getString("year"),
                        resultSet.getString("month"),
                        resultSet.getInt("invoice_count"),
                        resultSet.getString("total_amount")
                );

                invoiceStats.add(invoice);
            }

            statement.close();
            return invoiceStats;
        }catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean addTestCommodity(Commodity commodity) {
        try {
            String insertEmployee = "INSERT INTO commodity (id, name, category, producer, quantity_stock, quantity_to_sell, price) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statementEmployee = connection.prepareStatement(insertEmployee);

            statementEmployee.setInt(1, Integer.parseInt(commodity.id()));
            statementEmployee.setString(2, commodity.productName());
            statementEmployee.setString(3, commodity.category());
            statementEmployee.setString(4, commodity.producer());
            statementEmployee.setInt(5, Integer.parseInt(commodity.quantityInStock()));
            statementEmployee.setInt(6, Integer.parseInt(commodity.quantityToSell()));
            statementEmployee.setFloat(7, Float.parseFloat(commodity.price()));

            statementEmployee.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Adding a new commodity failed.");
            e.printStackTrace();
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("NullPointerException during commodity update. Check for null values.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Updating commodity failed due to an unexpected error.");
            e.printStackTrace();
            return false;
        }
    }
}
