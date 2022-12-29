package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.testbuilder.TestWorkOrders;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

class WeeklyTimesheetTest {

    @Test
    public void givenTimesheet_whenStartDate_thenFindFirstMondayOfLoggedHours(){
        TimesheetLine line1 = aTimesheetLine().build();
        line1.addLoggedHours(aLoggedHours().withDay(of(2022,12,8)).build());

        TimesheetLine line2 = aTimesheetLine().build();
        line2.addLoggedHours(aLoggedHours().withDay(of(2022,12,6)).build());

        WeeklyTimesheet weeklyTimesheet =
                        aWeeklyTimesheet()
                                .withLine(line1, line2)
                        .build();


        assertThat(weeklyTimesheet.startDate()).isEqualTo(of(2022,12,5));
    }

    @Test
    public void givenTimesheet_whenEndDate_thenFindLastSundayOfLoggedHours(){
        TimesheetLine line1 = aTimesheetLine().build();
        line1.addLoggedHours(aLoggedHours().withDay(of(2022,12,17)).build());

        TimesheetLine line2 = aTimesheetLine().build();
        line2.addLoggedHours(aLoggedHours().withDay(of(2022,12,15)).build());

        WeeklyTimesheet weeklyTimesheet =
                aWeeklyTimesheet()
                        .withLine(line1, line2)
                        .build();


        assertThat(weeklyTimesheet.endDate()).isEqualTo(of(2022,12,18));
    }

    @Test
    public void givenTimesheet_whenEndDate_thenFindLastSundayOfLoggedHoursIfSundayIncluded(){
        TimesheetLine line1 = aTimesheetLine().build();
        line1.addLoggedHours(aLoggedHours().withDay(of(2022,12,13)).build());

        TimesheetLine line2 = aTimesheetLine().build();
        line2.addLoggedHours(aLoggedHours().withDay(of(2022,12,18)).build());

        WeeklyTimesheet weeklyTimesheet =
                aWeeklyTimesheet()
                        .withLine(line1, line2)
                        .build();


        assertThat(weeklyTimesheet.endDate()).isEqualTo(of(2022,12,18));
    }

    @Test
    public void givenTimesheetLine_whenAddLineAndDifferentWorkOrder_thenAddLine(){
        TimesheetLine existingLine = aTimesheetLine().withWorkOrder(new WorkOrder("LMAC003.001")).build();
        existingLine.addLoggedHours(aLoggedHours().withDay(5).build());
        WeeklyTimesheet weeklyTimesheet =
                aWeeklyTimesheet()
                        .withLine(existingLine)
                        .build();

        TimesheetLine newLine = aTimesheetLine().withWorkOrder(new WorkOrder("LMAC005.001")).build();
        newLine.addLoggedHours(aLoggedHours().withDay(5).build());

        weeklyTimesheet.addLine(newLine);

        assertThat(weeklyTimesheet.lines()).hasSize(2);
    }

    @Test
    public void givenTimesheetLine_whenAddLineAndSameWorkOrder_thenMergeLine(){
        WorkOrder sameWorkOrder = TestWorkOrders.WORK_ORDER_1;
        LocalDate date = of(2022, 12, 5);
        TimesheetLine existingLine = aTimesheetLine().withWorkOrder(sameWorkOrder).build();
        existingLine.addLoggedHours(aLoggedHours(3).withDay(date).build());
        WeeklyTimesheet weeklyTimesheet =
                aWeeklyTimesheet()
                        .withLine(existingLine)
                        .build();

        TimesheetLine newLine = aTimesheetLine().withWorkOrder(sameWorkOrder).build();
        newLine.addLoggedHours(aLoggedHours(5).withDay(date).build());

        weeklyTimesheet.addLine(newLine);

        assertThat(weeklyTimesheet.lines()).hasSize(1);
        assertThat(weeklyTimesheet.lines().get(0).loggedHours().get(0).date()).isEqualTo(date);
        assertThat(weeklyTimesheet.lines().get(0).loggedHours().get(0).hours()).isEqualTo(8);
    }

    @Test
    public void givenTimesheetLine_whenAddLineAndSameWorkOrderYetDifferentIdentifier_thenNotMergeLine(){
        WorkOrder sameWorkOrder = TestWorkOrders.WORK_ORDER_1;
        LocalDate date = of(2022, 12, 5);
        TimesheetLine existingLine = aTimesheetLine().withWorkOrder(sameWorkOrder).build();
        existingLine.addLoggedHours(aLoggedHours(3).withDay(date).build());
        WeeklyTimesheet weeklyTimesheet =
                aWeeklyTimesheet()
                        .withLine(existingLine)
                        .build();

        TimesheetLine newLine = aTimesheetLine()
                            .withWorkOrder(sameWorkOrder)
                            .withIdentifier("44")
                            .build();
        newLine.addLoggedHours(aLoggedHours(5).withDay(date).build());

        weeklyTimesheet.addLine(newLine);

        assertThat(weeklyTimesheet.lines()).hasSize(2);
        assertThat(weeklyTimesheet.lines().get(0).loggedHours().get(0).date()).isEqualTo(date);
        assertThat(weeklyTimesheet.lines().get(1).loggedHours().get(0).date()).isEqualTo(date);
    }

}