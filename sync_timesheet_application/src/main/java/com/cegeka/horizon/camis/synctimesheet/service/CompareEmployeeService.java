package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.synctimesheet.service.command.SyncCommand;
import com.cegeka.horizon.camis.timesheet.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompareEmployeeService {
    public List<SyncCommand> compare(Employee inputEmployee, Employee camisEmployee) {
        return new ArrayList<>();
    }
}
