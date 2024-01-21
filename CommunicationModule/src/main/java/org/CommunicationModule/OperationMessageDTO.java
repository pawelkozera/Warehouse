package org.CommunicationModule;

import java.io.Serializable;

public record OperationMessageDTO<T> (String responseType, T data) implements Serializable {
}
