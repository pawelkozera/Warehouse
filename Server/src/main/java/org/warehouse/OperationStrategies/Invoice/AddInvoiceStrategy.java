package org.warehouse.OperationStrategies.Invoice;

import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class AddInvoiceStrategy implements OperationStrategy<Invoice> {
    @Override
    public OperationMessageDTO<Invoice> execute(OperationMessageDTO<Invoice> clientObject, Database database) {
        Invoice invoice = clientObject.data();

        if (!database.addInvoiceRecord(invoice)) {
            return new OperationMessageDTO<>("INSERT_INVOICE_FAILURE", null);
        }

        return new OperationMessageDTO<>("INSERT_INVOICE_SUCCESS", null);
    }
}
