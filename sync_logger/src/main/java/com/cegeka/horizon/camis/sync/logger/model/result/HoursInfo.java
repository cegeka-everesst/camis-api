package com.cegeka.horizon.camis.sync.logger.model.result;

public record HoursInfo(double inputHours, double camisHours) {
    public static HoursInfo empty() {
        return new HoursInfo(0.0, 0.0);
    }

    public static HoursInfo inputHours(double inputHours) {
        return new HoursInfo(0.0, inputHours);
    }

    public static HoursInfo compare(double inputHours, double camisHours) {
        return new HoursInfo(inputHours, camisHours);
    }
}
