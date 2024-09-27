import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class StockDataParser implements DataParser {
    @Override
    public <T> List<List<T>> stockParser(String stockData, Class<T> type) {
        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Initialize the list of lists
        List<T> timestamps = new ArrayList<>();
        List<T> opens = new ArrayList<>();
        List<T> highs = new ArrayList<>();
        List<T> lows = new ArrayList<>();
        List<T> closes = new ArrayList<>();
        List<T> volumes = new ArrayList<>();

        // List to store all lists
        List<List<T>> stockDataLists = new ArrayList<>();
        stockDataLists.add(timestamps);
        stockDataLists.add(opens);
        stockDataLists.add(highs);
        stockDataLists.add(lows);
        stockDataLists.add(closes);
        stockDataLists.add(volumes);

        // Parse the JSON
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(stockData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Navigate to the "Time Series (30min)" node
        JsonNode timeSeriesNode = rootNode.path("Time Series (30min)");

        // Iterate over the time series entries
        Iterator<Map.Entry<String, JsonNode>> fields = timeSeriesNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            T timestamp = type.cast(entry.getKey());
            JsonNode stockDataValue = entry.getValue();

            // Extract the stock data for each timestamp
            T open = type.cast(stockDataValue.get("1. open").asText());
            T high = type.cast(stockDataValue.get("2. high").asText());
            T low = type.cast(stockDataValue.get("3. low").asText());
            T close = type.cast(stockDataValue.get("4. close").asText());
            T volume = type.cast(stockDataValue.get("5. volume").asText());

            // Add each value to its respective list
            timestamps.add(timestamp);
            opens.add(open);
            highs.add(high);
            lows.add(low);
            closes.add(close);
            volumes.add(volume);
        }

        // Return the list of lists
        return stockDataLists;
    }
}