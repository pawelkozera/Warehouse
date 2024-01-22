import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.Reception.Reception;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.warehouse.Database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseInvoiceTest {
    private Database database;

    @BeforeEach
    void setUp() {
        database = Database.getInstance();
        database.connect("warehouse_test.db");
        database.createTables();

        Invoice invoiceReleased = new Invoice("1", "1", "Joe", "Doe", "01234567890", "1234567890",
                "123456789", "City", "Street", "12a", "200", "2024-04-04", "1");

        database.addInvoiceRecord(invoiceReleased);

        Invoice invoiceUnreleased = new Invoice("2", "0", "Joe", "Doe", "01234567890", "1234567890",
                "123456789", "City", "Street", "12a", "200", "2024-04-04", "1");

        database.addInvoiceRecord(invoiceUnreleased);
    }

    @AfterEach
    void tearDown() {
        try {
            Statement stmt = database.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM invoice");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.disconnect();
    }

    @Test
    public void testListInvoicesCorrectEmptyList() throws SQLException {
        Statement stmt = database.getConnection().createStatement();
        stmt.executeUpdate("DELETE FROM invoice");

        List<Invoice> invoices = database.listInvoices();

        assertTrue(invoices.isEmpty());
    }

    @Test
    public void testListInvoicesIncorrectEmptyList() {
        List<Invoice> invoices = database.listInvoices();
        assertFalse(invoices.isEmpty());
    }

    @Test
    public void testListUnreleasedInvoicesCorrect() {
        List<Invoice> invoices = database.listUnreleasedInvoices();

        assertFalse(invoices.isEmpty());
        assertEquals(1, invoices.size());
    }

    @Test
    public void testUpdateInvoiceStatusCorrect() {
        Invoice invoice = new Invoice("2", "1");
        database.updateInvoiceStatus(invoice);
        List<Invoice> invoices = database.listUnreleasedInvoices();

        assertTrue(invoices.isEmpty());
    }
}
