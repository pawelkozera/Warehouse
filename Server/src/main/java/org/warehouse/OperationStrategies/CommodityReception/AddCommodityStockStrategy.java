package org.warehouse.OperationStrategies.CommodityReception;

import org.CommunicationModule.Commodity.CommodityReception;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class AddCommodityStockStrategy implements OperationStrategy<CommodityReception> {
    @Override
    public OperationMessageDTO<CommodityReception> execute(OperationMessageDTO<CommodityReception> clientObject, Database database) {
        CommodityReception commodityReception = clientObject.data();

        if (!database.updateAddStockQuantity(commodityReception)) {
            return new OperationMessageDTO<>("ADD_COMMODITY_STOCK_FAILURE", null);
        }

        return new OperationMessageDTO<>("ADD_COMMODITY_STOCK_SUCCESS", null);
    }
}
