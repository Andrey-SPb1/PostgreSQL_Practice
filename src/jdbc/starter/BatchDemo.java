package jdbc.starter;

import jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchDemo {

    public static void main(String[] args) throws SQLException {

        long flight_id = 8L;
        String sql_ticket = "DELETE FROM ticket WHERE flight_id = " + flight_id;
        String sql_flight = "DELETE FROM flight WHERE id = " + flight_id;

        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionManager.open();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            statement.addBatch(sql_ticket);
            statement.addBatch(sql_flight);

            statement.executeBatch();

            connection.commit();
        } catch (Exception e) {
            if(connection != null) {
                connection.rollback();
            }
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(statement != null) {
                statement.close();
            }
        }

    }
}
