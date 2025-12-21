package game.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DepartmentChecker {
    private static final String URL_FEI = "https://www.fei.vsb.cz/460/cs/kontakt/lide/";

    public static boolean checkUser(String name) {
        // Backdoor pro admina
        if (name.equalsIgnoreCase("admin")) return true;

        try {
            URL url = new URL(URL_FEI);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Jednoduché hledání jména v HTML kódu stránky
                    if (line.contains(name)) return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            // V případě chyby sítě raději nepustíme dál (nebo return true pro offline dev)
            return false;
        }
    }
}