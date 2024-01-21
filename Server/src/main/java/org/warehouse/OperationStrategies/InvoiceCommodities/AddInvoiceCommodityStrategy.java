package org.warehouse.OperationStrategies.InvoiceCommodities;

import org.CommunicationModule.Invoice.InvoiceCommodities;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class AddInvoiceCommodityStrategy implements OperationStrategy<InvoiceCommodities> {
    @Override
    public OperationMessageDTO<InvoiceCommodities> execute(OperationMessageDTO<InvoiceCommodities> clientObject, Database database) {
        InvoiceCommodities invoiceCommodities = clientObject.data();

        if (!database.addInvoiceCommodityRecord(invoiceCommodities)) {
            return new OperationMessageDTO<>("INSERT_INVOICE_COMMODITY_FAILURE", null);
        }

        return new OperationMessageDTO<>("INSERT_INVOICE_COMMODITY_SUCCESS", null);
    }
}
