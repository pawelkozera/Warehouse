package org.warehouse.OperationStrategies.Employee;

import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class EditEmployeeStrategy implements OperationStrategy<Employee> {
    @Override
    public OperationMessageDTO<Employee> execute(OperationMessageDTO<Employee> clientObject, Database database) {
        Employee employee = clientObject.data();

        if (!database.editEmployee(employee)) {
            return new OperationMessageDTO<>("EDIT_EMPLOYEE_FAILURE", null);
        }

        return new OperationMessageDTO<>("EDIT_EMPLOYEE_SUCCESS", null);
    }
}
