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

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = (outHour - inHour);

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (duration <= Time.HALF_HOUR) {
                    break;
                } else if (ticket.isDiscount() == true) {
                    ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR) * Fare.DISCOUNT_RATE);
                    break;
                } else {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
            }
            case BIKE: {
                if (duration <= Time.HALF_HOUR) {
                    break;
                } else if (ticket.isDiscount() == true) {
                    ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR) * Fare.DISCOUNT_RATE);
                    break;
                } else {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}