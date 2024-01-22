import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.Commodity.CommodityReception;
import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.Reception.Reception;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.warehouse.Database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseReceptionTable {
    private Database database;

    @BeforeEach
    void setUp() {
        database = Database.getInstance();
        database.connect("warehouse_test.db");
        database.createTables();

        Reception reception = new Reception(1, "2023-05-05", 1);
        database.addReceptionRecord(reception);
    }

    @AfterEach
    void tearDown() {
        try {
            Statement stmt = database.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM reception_table");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.disconnect();
    }

    @Test
    public void testListReceptionIdCorrectEmptyList() throws SQLException {
        Statement stmt = database.getConnection().createStatement();
        stmt.executeUpdate("DELETE FROM reception_table");

        List<Reception> receptions = database.listReceptionId();

        assertTrue(receptions.isEmpty());
    }

    @Test
    public void testListReceptionIdIncorrectEmptyList() {
        List<Reception> receptions = database.listReceptionId();
        assertFalse(receptions.isEmpty());
    }
}
