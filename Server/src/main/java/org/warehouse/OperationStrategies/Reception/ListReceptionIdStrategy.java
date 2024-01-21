package org.warehouse.OperationStrategies.Reception;

import org.CommunicationModule.OperationMessageDTO;
import org.CommunicationModule.Reception.Reception;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.util.List;

public class ListReceptionIdStrategy implements OperationStrategy<List<Reception>> {
    @Override
    public OperationMessageDTO<List<Reception>> execute(OperationMessageDTO<List<Reception>> clientObject, Database database) {
        List<Reception> listOfReceptions = database.listReceptionId();

        if (listOfReceptions.isEmpty()) {
            return new OperationMessageDTO<>("GET_ALL_RECEPTION_ID_FAILURE", listOfReceptions);
        }

        return new OperationMessageDTO<>("GET_ALL_RECEPTION_ID_SUCCESS", listOfReceptions);
    }
}
