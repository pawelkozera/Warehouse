package org.CommunicationModule.Reception;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Reception (int id, LocalDate receptionDate, int employeeId) implements Serializable {
    public Reception(int id, String receptionDate, int employeeId) {
        this(
                id,
                LocalDate.parse(receptionDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                employeeId
        );
    }

    public Reception(int id) {
        this(
                id, LocalDate.parse("1111-11-11", DateTimeFormatter.ofPattern("yyyy-MM-dd")), 0
        );
    }
}