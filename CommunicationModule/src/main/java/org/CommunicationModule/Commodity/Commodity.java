package org.CommunicationModule.Commodity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Commodity (String id, String productName, String category, String producer, String quantityInStock,
                         String quantityToSell, String price) implements Serializable {

    public Commodity(String id) {
        this (
                id, "", "", "", "", "", ""
        );
    }

    public String getPrice() {
        return this.price;
    }
}
