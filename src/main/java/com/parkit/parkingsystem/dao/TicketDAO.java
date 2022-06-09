package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public boolean saveTicket(Ticket ticket) throws SQLException {
        Connection con = null;
        boolean isSaved = false;
        PreparedStatement ps=null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            // 1 = PARKING_NUMBER, 2 = VEHICLE_REG_NUMBER,3 = PRICE, 4 = IN_TIME)
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.execute();
            dataBaseConfig.closePreparedStatement(ps);
            isSaved = true;
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return isSaved;
    }

    public Ticket getTicket(String IDNumber)throws SQLException {
        Connection con = null;
        Ticket ticket = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_TICKET);
            ps.setString(1, IDNumber);
            rs = ps.executeQuery(); // 1 = PARKING_NUMBER, 2 = ID, 3 = PRICE, 4 = VEHICLE_REG_NUMBER, 5 = IN_TIME, 6 = OUT_TIME, 7 = TYPE
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(7)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(rs.getString(4));
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(5));
                if (rs.getTimestamp(6) != null) {
                    ticket.setOutTime(rs.getTimestamp(6));
                }
                boolean discount = getDiscount(ticket.getVehicleRegNumber());
                ticket.setDiscount(discount);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    private boolean getDiscount(String vehicleRegNumber)throws SQLException {
        boolean discount = false;
        Connection con = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_REGULAR_CUSTOMER);
            ps.setString(1, vehicleRegNumber);
             rs = ps.executeQuery();
            if (rs.next()) {
                discount = (rs.getInt(1) > 1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
            return discount;
        }
    }

    public int getID()throws SQLException {
        int id = 0;
        Connection con = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            con = dataBaseConfig.getConnection();
             ps = con.prepareStatement(DBConstants.GET_ID);
             rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
            return id;

        }
    }


    public boolean updateTicket(Ticket ticket) throws SQLException {
        Connection con = null;
        PreparedStatement ps=null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());
            ps.execute();
            ps.close();
            dataBaseConfig.closePreparedStatement(ps);
            return true;
        } catch (Exception ex) {
            logger.error("Error saving ticket info", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);

        }
        return false;
    }
}
