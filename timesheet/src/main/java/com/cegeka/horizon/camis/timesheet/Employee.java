package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import org.threeten.extra.LocalDateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Employee {
    private final ResourceId resourceId;
    private final String name;
    private final List<WeeklyTimesheet> weeklyTimeSheets = new ArrayList<>();

    public Employee(ResourceId resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }

    public void addWeeklyTimesheet(WeeklyTimesheet weeklyTimesheetToAdd){
        Optional<WeeklyTimesheet> sameStart = this.weeklyTimeSheets.stream()
                        .filter(weeklyTimesheet -> weeklyTimesheet.startDate().equals(weeklyTimesheetToAdd.startDate())).findFirst();

        if(sameStart.isEmpty()){
            this.weeklyTimeSheets.add(weeklyTimesheetToAdd);
        }else{
            weeklyTimesheetToAdd.lines().forEach(line -> sameStart.get().addLine(line));
        }
    }

    public List<WeeklyTimesheet> weeklyTimesheets() {
        return this.weeklyTimeSheets.stream().sorted(new WeeklyTimesheet.SortByStartDate()).toList();
    }

    public List<WorkOrderStart> getFirstUseOfWorkOrders() {
        return this.weeklyTimeSheets.stream().map(WeeklyTimesheet::getFirstUseOfWorkOrders)
                .collect(Collectors.flatMapping(List::stream, Collectors.toList()));
    }

    public List<WorkOrder> getUsedWorkOrders() {
        return getFirstUseOfWorkOrders().stream().map(WorkOrderStart::workOrder).distinct().collect(Collectors.toList());
    }

    public ResourceId resourceId() {
        return resourceId;
    }

    public String name() {
        return name;
    }

    @Deprecated
    public LocalDateRange getTimesheetDurations() {
        return LocalDateRange.of(
            this.weeklyTimeSheets.stream().min(new WeeklyTimesheet.SortByStartDate()).get().startDate(),
            this.weeklyTimeSheets.stream().max(new WeeklyTimesheet.SortByStartDate()).get().endDate()
                );
    }

    public boolean isToSync(TimeCode timeCode) {
        return !(!resourceId.isExternal() && timeCode.equals(TimeCode.NO_ASSIGNMENT));
    }

    public Optional<WeeklyTimesheet> findWeeklyTimesheetByStartDate(LocalDate startDate) {
        return weeklyTimeSheets.stream().filter(weeklyTimesheet -> weeklyTimesheet.hasStartDate(startDate)).findFirst();
    }

    public Stream<LoggedHoursByDayDetail> loggedHoursDetails() {
        return weeklyTimeSheets.stream().flatMap(WeeklyTimesheet::loggedHoursDetails);
    }

    public boolean hasMinimumHoursLogged(double hours) {
        return weeklyTimeSheets.stream().mapToDouble(WeeklyTimesheet::getTotalHoursLogged).allMatch(sum -> sum >= hours) ;
    }

    public static class MergeEmployees implements java.util.function.BiFunction<Employee, Employee, Employee> {
        @Override
        public Employee apply(Employee employee1, Employee employee2) {
            if(! employee1.resourceId.equals(employee2.resourceId)){
                throw new IllegalArgumentException("Only employees with same resourceId can be merged");
            }
            Employee mergedEmployee = new Employee(employee1.resourceId, employee1.name);
            employee2.weeklyTimeSheets.forEach(
                    mergedEmployee::addWeeklyTimesheet
            );
            employee1.weeklyTimeSheets.forEach(
                    mergedEmployee::addWeeklyTimesheet
            );
            return mergedEmployee;
        }
    }

    public static class MergeEmployeesOperator implements java.util.function.BinaryOperator<Employee> {
        @Override
        public Employee apply(Employee employee1, Employee employee2) {
            return new MergeEmployees().apply(employee1, employee2);
        }
    }

    @Override
    public String toString() {
        return "Employee{" + "resourceId=" + resourceId + "/" + name +
                ", weeklyTimeSheets=" + weeklyTimeSheets.stream().sorted(new WeeklyTimesheet.SortByStartDate()).toList() +
                '}';
    }

    public static class SortByName implements java.util.Comparator<Employee> {
        @Override
        public int compare(Employee o1, Employee o2) {
            return o1.name.compareTo(o2.name);
        }
    }


}
