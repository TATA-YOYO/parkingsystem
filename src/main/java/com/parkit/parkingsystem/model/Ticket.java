package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean discount = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        ParkingSpot parkingSpotTemp= new ParkingSpot(parkingSpot.getId(), parkingSpot.getParkingType(),parkingSpot.isAvailable());
        return parkingSpotTemp;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        ParkingSpot parkingSpotTemp= new ParkingSpot(parkingSpot.getId(), parkingSpot.getParkingType(),parkingSpot.isAvailable());
        this.parkingSpot = parkingSpotTemp;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        Date inTimeTemp = new Date(inTime.getTime());
        return inTimeTemp;
    }

    public void setInTime(Date inTime) {
        Date inTimeTemp = new Date(inTime.getTime());
        this.inTime = inTimeTemp;
    }

    public Date getOutTime() {
        Date outTimeTemp = new Date(outTime.getTime());
        return outTimeTemp;
    }

    public void setOutTime(Date outTime) {
        Date outTimeTemp = new Date(outTime.getTime());
        this.outTime = outTimeTemp;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }
}
