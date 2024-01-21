package org.warehouse.OperationStrategyFactory;

import org.warehouse.OperationStrategies.OperationStrategy;

public interface OperationStrategyFactory {
    <T> OperationStrategy<T> createStrategy(String operationCode);
}