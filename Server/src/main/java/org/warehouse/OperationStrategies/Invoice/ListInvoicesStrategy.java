package org.warehouse.OperationStrategies.Invoice;

import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.util.List;

public class ListInvoicesStrategy implements OperationStrategy<List<Invoice>> {
    @Override
    public OperationMessageDTO<List<Invoice>> execute(OperationMessageDTO<List<Invoice>> clientObject, Database database) {
        List<Invoice> listOfInvoices = database.listInvoices();

        if (listOfInvoices.isEmpty()) {
            return new OperationMessageDTO<>("LIST_INVOICES_FAILURE", listOfInvoices);
        }

        return new OperationMessageDTO<>("LIST_INVOICES_SUCCESS", listOfInvoices);
    }
}
