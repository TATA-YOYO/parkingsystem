package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.Time;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = (ticket.getInTime().getTime()) / Time.MILLISECOND_PER_HOUR;
        double outHour = (ticket.getOutTime().getTime()) / Time.MILLISECOND_PER_HOUR;

        double duration = (outHour - inHour);

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (duration <= Time.HALF_HOUR) {
                    break;
                } else if (ticket.isDiscount()) {
                    double fare = ((duration * Fare.CAR_RATE_PER_HOUR) * Fare.DISCOUNT_RATE);
                    double fareRounded = Math.round(fare * 100.0) / 100.0;
                    ticket.setPrice(fareRounded);
                    break;
                } else {
                    double fare = duration * Fare.CAR_RATE_PER_HOUR;
                    double fareRounded = Math.round(fare * 100.0) / 100.0;
                    ticket.setPrice(fareRounded);
                    break;
                }
            }
            case BIKE: {
                if (duration <= Time.HALF_HOUR) {
                    break;
                } else if (ticket.isDiscount()) {
                    double fare = ((duration * Fare.BIKE_RATE_PER_HOUR) * Fare.DISCOUNT_RATE);
                    double fareRounded = Math.round(fare * 100.0) / 100.0;
                    ticket.setPrice(fareRounded);
                    break;
                } else {
                    double fare = duration * Fare.BIKE_RATE_PER_HOUR;
                    double fareRounded = Math.round(fare * 100.0) / 100.0;
                    ticket.setPrice(fareRounded);
                    break;
                }
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}