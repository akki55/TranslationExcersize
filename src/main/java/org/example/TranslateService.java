package org.example;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Service
public class TranslateService {

    private String apiUrl;
    private String apiKey;
    @Getter
    private static URL endpoint;

    public TranslateService() {
        // We can initialize apiUrl, apiKey, and endpoint if needed, maybe if provided in some configuration file
    }
    public TranslateService(String apiUrl, String apiKey) throws MalformedURLException {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        // the endpoint of Google is a combination of the apiUrl and the key passed as a path parameter
        this.endpoint = new URL(apiUrl + "?key=" + apiKey);
    }

    public String getApiConfiguration() {
        return apiUrl + "?key=" + apiKey;
    }

    /** This  function takes the translateInput and modify it to make it compatible with Google Translate API
     * Please Check this https://cloud.google.com/translate/docs/basic/translating-text.
     * This function returns a list of stings such that each string is formated as the following:
     * "(detectedSourceLanguage) Line -> (destLanguage) Translated Line"
     */
    public static List<String> translate(TranslateInput translateInput) {
        List<String> reply = new ArrayList<>();

        String requestBody = translateInput.getJsonBody();

        try {
        JSONObject response = sendGoogleTranslateRequest(requestBody);

        if (response != null) {
            System.out.println("Google Translate API Response: " + response.toString());

                JSONObject data = response.getJSONObject("data");
                JSONArray translations = data.getJSONArray("translations");

                for (int i = 0; i < translations.length(); i++) {
                    JSONObject translation = translations.getJSONObject(i);
                    String detectedSourceLanguage = translation.optString("detectedSourceLanguage", "");
                    String translatedText = translation.optString("translatedText", "");
                    String originalLine = translateInput.getLines().get(i);

                    String formattedString = String.format(
                            "(%s) %s -> (%s) %s",
                            detectedSourceLanguage,
                            originalLine,
                            translateInput.getDestLanguage(),
                            translatedText
                    );

                    reply.add(formattedString);
                }
            } else {
            throw new TranslateServiceException("An unexpected error occurred: Response is null.");
        }
        } catch (JSONException e) {
            throw new TranslateServiceException("An unexpected error occurred while processing the response.", e);
        }
        return reply;
    }

    /** This function takes the Google Translate request body, establish a connection and gets the response as a JSONobject
     * This response is returned and processed in the translate function.
     */
    public static JSONObject sendGoogleTranslateRequest(String requestBody) {
        try {
            if (endpoint == null) {
                throw new TranslateServiceException("API endpoint is not configured. Send configuration to api endpoint!");
            }

            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();

            // Set the necessary headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write the request body to the connection

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
                System.out.println("Google Translate API Request Body: " + requestBody);
            }

            // Get the response from the server

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Google Translate API Response Body: " + response.toString());
                return new JSONObject(response.toString());
            }
        } catch (IOException | JSONException e) {
            throw new TranslateServiceException("Error during Google Translate API request.", e);
        }

    }
}
