package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.WorkOrderStart;
import com.cegeka.horizon.camis.workorder.WorkOrderAccess;
import com.cegeka.horizon.camis.workorder.WorkorderAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CheckWorkOrderService {

    @Autowired
    private WorkorderAccessService workorderAccessService;
    private static final Logger logger = LoggerFactory.getLogger("CheckWorkOrdersResultLogger");


    public void check(List<Employee> employees) {
        employees.forEach(
                employee -> {
                    employee.getFirstUseOfWorkOrders().stream()
                            .collect(groupingBy(workOrderStart -> workOrderStart.workOrder())).forEach(
                                    (workOrder, workOrderStarts) -> {
                                        LocalDate start = workOrderStarts.stream().sorted(new WorkOrderStart.byStart()).findFirst().get().start();
                                        WorkOrderAccess accessAllowed = workorderAccessService.isAccessAllowed(employee.resourceId(), workOrder,
                                                start);
                                        logger.info("Checking access for " + employee.name() + " for work order ", workOrder.value());
                                        if(accessAllowed.access() == false){
                                            logger.info("Access for " + employee.name() + " for work order ", workOrder.value() + " on date " + start + "is NOT ALLOWED");
                                        }
                                    }

                            );
                }
                );
    }

}
