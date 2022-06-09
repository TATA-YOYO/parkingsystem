package com.parkit.parkingsystem;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static Ticket ticket;

    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setupPerTests() {
        //With this some ticket is already in the DB
        dataBasePrepareService.clearDataBaseEntries();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("TEST");
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
        ticket.setDiscount(true);
        ticketDAO.saveTicket(ticket);
    }

    @AfterAll
    private static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void saveTicket() {
        //Act
        boolean result = ticketDAO.saveTicket(ticket);
        //Assert
        assertTrue(result);
    }

    /**
     * this test checks if "ticketDAO.getTicket" method really get a ticket, and verify if it is not null
     */
    @Test
    public void getTicketWithoutDiscountTest() {
        //Act
        Ticket result = ticketDAO.getTicket("1");

        //Assert
        assertNotEquals(null, result);
        assertFalse(result.isDiscount());
    }

    @Test
    public void updateTicket() throws SQLException {
        //Act
        boolean result = ticketDAO.updateTicket(ticket);

        //Assert
        assertTrue(result);
    }

    @Test
    public void getID() {
        //Act
        int result = ticketDAO.getID();
        //Assert
        assertNotEquals(0, result);
    }
}
