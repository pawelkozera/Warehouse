package org.warehouse.OperationStrategies.Employee;

import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

public class GetEmployeeByLoginStrategy implements OperationStrategy<Employee> {
    @Override
    public OperationMessageDTO<Employee> execute(OperationMessageDTO<Employee> clientObject, Database database) {
        Employee employee = clientObject.data();

        Employee employeeGot = database.getEmployeeByLogin(employee.login());

        if (employeeGot != null) {
            return new OperationMessageDTO<>("GET_EMPLOYEE_BY_LOGIN_SUCCESS", employeeGot);
        }

        return new OperationMessageDTO<>("GET_EMPLOYEE_BY_LOGIN_FAILURE", null);
    }
}
