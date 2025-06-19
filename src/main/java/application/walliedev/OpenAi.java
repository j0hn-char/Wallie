package application.walliedev;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.cdimascio.dotenv.Dotenv;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class OpenAi  {

    public static String getResponse(String query) throws Exception  {

        Dotenv dotenv = Dotenv.load();
        String ENDPOINT = dotenv.get("ENDPOINT");
        String APIKEY = dotenv.get("APIKEY");

        String requestBody = String.format("""
            {
              "model": "gpt-4.1",
              "messages": [
                {"role": "user", "content": "%s"}
              ]
            }
            """, query);

        // Send request to endpoint and get response
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + APIKEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse response and return content string
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> json = gson.fromJson(response.body(), type);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>)json.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        @SuppressWarnings("unchecked")
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        return (String) message.get("content");

    }
}

