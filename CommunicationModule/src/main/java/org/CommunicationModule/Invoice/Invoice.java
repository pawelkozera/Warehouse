package org.CommunicationModule.Invoice;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Invoice (String id, String wydane, String buyersName, String buyersSurname, String buyersPesel, String buyersNIP,
                       String buyersPhoneNr, String buyersCity, String buyersStreet, String buyersHouseNr, String purchaseSum,
                       LocalDate purchaseDate, String employeeId) implements Serializable {

    public Invoice(String id, String wydane, String buyersName, String buyersSurname, String buyersPesel, String buyersNIP,
                   String buyersPhoneNr, String buyersCity, String buyersStreet, String buyersHouseNr, String purchaseSum,
                   String purchaseDate, String employeeId) {
        this(id, wydane, buyersName, buyersSurname, buyersPesel, buyersNIP,
                buyersPhoneNr, buyersCity, buyersStreet, buyersHouseNr, purchaseSum,
                LocalDate.parse(purchaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")), employeeId);
    }
    public Invoice(String id, String wydane) {
        this(id, wydane, "", "", "", "", "",
                "", "", "", "",
                LocalDate.parse("1111-11-11", DateTimeFormatter.ofPattern("yyyy-MM-dd")), "");
    }
}
