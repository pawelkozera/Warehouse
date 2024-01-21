package org.warehouse.OperationStrategies.CommodityRelease;

import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class SubstractCommodityStockStrategy implements OperationStrategy<CommodityRelease> {

    @Override
    public OperationMessageDTO<CommodityRelease> execute(OperationMessageDTO<CommodityRelease> clientObject, Database database) {
        CommodityRelease commodityRelease = clientObject.data();

        if (!database.updateSubstractStockQuantity(commodityRelease)) {
            return new OperationMessageDTO<>("SUBSTRACT_COMMODITY_STOCK_FAILURE", null);
        }

        return new OperationMessageDTO<>("SUBSTRACT_COMMODITY_STOCK_SUCCESS", null);
    }
}
