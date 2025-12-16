package game;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataManager {
    private static final String SETTINGS_FILE = "game_settings.properties";
    private static final String SCORES_FILE = "scores.csv";
    private static final String SAVE_FILE = "loderunner_save.bin";

    public static Properties loadSettings() {
        Properties props = new Properties();
        props.setProperty("difficulty", "NORMAL"); // Default

        Path path = Paths.get(SETTINGS_FILE);
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("Chyba nastavení: " + e.getMessage());
            }
        } else {
            saveSettings(props);
        }
        return props;
    }

    public static void saveSettings(Properties props) {
        try (OutputStream out = Files.newOutputStream(Paths.get(SETTINGS_FILE))) {
            props.store(out, "Lode Runner Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- SKÓRE ---
    public static void saveScore(String playerName, int score) {
        String record = String.format("%s,%d,%s%n", playerName, score, new Date().toString());
        try {
            Files.write(Paths.get(SCORES_FILE), record.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Skóre uloženo.");
        } catch (IOException e) {
            System.err.println("Chyba skóre: " + e.getMessage());
        }
    }

    public static void saveGameState(GameState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(state);
            System.out.println("Hra uložena do " + SAVE_FILE);
        } catch (IOException e) {
            System.err.println("Chyba ukládání hry: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static GameState loadGameState() {
        if (!Files.exists(Paths.get(SAVE_FILE))) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Chyba načítání hry: " + e.getMessage());
            return null;
        }
    }
}