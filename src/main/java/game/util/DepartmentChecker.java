package game.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DepartmentChecker {
    private static final String URL_FEI = "https://www.fei.vsb.cz/460/cs/kontakt/lide/";

    public static boolean checkUser(String name) {
        try {

            URL url = new URL(URL_FEI);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(name)) return true;
                }
            }

            // Pro testování vracíme true pro "admin"
            return name.equalsIgnoreCase("admin");
        } catch (Exception e) {
            return false;
        }
    }
}