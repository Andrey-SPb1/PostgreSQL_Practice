package jdbc.starter;

import jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {

        long flight_id = 9L;
        String sql_ticket = """ 
                DELETE FROM ticket WHERE flight_id = ?;
                """;
        String sql_flight = """ 
                DELETE FROM flight WHERE id = ?;
                """;

        Connection connection = null;
        PreparedStatement deleteTicketStatement = null;
        PreparedStatement deleteFlightStatement = null;

        try {
            connection = ConnectionManager.open();
            deleteTicketStatement = connection.prepareStatement(sql_ticket);
            deleteFlightStatement = connection.prepareStatement(sql_flight);

            connection.setAutoCommit(false);

            deleteTicketStatement.setLong(1, flight_id);
            deleteFlightStatement.setLong(1, flight_id);

            deleteTicketStatement.executeUpdate();

//            if(true){
//                throw new RuntimeException("Oops");
//            }

            deleteFlightStatement.executeUpdate();

            connection.commit();

        } catch (Exception e) {
            if(connection != null) {
                connection.rollback();
            }
            throw e;

        } finally {
            if(connection != null) {
                connection.close();
            }
            if(deleteTicketStatement != null) {
                deleteTicketStatement.close();
            }
            if(deleteFlightStatement != null) {
                deleteFlightStatement.close();
            }
        }

    }
}
