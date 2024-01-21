package org.warehouse.OperationStrategies.Reception;

import org.CommunicationModule.OperationMessageDTO;
import org.CommunicationModule.Reception.Reception;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class AddReceptionStrategy implements OperationStrategy<Reception> {
    @Override
    public OperationMessageDTO<Reception> execute(OperationMessageDTO<Reception> clientObject, Database database) {
        Reception reception = clientObject.data();

        if (!database.addReceptionRecord(reception)) {
            return new OperationMessageDTO<>("ADD_RECEPTION_FAILURE", null);
        }

        return new OperationMessageDTO<>("ADD_RECEPTION_SUCCESS", null);
    }
}
