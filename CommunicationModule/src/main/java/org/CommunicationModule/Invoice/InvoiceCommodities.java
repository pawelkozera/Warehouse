package org.CommunicationModule.Invoice;

import java.io.Serializable;

public record InvoiceCommodities(String invoice_id, int commodity_id, int quantity) implements Serializable {

}
