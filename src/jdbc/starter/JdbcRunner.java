package jdbc.starter;

import jdbc.starter.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {

        String sql = """
                SELECT *
                FROM ticket;
                """;

        try(var connection = ConnectionManager.open();
            var statement = connection.createStatement()) {
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());
            var execute = statement.execute(sql);
            System.out.println(execute);
            System.out.println(statement.getUpdateCount());

            var resultSet = statement.executeQuery(sql);
            // Закроется самостоятельно после закрытия statement
            while(resultSet.next()) {
                System.out.println(resultSet.getLong("id"));
                System.out.println(resultSet.getString("passenger_no"));
                System.out.println(resultSet.getBigDecimal("cost"));
                System.out.println("-------");
            }
        }

    }
}
