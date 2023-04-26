package jdbc.starter;

import jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.time.*;
import java.util.*;

public class JdbcRunner2 {

    public static void main(String[] args) throws SQLException {

//        Long value = 2L;
//        List<Long> ticketByFlightId = getTicketByFlightId(value);
//        System.out.println(ticketByFlightId);

//        List<Long> flightsBetween =
//                getFlightsBetween(LocalDate.of(2020,5,12).atStartOfDay(), LocalDateTime.now());
//        System.out.println(flightsBetween);

        try{
            checkMetaData();
        } finally {
            ConnectionManager.closePool();
        }

    }

    private static void checkMetaData() throws SQLException {

        try(Connection connection = ConnectionManager.get()) {
            var metaData = connection.getMetaData();
            var catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                var catalog = catalogs.getString("TABLE_CAT");
                var schemas = metaData.getSchemas();
                while (schemas.next()) {
                    var schema = schemas.getString("TABLE_SCHEM");
                    ResultSet tables = metaData.getTables(catalog, schema, "%", new String[] {"TABLE"});
                    if(schema.equals("public")) {
                        int num = 0;
                        while (tables.next()) {
                            var table = tables.getString("TABLE_NAME");
                            System.out.println(++num + ")" + table);
                            var columns = metaData.getColumns(catalog, schema, table, "%");
                            while (columns.next()) {
                                System.out.println(columns.getString("COLUMN_NAME"));
                            }
                        }
                    }
                }
            }
        }
    }

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) throws SQLException{

        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?;
                """;
        List<Long> result = new ArrayList<>();
        try(Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        }
        return result;
    }

    private static List<Long> getTicketByFlightId(Long flightId) throws SQLException {
        String sql = """
                SELECT id
                FROM ticket
                WHERE flight_id = ?;
                """;
        List<Long> result = new ArrayList<>();

        try(Connection connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1,flightId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
//                result.add(resultSet.getLong("id")); NOT NULL SAFE
                result.add(resultSet.getObject("id", Long.class));
            }
        }
        return result;
    }
}
