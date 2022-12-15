import org.apache.hc.core5.net.URIBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

public class Main {

    static final String HOST = "api.openweathermap.org";
    static final String PATH = "data/2.5/forecast";
    static final String PARAM_ID = "524901";
    static final String API_KEY = "f8feabfe3eddc6a9c2547b409b947c4e";

    protected static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {

        try {
            var url = buildURL();
            var conn = createConnection(url);
            var response = makeRequest(conn);
            log.info(response);
        } catch (IOException | URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private static URL buildURL() throws MalformedURLException, URISyntaxException {
        URIBuilder builder = new URIBuilder()
                .setScheme("https")
                .setHost(HOST)
                .setPath(PATH)
                .setParameter("id", PARAM_ID)
                .setParameter("appid", API_KEY);

        return builder.build().toURL();
    }

    private static HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }

    private static String makeRequest(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();

        if (status < 300) {
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                content.append(inputLine);
            }
            input.close();
            return content.toString();
        } else {
            return conn.getResponseMessage();
        }
    }
}