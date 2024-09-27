import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

public class DatabaseInsertion implements DatabaseInserter {
    // Load environment variables
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");
    private static final String DB_PORT = dotenv.get("DB_PORT");
    private static final String DB_NAME = dotenv.get("DB_NAME");
    private static final String DB_TABLE_NAME = dotenv.get("DB_TABLE_NAME");
    private static final String CONNECTION_STATUS =
            "jdbc:mysql://" + DB_USER + ":" + DB_PASSWORD + "@localhost:" + DB_PORT + "/" + DB_NAME;

    @Override
    public <T> void insertIntoDatabase(List<List<T>> stockDataLists) {
        // SQL insert query for the given table
        String sqlInsert = "INSERT INTO " + DB_TABLE_NAME + " (Datetime, Open, High, Low, Close, Volume) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(CONNECTION_STATUS);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {

            // Get each list from the stockDataLists
            List<T> timestamps = stockDataLists.get(0);
            List<T> opens = stockDataLists.get(1);
            List<T> highs = stockDataLists.get(2);
            List<T> lows = stockDataLists.get(3);
            List<T> closes = stockDataLists.get(4);
            List<T> volumes = stockDataLists.get(5);

            // Iterate through the lists and add to the database using streams
            IntStream.range(0, timestamps.size()).forEach(i -> {
                try {
                    preparedStatement.setObject(1, timestamps.get(i));  // Datetime
                    preparedStatement.setObject(2, opens.get(i));       // Open
                    preparedStatement.setObject(3, highs.get(i));       // High
                    preparedStatement.setObject(4, lows.get(i));        // Low
                    preparedStatement.setObject(5, closes.get(i));      // Close
                    preparedStatement.setObject(6, volumes.get(i));     // Volume

                    // Execute the insert
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Error executing insert for index " + i + ": " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Data inserted successfully!");

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}