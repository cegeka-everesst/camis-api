package com.cegeka.horizon.camis.sync.logger.model.result;

public record HoursInfo(double inputHours, double camisHours) {
    private static final double NO_HOURS_INFO = -1.0;


    public static HoursInfo empty() {
        return new HoursInfo(NO_HOURS_INFO, NO_HOURS_INFO);
    }

    public static HoursInfo inputHours(double inputHours) {
        return new HoursInfo(inputHours, NO_HOURS_INFO);
    }

    public static HoursInfo compare(double inputHours, double camisHours) {
        return new HoursInfo(inputHours, camisHours);
    }
}
