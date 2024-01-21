package org.CommunicationModule.Stat;

import java.io.Serializable;

public record InvoicesStatistics (String year, String month, int invoiceCount, String amount) implements Serializable {

}
