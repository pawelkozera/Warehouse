package org.warehouse.OperationStrategies.Employee;

import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.OperationMessageDTO;
import org.warehouse.Database;
import org.warehouse.OperationStrategies.OperationStrategy;

import java.util.List;

public class ListLoginsAndNamesStrategy implements OperationStrategy<List<Employee>> {

    @Override
    public OperationMessageDTO<List<Employee>> execute(OperationMessageDTO<List<Employee>> clientObject, Database database) {
        List<Employee> listOfLoginsAndNames = database.listLoginsAndNames();

        if (listOfLoginsAndNames.isEmpty()) {
            return new OperationMessageDTO<>("LIST_LOGINS_NAMES_FAILURE", listOfLoginsAndNames);
        }

        return new OperationMessageDTO<>("LIST_LOGINS_NAMES_SUCCESS", listOfLoginsAndNames);
    }
}
