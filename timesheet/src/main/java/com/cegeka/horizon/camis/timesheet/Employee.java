package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
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
    private final EmployeeIdentification id;
    private final List<WeeklyTimesheet> weeklyTimeSheets = new ArrayList<>();
    public Employee(EmployeeIdentification id) {
        this.id = id;
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
        return id.resourceId();
    }

    public String name() {
        return id.name();
    }

    @Deprecated
    public LocalDateRange getTimesheetDurations() {
        return LocalDateRange.of(
            this.weeklyTimeSheets.stream().min(new WeeklyTimesheet.SortByStartDate()).get().startDate(),
            this.weeklyTimeSheets.stream().max(new WeeklyTimesheet.SortByStartDate()).get().endDate()
                );
    }

    public boolean isToSync(TimeCode timeCode) {
        return !(!id.resourceId().isExternal() && timeCode.equals(TimeCode.NO_ASSIGNMENT));
    }

    public Optional<WeeklyTimesheet> findWeeklyTimesheetByStartDate(LocalDate startDate) {
        return weeklyTimeSheets.stream().filter(weeklyTimesheet -> weeklyTimesheet.hasStartDate(startDate)).findFirst();
    }

    public Stream<LoggedHoursByDayDetail> loggedHoursDetails() {
        return weeklyTimeSheets.stream().flatMap(WeeklyTimesheet::loggedHoursDetails);
    }

    public boolean hasMinimumDailyHoursLogged(LocalDate date, double minimalDailyHours) {
        return dailyHoursLogged(date) - minimalDailyHours >= 0;
    }

    public LocalDateRange loggedHoursRange() {
        LocalDate start = loggedHoursDetails()
                .map(loggedHoursByDayDetail -> loggedHoursByDayDetail.loggedHoursByDay().date())
                .min(LocalDate::compareTo).get();

        LocalDate end = loggedHoursDetails()
                .map(loggedHoursByDayDetail -> loggedHoursByDayDetail.loggedHoursByDay().date())
                .max(LocalDate::compareTo).get();

        return LocalDateRange.of(start, end.plusDays(1));
    }

    public EmployeeIdentification id() {
        return id;
    }

    public double dailyHoursLogged(LocalDate date) {
        return loggedHoursDetails().filter(
                        loggedHoursByDayDetail -> loggedHoursByDayDetail.loggedHoursByDay().date().equals(date)
                ).mapToDouble(loggedHoursByDayDetail -> loggedHoursByDayDetail.loggedHoursByDay().hours())
                .sum();
    }

    public static class MergeEmployees implements java.util.function.BiFunction<Employee, Employee, Employee> {
        @Override
        public Employee apply(Employee employee1, Employee employee2) {
            if(! employee1.id.resourceId().equals(employee2.id.resourceId())){
                throw new IllegalArgumentException("Only employees with same resourceId can be merged");
            }
            Employee mergedEmployee = new Employee(employee1.id);
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
        return "Employee{" + "resourceId=" + id.resourceId() + "/" + id.name() +
                ", weeklyTimeSheets=" + weeklyTimeSheets.stream().sorted(new WeeklyTimesheet.SortByStartDate()).toList() +
                '}';
    }

    public static class SortByName implements java.util.Comparator<Employee> {
        @Override
        public int compare(Employee o1, Employee o2) {
            return o1.id.name().compareTo(o2.id.name());
        }
    }
}
