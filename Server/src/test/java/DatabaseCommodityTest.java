import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.Commodity.CommodityReception;
import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.Employee.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.warehouse.Database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseCommodityTest {
    private Database database;

    @BeforeEach
    void setUp() {
        database = Database.getInstance();
        database.connect("warehouse_test.db");
        database.createTables();

        Commodity commodity = new Commodity("1", "testProduct", "testCategory", "testProducer", "40", "20", "2");

        database.addTestCommodity(commodity);
    }

    @AfterEach
    void tearDown() {
        try {
            Statement stmt = database.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM commodity");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.disconnect();
    }

    @Test
    public void testListCommoditiesCorrectEmptyList() throws SQLException {
        Statement stmt = database.getConnection().createStatement();
        stmt.executeUpdate("DELETE FROM commodity");

        List<Commodity> commodities = database.listCommodities();

        assertTrue(commodities.isEmpty());
    }

    @Test
    public void testListCommoditiesIncorrectEmptyList() {
        List<Commodity> commodities = database.listCommodities();
        assertFalse(commodities.isEmpty());
    }

    @Test
    public void testGetCommodityByIdIncorrect() {
        Commodity commodity = database.getCommodityById("-1");
        assertNull(commodity);
    }

    @Test
    public void testGetCommodityByIdCorrect() {
        Commodity commodity = database.getCommodityById("1");
        assertNotNull(commodity);
    }

    @Test
    public void testUpdateAddStockQuantityCorrect() {
        CommodityReception commodityReception = new CommodityReception(1, 20);
        assertTrue(database.updateAddStockQuantity(commodityReception));

        Commodity commodity = database.getCommodityById("1");
        assertEquals("60", commodity.quantityInStock());
    }

    @Test
    public void testUpdateSubstractStockQuantityCorrect() {
        CommodityRelease commodityRelease = new CommodityRelease(1, 20);
        assertTrue(database.updateSubstractStockQuantity(commodityRelease));

        Commodity commodity = database.getCommodityById("1");
        assertEquals("20", commodity.quantityInStock());
    }

    @Test
    public void testUpdateAddToSellQuantityCorrect() {
        CommodityReception commodityReception = new CommodityReception(1, 20);
        assertTrue(database.updateAddToSellQuantity(commodityReception));

        Commodity commodity = database.getCommodityById("1");
        assertEquals("40", commodity.quantityToSell());
    }
}
