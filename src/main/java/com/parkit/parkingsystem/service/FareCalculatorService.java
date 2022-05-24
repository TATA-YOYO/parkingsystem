package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.Time;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = (ticket.getInTime().getTime())/Time.millisecondPerHour;
        double outHour = (ticket.getOutTime().getTime())/Time.millisecondPerHour;

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = (outHour - inHour);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (duration<=0.5){
                    break;
                }
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                if (duration<=0.5){
                    break;
                }
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}