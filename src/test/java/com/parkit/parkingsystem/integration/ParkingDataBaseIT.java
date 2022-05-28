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
        dataBasePrepareService.clearDataBaseEntries();
        parkingSpot= new ParkingSpot(1,ParkingType.CAR,false);
        parkingSpotDAO.updateParking(parkingSpot);
        ticket= new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("TEST");
        ticket.setInTime(new Date());
        ticketDAO.saveTicket(ticket);
    }

    @AfterAll
    private static void tearDown() {
    }


    @Test
    public void testParkingACar() throws Exception {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //WHEN
        parkingService.processIncomingVehicle();
        Ticket ticket =ticketDAO.getTicket("2");
        String result = ticket.getVehicleRegNumber();

        //THEN
        assertEquals("ABCDEF", result);
        assertEquals(3, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }

    @Test
    public void testParkingLotExit() throws Exception {
        //GIVEN
        when(inputReaderUtil.readIDNumberRegistration()).thenReturn("1");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //WHEN
        parkingService.processExitingVehicle();
        Ticket ticket = ticketDAO.getTicket("1");
        double result = ticket.getOutTime().getTime();

        //THEN
      assertNotEquals(0,result);
    }

}
