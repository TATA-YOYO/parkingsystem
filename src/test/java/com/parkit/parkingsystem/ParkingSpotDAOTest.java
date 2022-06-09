package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static DataBaseTestConfig dataBaseTestConfig;

    @BeforeAll
    private static void setUp() {
        dataBaseTestConfig = new DataBaseTestConfig();
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setupPerTests() {
        dataBasePrepareService.clearDataBaseEntries();
    }//To clean up database

    @AfterAll
    private static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }//To clean up database

    /**
     * this test verifies that the "getNextAvailableSlot" method returns the number of the next slot
     */
    @Test
    public void getNextAvailableSlotForCARTest() throws SQLException {
        //Arrange
        //Act
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        //Assert
        assertEquals(1, result);
    }

    @Test
    public void getNextAvailableSlotForBikeTest() throws SQLException {
        //Arrange
        //Act
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

        //Assert
        assertEquals(4, result);
    }

    /**
     * this test checks if the update of the database is done well
     */
    @Test
    public void updateParkingForCarTest() throws SQLException {
        //Arrange
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        //Act
        boolean result = parkingSpotDAO.updateParking(parkingSpot);

        //Assert
        assertTrue(result);
    }

    @Test
    public void updateParkingForBikeTest() throws SQLException {
        //Arrange
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        //Act
        boolean result = parkingSpotDAO.updateParking(parkingSpot);

        //Assert
        assertTrue(result);
    }
}
