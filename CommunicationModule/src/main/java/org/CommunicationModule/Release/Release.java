package org.CommunicationModule.Release;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Release (String primaryId, LocalDate releaseDate, int employeeId, String invoiceId) implements Serializable {
    public Release(String primaryId, String releaseDate, int employeeId, String invoiceId) {
        this(
                primaryId,
                LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                employeeId,
                invoiceId
        );
    }
}