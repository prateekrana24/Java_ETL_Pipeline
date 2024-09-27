import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import io.github.cdimascio.dotenv.Dotenv;

// This class will fetch the stock data from the Alpha Vantage API
public class AlphaVantageService implements DataFetcher {
    // Load environment variables
    private final Dotenv dotenv = Dotenv.load();
    private final String API_KEY = dotenv.get("API_KEY");
    private final String API_URL =
            "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY" +
                    "&symbol=TSLA&interval=30min&outputsize=full&apikey=" + API_KEY;

    @Override
    // Method to fetch stock data from Alpha Vantage
    public String fetchStockData() {
        StringBuilder content = new StringBuilder();
        try {
            // Create a URL object
            URL url = new URL(API_URL);

            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method (GET, POST, etc.)
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    // Use streams to read lines from the response
                    in.lines().forEach(content::append);
                }
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("AlphaVantage URL connection error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return content.toString();
    }


}
