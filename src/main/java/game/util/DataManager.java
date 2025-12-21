package game.util;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private final List<String> scoreHistory = new ArrayList<>();

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }
    public void addScoreRecord(int level, int score) {
        scoreHistory.add("Level " + (level + 1) + ": " + score);
    }
    public List<String> getHistory() { return new ArrayList<>(scoreHistory); }
}