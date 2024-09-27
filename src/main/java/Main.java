import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Use interfaces instead of specific classes
        DataFetcher dataFetcher = new AlphaVantageService();
        DataParser dataParser = new StockDataParser();
        DatabaseInserter databaseInserter = new DatabaseInsertion();

        // Fetch stock data
        String stockData = dataFetcher.fetchStockData();
        System.out.println(stockData);

        // Parse stock data
        List<List<String>> stockDataCleaned = dataParser.stockParser(stockData, String.class);
        System.out.println(stockDataCleaned);

        // Insert parsed data into the database
        databaseInserter.insertIntoDatabase(stockDataCleaned);
    }
}