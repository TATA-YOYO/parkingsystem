package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static com.parkit.parkingsystem.constants.Time.MILLISECOND_PER_HOUR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private final static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpot parkingSpot;
    private static Ticket ticket;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() {
        //some car is already in the parking lot
        dataBasePrepareService.clearDataBaseEntries();
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("Car");
        double oneHourLess = (new Date().getTime()) - MILLISECOND_PER_HOUR;
        ticket.setInTime(new Date((long) oneHourLess));
        ticketDAO.saveTicket(ticket);
    }

    @AfterAll
    private static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * this test verify a ticket is saved in the database when the "processIncomingVehicle" method is called for a car.
     */
    @Test
    public void testParkingACar() throws Exception {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //WHEN
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("2");
        String result = ticket.getVehicleRegNumber();

        //THEN
        assertEquals("ABCDEF", result);
        assertEquals(3, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    /**
     * this test verify a ticket is saved in the database when the "processIncomingVehicle" method is called for a Bike
     */
    @Test
    public void testParkingABike() throws Exception {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //WHEN
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("2");
        String result = ticket.getVehicleRegNumber();

        //THEN
        assertEquals("ABCDEF", result);
        assertEquals(5, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    /**
     * this test verify for a car if the "processExitingVehicle" method
     * saves the time of release and the price in the database.
     */
    @Test
    public void testParkingLotExitForCar() throws Exception {
        //GIVEN
        when(inputReaderUtil.readTicketIDNumberRegistration()).thenReturn("1");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //WHEN
        parkingService.processExitingVehicle();
        Ticket ticket = ticketDAO.getTicket("1");
        double result = ticket.getOutTime().getTime();

        //THEN
        assertNotEquals(0, result);
        assertNotEquals(0.0, ticket.getPrice());
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

    /**
     * this test verify for a Bike if the "processExitingVehicle" method
     * saves the time of release and the price in the database.
     */
    @Test
    public void testParkingLotExitForBike() throws Exception {
        //GIVEN
        when(inputReaderUtil.readTicketIDNumberRegistration()).thenReturn("2");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        parkingSpotDAO.updateParking(parkingSpot);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("Bike");
        double oneHourLess = (new Date().getTime()) - MILLISECOND_PER_HOUR;
        ticket.setInTime(new Date((long) oneHourLess));
        ticketDAO.saveTicket(ticket);

        //WHEN
        parkingService.processExitingVehicle();
        Ticket ticket = ticketDAO.getTicket("2");
        double result = ticket.getOutTime().getTime();

        //THEN
        assertNotEquals(0, result);
        assertNotEquals(0.0, ticket.getPrice());
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

    /**
     *
     */
    @Test
    public void TestCalculatePriceForRegularCustomer() throws Exception {
        //GIVEN
        when(inputReaderUtil.readTicketIDNumberRegistration()).thenReturn("2");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        parkingSpotDAO.updateParking(parkingSpot);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("Bike");
        double oneHourLess = (new Date().getTime()) - MILLISECOND_PER_HOUR;
        ticket.setInTime(new Date((long) oneHourLess));
        ticketDAO.saveTicket(ticket);
        parkingService.processExitingVehicle();
        Ticket ticketTemp = ticketDAO.getTicket("2");
        double priceWithoutDiscount = ticketTemp.getPrice();
        ticketDAO.saveTicket(ticket);

        //WHEN
        parkingService.processExitingVehicle();
        ticketTemp = ticketDAO.getTicket("2");
        double result = ticketTemp.getPrice();

        //THEN
        assertEquals((0.95 * priceWithoutDiscount), result);

    }
}
