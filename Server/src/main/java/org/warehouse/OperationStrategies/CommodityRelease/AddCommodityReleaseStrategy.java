package org.warehouse.OperationStrategies.CommodityRelease;

import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class AddCommodityReleaseStrategy implements OperationStrategy<CommodityRelease> {
    @Override
    public OperationMessageDTO<CommodityRelease> execute(OperationMessageDTO<CommodityRelease> clientObject, Database database) {
        CommodityRelease commodityRelease = clientObject.data();

        if (!database.addCommodityReleaseRecord(commodityRelease)) {
            return new OperationMessageDTO<>("ADD_COMMODITY_RELEASE_FAILURE", null);
        }

        return new OperationMessageDTO<>("ADD_COMMODITY_RELEASE_SUCCESS", null);
    }
}
