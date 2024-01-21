package org.warehouse.OperationStrategies.Commodity;

import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.util.List;

public class ListCommoditiesStrategy implements OperationStrategy<List<Commodity>> {
    @Override
    public OperationMessageDTO<List<Commodity>> execute(OperationMessageDTO<List<Commodity>> clientObject, Database database) {
        List<Commodity> listOfCommodities = database.listCommodities();

        if (listOfCommodities.isEmpty()) {
            return new OperationMessageDTO<>("LIST_COMMODITIES_FAILURE", listOfCommodities);
        }

        return new OperationMessageDTO<>("LIST_COMMODITIES_SUCCESS", listOfCommodities);
    }
}
