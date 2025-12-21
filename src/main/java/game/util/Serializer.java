package game.util;
import game.model.World;
import java.io.*;

public class Serializer {
    public static void saveGame(World world, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(world);
        } catch (IOException e) { e.printStackTrace(); }
    }
    public static World loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (World) in.readObject();
        } catch (Exception e) { return null; }
    }
}