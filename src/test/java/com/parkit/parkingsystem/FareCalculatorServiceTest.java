package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    /**
     * This test checks if the price for one hour for a car is right.<br>
     * in "Arrange"I set up a ticket with one hour less in entry time
     */
    @Test
    public void calculateFareCar(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }
    /**
     * This test checks if the price for one hour for a bike is right.<br>
     * in "Arrange"I set up a ticket with one hour less in entry time
     */
    @Test
    public void calculateFareBike(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }
    /**
     * This test checks if an exception is throws when the type of vehicle is unknow.<br>
     * in "Arrange"I set up a ticket with unknow type in parking spot.
     */
    @Test
    public void calculateFareUnknowType(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Assert
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * This test checks if an exception is throws when entry time is in a future
     */
    @Test
    public void calculateFareBikeWithFutureInTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * This test checks if the fare for a bike during 45min is right.
     */
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    /**
     * This test checks if the fare for a car during 45min is right.
     */
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals( Math.round((0.75 * Fare.CAR_RATE_PER_HOUR)*100.0)/100.0 , ticket.getPrice());
    }

    /**
     * This test checks if fare is right when a duration is mor than a day.
     */
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    /**
     * this test checks for car if the parking rate is free when it has a duration of less than half an hour.
     */
    @Test
    public void calculateFareCarWithLessThanHalfHour(){

        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (30 * 60 * 1000) ); //30 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals( (0 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    /**
     * this test checks for Bike if the parking rate is free when it has a duration of less than half an hour.
     */
    @Test
    public void calculateFareBikeWithLessThanHalfHour(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (30 * 60 * 1000) ); //30 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals( (0 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    /**
     * this test checks for a car if a discount is really calculate if is a regular customer
     */
    @Test
    public void calculateFareCarForRegularCustomers(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setDiscount(true);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals(Math.round ((((24 * Fare.CAR_RATE_PER_HOUR))*0.95)*100.0)/100.0 , ticket.getPrice());
    }

    /**
     * this test checks for a bike if a discount is really calculate if is a regular customer
     */
    @Test
    public void calculateFareBikeForRegularCustomers (){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setDiscount(true);
        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals( Math.round((((24 * Fare.BIKE_RATE_PER_HOUR))*0.95)*100.0)/100.0 , ticket.getPrice());
    }
}
