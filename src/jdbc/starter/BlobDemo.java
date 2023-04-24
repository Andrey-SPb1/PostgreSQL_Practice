package jdbc.starter;

import jdbc.starter.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobDemo {

    public static void main(String[] args) throws SQLException, IOException {
//        setImage();
        getImage();
    }

    private static void getImage() throws SQLException, IOException{
        int id = 1;
        String sql = """
                SELECT *
                FROM aircraft
                WHERE id = ?;
                """;

        try(var connection = ConnectionManager.open();
        var prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setInt(1,id);
            var resultSet = prepareStatement.executeQuery();
            while(resultSet.next()) {
                var image = resultSet.getBytes("image");
                Files.write(Path.of("resources", "Boeing-2.jpg"), image, StandardOpenOption.CREATE);
            }
        }
    }

    private static void setImage() throws SQLException, IOException {
        String sql = """
                UPDATE aircraft
                SET image = ?
                WHERE id = 1;
                """;

        try(var connection = ConnectionManager.open();
            var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBytes(1,
                    Files.readAllBytes(Path.of("resources", "Boeing.jpg")));
            preparedStatement.executeUpdate();
        }
    }
}
