package com.cegeka.horizon.camis.synctimesheet;

import com.cegeka.horizon.camis.api.timesheet.EntryList;
import com.cegeka.horizon.camis.api.workorder.AccessAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TimesheetService {

    private final WebClient webClient;

    @Autowired
    public TimesheetService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8080")
                        .build();
    }

    public void sync(){
        webClient.get();
        new EntryList();
        new AccessAllowed();
    }
}
