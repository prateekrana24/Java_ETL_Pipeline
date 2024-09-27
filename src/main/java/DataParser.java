import java.util.List;

public interface DataParser {
    <T> List<List<T>> stockParser(String stockData, Class<T> type);
}
