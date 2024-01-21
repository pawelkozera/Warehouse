package org.warehouse.OperationStrategies.Release;

import org.CommunicationModule.OperationMessageDTO;
import org.CommunicationModule.Release.Release;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class AddReleaseStrategy implements OperationStrategy<Release> {
    @Override
    public OperationMessageDTO<Release> execute(OperationMessageDTO<Release> clientObject, Database database) {
        Release release = clientObject.data();

        if (!database.addReleaseRecord(release)) {
            return new OperationMessageDTO<>("ADD_RELEASE_FAILURE", null);
        }

        return new OperationMessageDTO<>("ADD_RELEASE_SUCCESS", null);
    }
}
