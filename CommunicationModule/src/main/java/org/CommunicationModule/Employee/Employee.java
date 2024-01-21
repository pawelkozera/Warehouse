package org.CommunicationModule.Employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Employee (int id, String name, String surname, String pesel, String city, String country,
                       String street, String houseNumber, String gender, LocalDate dateOfBrith, String position, String phoneNumber,
                       String password, String login, String salary, String status, String comments)  implements Serializable {
    public Employee(String name, String surname, String position,
                    String country, String city, String street, String houseNumber,
                    String gender, String pesel, String birthDate, String phone,
                    String username, String password, String status, String salary) {
        this (
                0, name, surname, pesel, city, country, street, houseNumber, gender,
                LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                position, phone, password, username, salary, status, ""
        );
    }

    public Employee(String firstName, String lastName, String position,
                    String country, String city, String street, String houseNumber,
                    String gender, String pesel, String birthDate, String phone,
                    String username, String password, String status) {
        this (
                0, firstName, lastName, pesel, city, country, street, houseNumber, gender,
                LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                position, phone, password, username, "", status, ""
        );
    }

    public Employee(String firstName, String lastName, String position,
                    String country, String city, String street, String houseNumber,
                    String gender, String pesel, String birthDate, String phone,
                    String username, String password, String status, String salary, String comments) {
        this (
                0, firstName, lastName, pesel, city, country, street, houseNumber, gender,
                LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                position, phone, password, username, salary, status, comments
        );
    }

    public Employee(String name, String surname, String city, String gender, String position, String phoneNumber, String login,String status,String salary) {
        this (
                0, name, surname, "", city, "", "", "", gender,
                LocalDate.parse("1111-11-11", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                position, phoneNumber, "", login, salary, status, ""
        );
    }

    public Employee(int id, String position, String firstName, String lastName) {
        this (
                id, firstName, lastName, "", "", "", "", "", "",
                LocalDate.parse("1111-11-11", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                position, "", "", "", "", "", ""
        );
    }

    public Employee(String login, String password) {
        this (
                0, "", "", "", "", "", "", "", "",
                LocalDate.parse("1111-11-11", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                "", "", password, login, "", "", ""
        );
    }

    public Employee(String username) {
        this (
                0, "", "", "", "", "", "", "", "",
                LocalDate.parse("1111-11-11", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                "", "", "", username, "", "", ""
        );
    }

    public Employee withStatus(String newStatus) {
        return new Employee(
                this.id, this.name, this.surname, this.pesel, this.city, this.country, this.street,
                this.houseNumber, this.gender, this.dateOfBrith, this.position,
                this.phoneNumber, this.password, this.login, this.salary, newStatus, this.comments
        );
    }
}
