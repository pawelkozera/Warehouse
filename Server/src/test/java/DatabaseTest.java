import org.CommunicationModule.Employee.Employee;
import org.junit.jupiter.api.*;
import org.warehouse.Database;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private Database database;

    @BeforeEach
    void setUp() {
        database = Database.getInstance();
        database.connect("warehouse_test.db");
        database.createTables();

        Employee testEmployee = new Employee(
                "TestFirstName",
                "TestLastName",
                "TestPosition",
                "TestCountry",
                "TestCity",
                "TestStreet",
                "11A",
                "Male",
                "11111111111",
                "2000-01-01",
                "123456789",
                "TestUsername",
                "TestPassword",
                "1",
                "5000",
                "TestComments"
        );

        database.addEmployee(testEmployee);
    }

    @AfterEach
    void tearDown() {
        try {
            Statement stmt = database.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM employee");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.disconnect();
    }

    @Test
    public void testAddEmployeeCorrect() {
        Employee testEmployee = new Employee(
                "Joe",
                "Doe",
                "TestPosition",
                "TestCountry",
                "TestCity",
                "TestStreet",
                "11A",
                "Male",
                "11111111111",
                "2000-01-01",
                "123456789",
                "JoeDoe",
                "JoeDoe",
                "1",
                "5000",
                "TestComments"
        );

        assertTrue(database.addEmployee(testEmployee));
    }

    @Test
    public void testAddEmployeeIncorrect() {
        Employee testEmployee = new Employee(
                "Joe",
                "Doe",
                "TestCity",
                "TestGender",
                "TestPosition",
                "123456789",
                "testLogin",
                "1",
                "5000"
        );

        assertFalse(database.addEmployee(testEmployee));
    }

    @Test
    public void testEditEmployeeCorrect() {
        Employee testEmployee = new Employee(
                "Joe",
                "Doe",
                "TestPosition",
                "TestCountry",
                "TestCity",
                "TestStreet",
                "11A",
                "Male",
                "11111111111",
                "2000-01-01",
                "123456789",
                "JoeDoe",
                "",
                "1",
                "8000",
                "TestComments"
        );

        assertTrue(database.addEmployee(testEmployee));
    }

    @Test
    public void testEditEmployeeIncorrect() {
        Employee employee = new Employee(
                "Joe",
                "Doe",
                "TestCity",
                "TestGender",
                "TestPosition",
                "123456789",
                "TestUsername",
                "1",
                "5000"
        );

        assertFalse(database.editEmployee(employee));
    }

    @Test
    public void testListEmployeesCorrect() {
        List<Employee> employees = database.listEmployees();

        assertEquals(1, employees.size());
    }

    @Test
    public void testListEmployeesCorrectEmptyList() throws SQLException {
        Statement stmt = database.getConnection().createStatement();
        stmt.executeUpdate("DELETE FROM employee");

        List<Employee> employees = database.listEmployees();

        assertTrue(employees.isEmpty());
    }

    @Test
    public void testListLoginsAndNamesCorrect() {
        List<Employee> employees = database.listLoginsAndNames();

        assertEquals(1, employees.size());

        for (Employee employee : employees) {
            assertTrue(!employee.login().isEmpty() && !employee.name().isEmpty() && !employee.surname().isEmpty());
        }
    }

    @Test
    public void testListLoginsAndNamesCorrectEmptyList() throws SQLException {
        Statement stmt = database.getConnection().createStatement();
        stmt.executeUpdate("DELETE FROM employee");

        List<Employee> employees = database.listLoginsAndNames();

        assertTrue(employees.isEmpty());
    }

    @Test
    public void testGetEmployeeByLoginCorrect() {
        Employee employee = database.getEmployeeByLogin("TestUsername");

        assertNotNull(employee);
        assertTrue(employee.login().isEmpty() && employee.password().isEmpty());
    }

    @Test
    public void testGetEmployeeByLoginIncorrect() {
        Employee employee = database.getEmployeeByLogin("DoesntExist");

        assertNull(employee);
    }
}
