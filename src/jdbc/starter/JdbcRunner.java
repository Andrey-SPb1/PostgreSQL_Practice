package jdbc.starter;

import java.sql.DriverManager;

public class JdbcRunner {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "Elephant27";
        DriverManager.getConnection(url, user, password);

    }
}
