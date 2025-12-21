package game.util;

import java.io.*;
import java.util.*;

public class DataManager {
    private static DataManager instance;
    private static final String FILE_NAME = "best_times_v2.dat";

    private Map<Integer, List<ScoreEntry>> bestTimes;
    private String currentPlayer = "Unknown";

    private DataManager() {
        bestTimes = new HashMap<>();
        loadData();
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public void setCurrentPlayer(String name) {
        this.currentPlayer = name;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void addTimeRecord(int levelIndex, long timeInSeconds) {
        if (timeInSeconds <= 0) return;

        bestTimes.putIfAbsent(levelIndex, new ArrayList<>());
        List<ScoreEntry> entries = bestTimes.get(levelIndex);

        entries.add(new ScoreEntry(currentPlayer, timeInSeconds));

        Collections.sort(entries);

        if (entries.size() > 10) {
            bestTimes.put(levelIndex, new ArrayList<>(entries.subList(0, 10)));
        }

        saveData();
    }

    public List<String> getTopTimesForLevel(int levelIndex) {
        List<ScoreEntry> entries = bestTimes.getOrDefault(levelIndex, Collections.emptyList());
        if (entries.isEmpty()) {
            return List.of("Zatím žádné časy.");
        }

        List<String> formatted = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            ScoreEntry e = entries.get(i);
            formatted.add((i + 1) + ". " + e.playerName + ": " + e.timeInSeconds + "s");
        }
        return formatted;
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(bestTimes);
        } catch (IOException e) {
            System.err.println("Chyba při ukládání: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof Map) {
                bestTimes = (Map<Integer, List<ScoreEntry>>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            bestTimes = new HashMap<>();
        }
    }

    // Pomocná třída pro záznam (Jméno + Čas)
    private static class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
        private static final long serialVersionUID = 1L;
        String playerName;
        long timeInSeconds;

        public ScoreEntry(String playerName, long timeInSeconds) {
            this.playerName = playerName;
            this.timeInSeconds = timeInSeconds;
        }

        @Override
        public int compareTo(ScoreEntry other) {
            return Long.compare(this.timeInSeconds, other.timeInSeconds);
        }
    }
}