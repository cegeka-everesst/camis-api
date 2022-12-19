package com.cegeka.horizon.camis.synctimesheet;

import com.cegeka.horizon.camis.synctimesheet.domain.ResourceId;
import com.cegeka.horizon.camis.synctimesheet.domain.Workorder;
import com.cegeka.horizon.camis.synctimesheet.service.TimesheetService;
import com.cegeka.horizon.camis.synctimesheet.service.WorkorderAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class SyncTimesheetApp {

    @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private WorkorderAccessService workorderAccessService;

    public static void main(String[] args) {
        new SpringApplicationBuilder(SyncTimesheetApp.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner isAccessible(ApplicationContext ctx) {
        return args -> {
            System.out.println(
                    workorderAccessService.isAccessAllowed(new ResourceId("I098816"),
                            new Workorder("LMAC000.003"),
                            LocalDate.of(2022, 12, 11))

            );
            System.out.println(timesheetService.getTimesheetEntries(new ResourceId("I098816"), LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 12)));
        };
    }

}
