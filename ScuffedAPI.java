import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;
import java.util.UUID;
/**
 * god had no hands in making this.
 * 
 * ScuffedAPI (BASIC) implementation
 * Leaderboard Handling.
 * 
 * @author Denny Ung
 * @version Version 1.5.3 (June 11, 2025)
 */
public class ScuffedAPI {
    // api url
    private static ScuffedAPI instance;
    private final String api_url = "https://api-ddc.scuffed.dev";
    private static String user = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    private static final Pattern PLACE_PATTERN = Pattern.compile("\"place\"\\s*:\\s*(\\d+)");
    private boolean connected = false; 
    public static synchronized ScuffedAPI getInstance() {
        if (instance == null) {
            instance = new ScuffedAPI();
        }
        return instance;
    }
    
    public static String getUUID()
    {
        return user;
    }
    
    public boolean isConnected()
    {
        return connected;
    }
    
    public static void setUsername(String username)
    {
        user = username;
    }

    /*  
     * Connects to the ScuffedAPI server.
     * Returns true if the connection was successful, false otherwise.
     *  
     *  This method sends a GET request to the "/connect" endpoint of the API.
     * It checks the response code to determine if the connection was successful.
     * 
     */
    
    public boolean connect() {
        try {
            URL url = new URL(api_url + "/connect");
            HttpURLConnection client = (HttpURLConnection)url.openConnection();
            client.setRequestMethod("GET");
            int code = client.getResponseCode();
            client.disconnect();
            connected = code >= 200 && code < 300;
            return connected;
        } catch (IOException e) {
            connected = false;
            return connected;
        }
    }

    /**
     * Sends the score and wave to the ScuffedAPI server.
     * 
     * @param score The score to send.
     * @param wave The wave number to send.
     * @return The place in the leaderboard as an integer.
     * @throws IOException If an error occurs while sending the score.
     */
    
    public int sendScore(long score, int wave) throws IOException {
        URL url = new URL(api_url + "/sendScore");
        HttpURLConnection client = (HttpURLConnection) url.openConnection();

        client.setRequestMethod("POST");
        client.setDoOutput(true);
        client.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // ... why doesnt java have JSON parsing built in?. string to json String
        String json = "{" + "\"id\":\"" + user + "\"," + "\"score\":" + score + "," + "\"wave\":" + wave + "}";

        try (OutputStream os = client.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = client.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? client.getInputStream() : client.getErrorStream();
        String resp = readStream(is);
        client.disconnect();

        Matcher m = PLACE_PATTERN.matcher(resp);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        throw new IOException("Invalid response (no place): " + resp);

    }
    
    

    /**
     * Retrieves the leaderboard from the ScuffedAPI server.
     * 
     * @return A list of LeaderboardEntry objects representing the leaderboard.
     * @throws IOException If an error occurs while retrieving the leaderboard.
     */

    public List<LeaderboardEntry> getLeaderboard() throws IOException {
        URL url = new URL(api_url + "/getLeaderboard");
        HttpURLConnection client = (HttpURLConnection) url.openConnection();
        client.setRequestMethod("GET");

        int status = client.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? client.getInputStream() : client.getErrorStream();
        String resp = readStream(is);
        client.disconnect();

        List<LeaderboardEntry> list = new ArrayList<>();

        for (String line : resp.split("\\r?\\n")) {
            if (line.length() == 0)
                continue;
            String[] parts = line.split(",");
            if (parts.length < 3)
                continue;
            String entryId = parts[0];
            int entryScore = Integer.parseInt(parts[1]);
            int entryWave = Integer.parseInt(parts[2]);
            list.add(new LeaderboardEntry(entryId, entryScore, entryWave));
        }

        return list;
    }

    private static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }


    /**
     * Represents a single entry in the leaderboard.
     * Contains the user's ID, score, and wave number.
     */
    public static class LeaderboardEntry {
        public final String id;

        public final int score;
        public final int wave;

        public LeaderboardEntry(String id, int score, int wave) {
            this.id = id;
            this.score = score;
            this.wave = wave;
        }

        @Override
        public String toString() {
            return String.format("Username: %s \nScore: %d | Wave: %d", id, score, wave);
        }
    }
}
