package org.warehouse;

import org.CommunicationModule.*;
import org.warehouse.OperationStrategyFactory.OperationStrategyFactory;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Database database;
    private OperationStrategyFactory strategyFactory;

    public Server(OperationStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public void start() {
        System.out.println("Server started.");

        database = Database.getInstance();

        database.connect();
        database.createTables();

        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            int port = 1025;
            ServerSocket serverSocket = new ServerSocket(port);
            while (!Thread.interrupted()) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            executorService.shutdown();
            database.disconnect();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            OperationMessageDTO<?> clientObject = (OperationMessageDTO<?>) objectInputStream.readObject();

            OperationMessageDTO<?> responseDTO = processClientObject(clientObject);

            objectOutputStream.writeObject(responseDTO);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Exception while handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Exception while closing client socket: " + e.getMessage());
            }
        }
    }

    private <T> OperationMessageDTO<T> processClientObject(OperationMessageDTO<T> clientObject) {
        String operationCode = clientObject.responseType();

        OperationStrategy<T> strategy = strategyFactory.createStrategy(operationCode);

        return strategy.execute(clientObject, database);
    }
}
