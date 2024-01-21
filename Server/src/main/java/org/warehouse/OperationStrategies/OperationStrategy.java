package org.warehouse.OperationStrategies;

import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;

public interface OperationStrategy<T> {
    OperationMessageDTO<T> execute(OperationMessageDTO<T> clientObject, Database database);
}