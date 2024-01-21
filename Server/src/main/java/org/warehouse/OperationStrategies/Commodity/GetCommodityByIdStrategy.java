package org.warehouse.OperationStrategies.Commodity;

import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class GetCommodityByIdStrategy implements OperationStrategy<Commodity> {
    @Override
    public OperationMessageDTO<Commodity> execute(OperationMessageDTO<Commodity> clientObject, Database database) {
        Commodity commodity = clientObject.data();

        Commodity commodityGot = database.getCommodityById(commodity.id());

        if (commodityGot != null) {
            return new OperationMessageDTO<>("GET_COMMODITY_BY_ID_SUCCESS", commodityGot);
        }

        return new OperationMessageDTO<>("GET_COMMODITY_BY_ID_FAILURE", null);
    }
}
