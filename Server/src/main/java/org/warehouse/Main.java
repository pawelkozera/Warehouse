package org.warehouse;

import org.warehouse.OperationStrategyFactory.ConcreteOperationStrategyFactory;
import org.warehouse.OperationStrategyFactory.OperationStrategyFactory;

public class Main {
    public static void main(String[] args) {
        OperationStrategyFactory strategyFactory = new ConcreteOperationStrategyFactory();
        Server server = new Server(strategyFactory);

        server.start();
    }
}