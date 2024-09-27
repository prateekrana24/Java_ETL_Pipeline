import java.util.List;

public interface DatabaseInserter {
    <T> void insertIntoDatabase(List<List<T>> stockDataLists);
}
