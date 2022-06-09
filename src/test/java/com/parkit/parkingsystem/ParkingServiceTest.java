package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;//to automate testing
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;//to simulate database access
    @Mock
    private static TicketDAO ticketDAO;//to simulate database access

    @BeforeEach
    private void setUpPerTest() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    /**
     * This test checks if "processIncomingVehicle" method called right all methods necessary
     *
     */
    @Test
    public void processIncomingTest() throws Exception {
        //Arrange
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);


        //Act
        parkingService.processIncomingVehicle();

        //Assert
        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));

    }
    /**
     * This test checks if "processExitingVehicle" method called right all methods necessary
     */
    @Test
    public void processExitingVehicleTest() throws SQLException {
        //Arrange
        try {
            when(inputReaderUtil.readTicketIDNumberRegistration()).thenReturn("1");
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }

        //Act
        parkingService.processExitingVehicle();

        //Assert
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());

    }
    /**
     * This test checks if "parkingService" called right "getNextParkingNumberIfAvailable" method
     * and verify if the return value is not null
     */
    @Test
    public void getNextParkingNumberIfAvailableTest() throws SQLException {
        //Arrange
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        //Act
        ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();

        //Assert
        assertNotEquals(null, result);
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

}
