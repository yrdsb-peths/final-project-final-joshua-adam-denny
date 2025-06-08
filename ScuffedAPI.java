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
 * @version Version 1.0.0.(May 29, 2025)
 */
public class ScuffedAPI {
    // api url
    private static ScuffedAPI instance;
    //private final String api_url = "https://api-ddc.scuffed.dev";
    private final String api_url = "http://localhost:8080";
    private final String api_key = "ballsballsballs";// not really a key, but i hate bots so yuh. too lazy to implement a proper key system., unused
    private final String user = UUID.randomUUID().toString().replace("-", "").substring(0, 8);;
    private static final Pattern PLACE_PATTERN = Pattern.compile("\"place\"\\s*:\\s*(\\d+)");
    private boolean connected = false; 
    public static synchronized ScuffedAPI getInstance() {
        if (instance == null) {
            instance = new ScuffedAPI();
        }
        return instance;
    }
    
    public String getUUID()
    {
        return user;
    }
    
    public boolean isConnected()
    {
        return connected;
    }
    
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
    
    public int sendScore(int score, int wave) throws IOException {
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
