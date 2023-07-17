package com.cegeka.horizon.camis.sync_timesheet.csv;

public record SlackEmployee(String tempoUserName,
                            boolean cgk,
                            boolean dev,
                            String id,
                            String team,
                            String camisUserName,
                            String camisId,
                            String slackUserName) {
}
