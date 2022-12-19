package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.api.timesheet.EntryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TimesheetService {

    private final WebClient webClient;

    @Autowired
    public TimesheetService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void sync(){
    }
}
