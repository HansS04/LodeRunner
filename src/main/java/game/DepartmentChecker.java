package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class DepartmentChecker {

    private static final String FEI_URL = "https://www.fei.vsb.cz/460/cs/kontakt/lide/";

    public static boolean isMemberOfDepartment(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) return false;
        System.out.println("Ověřuji uživatele: " + playerName);

        try {
            String htmlContent = downloadPage(FEI_URL);
            String normalizedInput = playerName.trim();
            String regex = "(?i).*" + Pattern.quote(normalizedInput) + ".*";

            if (htmlContent.matches(regex) || htmlContent.toLowerCase().contains(normalizedInput.toLowerCase())) {
                System.out.println("✅ Člen katedry nalezen!");
                return true;
            }
        } catch (Exception e) {
            System.err.println("Chyba ověření: " + e.getMessage());
        }
        return false;
    }

    private static String downloadPage(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }
}