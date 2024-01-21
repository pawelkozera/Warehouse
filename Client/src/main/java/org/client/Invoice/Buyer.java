package org.client.Invoice;

public record Buyer(String firstName, String lastName, String pesel, String phoneNumber, String city, String street, String houseNumber, String nip, String totalCost) {
}