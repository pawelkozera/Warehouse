package org.warehouse.OperationStrategies.CommodityRelease;

import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class SubstractCommodityToSellStrategy implements OperationStrategy<CommodityRelease> {
    @Override
    public OperationMessageDTO<CommodityRelease> execute(OperationMessageDTO<CommodityRelease> clientObject, Database database) {
        CommodityRelease commodityRelease = clientObject.data();

        if (!database.updateSubstractToSellQuantity(commodityRelease)) {
            return new OperationMessageDTO<>("SUBSTRACT_COMMODITY_TO_SELL_FAILURE", null);
        }

        return new OperationMessageDTO<>("SUBSTRACT_COMMODITY_TO_SELL_SUCCESS", null);
    }
}
