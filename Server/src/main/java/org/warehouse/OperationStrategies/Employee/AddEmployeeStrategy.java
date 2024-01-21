package org.warehouse.OperationStrategies.Employee;

import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class AddEmployeeStrategy implements OperationStrategy<Employee> {
    @Override
    public OperationMessageDTO<Employee> execute(OperationMessageDTO<Employee> clientObject, Database database) {
        Employee employee = clientObject.data();

        if (!database.addEmployee(employee)) {
            return new OperationMessageDTO<>("ADD_EMPLOYEE_FAILURE", null);
        }

        return new OperationMessageDTO<>("ADD_EMPLOYEE_SUCCESS", null);
    }
}