package org.client.CommunicationModule;

import org.CommunicationModule.OperationMessageDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationUtils {

    public static OperationMessageDTO<?> sendMessageToServer(OperationMessageDTO<?> operationMessageDTO) {
        OperationMessageDTO<?> response = new OperationMessageDTO<>("Failed", null);
        Socket socket = null;

        try {
            socket = new Socket("localhost", 1025);

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            outputStream.writeObject(operationMessageDTO);

            response = (OperationMessageDTO<?>) inputStream.readObject();
            System.out.println("Server response: " + response.responseType());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }
}