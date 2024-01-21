package org.warehouse.OperationStrategies.Invoice;

import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class UpdateInvoiceStatusStrategy implements OperationStrategy<Invoice> {
    @Override
    public OperationMessageDTO<Invoice> execute(OperationMessageDTO<Invoice> clientObject, Database database) {
        Invoice invoice = clientObject.data();

        if (!database.updateInvoiceStatus(invoice)) {
            return new OperationMessageDTO<>("UPDATE_INVOICE_STATUS_FAILURE", null);
        }

        return new OperationMessageDTO<>("UPDATE_INVOICE_STATUS_SUCCESS", null);
    }
}
