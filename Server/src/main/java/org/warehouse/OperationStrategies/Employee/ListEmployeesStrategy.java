package org.warehouse.OperationStrategies.Employee;

import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.util.List;

public class ListEmployeesStrategy implements OperationStrategy<List<Employee>> {
    @Override
    public OperationMessageDTO<List<Employee>> execute(OperationMessageDTO<List<Employee>> clientObject, Database database) {
        List<Employee> listOfEmployees = database.listEmployees();

        if (listOfEmployees.isEmpty()) {
            return new OperationMessageDTO<>("LIST_EMPLOYEES_FAILURE", null);
        }

        return new OperationMessageDTO<>("LIST_EMPLOYEES_SUCCESS", listOfEmployees);
    }
}
