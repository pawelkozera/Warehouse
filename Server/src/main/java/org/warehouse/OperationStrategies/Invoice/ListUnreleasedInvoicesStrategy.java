package org.warehouse.OperationStrategies.Invoice;

import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.util.List;

public class ListUnreleasedInvoicesStrategy implements OperationStrategy<List<Invoice>> {
    @Override
    public OperationMessageDTO<List<Invoice>> execute(OperationMessageDTO<List<Invoice>> clientObject, Database database) {
        List<Invoice> litsOfUnreleasedInvoices = database.listUnreleasedInvoices();

        if(litsOfUnreleasedInvoices.isEmpty()) {
            return new OperationMessageDTO<>("LIST_UNRELEASED_INVOICES_FAILURE", litsOfUnreleasedInvoices);
        }

        return new OperationMessageDTO<>("LIST_UNRELEASED_INVOICES_SUCCESS", litsOfUnreleasedInvoices);
    }
}
