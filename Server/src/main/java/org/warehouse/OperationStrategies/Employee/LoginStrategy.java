package org.warehouse.OperationStrategies.Employee;

import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class LoginStrategy implements OperationStrategy<Employee> {
    @Override
    public OperationMessageDTO<Employee> execute(OperationMessageDTO<Employee> clientObject, Database database) {
        Employee employee = clientObject.data();
        Employee result = database.login(employee);

        if (result.id() == -1) {
            return new OperationMessageDTO<>("LOGIN_FAILURE", null);
        }

        return new OperationMessageDTO<>("LOGIN_SUCCESS", result);
    }
}
