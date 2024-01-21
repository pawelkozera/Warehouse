package org.warehouse.OperationStrategies.Invoice;

import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.OperationMessageDTO;
import org.CommunicationModule.Stat.InvoicesStatistics;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.util.List;

public class ListInvoiceStatsStrategy implements OperationStrategy<List<InvoicesStatistics>> {
    @Override
    public OperationMessageDTO<List<InvoicesStatistics>> execute(OperationMessageDTO<List<InvoicesStatistics>> clientObject, Database database) {
        List<InvoicesStatistics> listOfInvoiceStats = database.listInvoiceStats();

        if(listOfInvoiceStats.isEmpty()) {
            return new OperationMessageDTO<>("LIST_OF_INVOICE_STATS_FAILURE", listOfInvoiceStats);
        }
        return new OperationMessageDTO<>("LIST_OF_INVOICE_STATS_SUCCESS", listOfInvoiceStats);
    }
}
