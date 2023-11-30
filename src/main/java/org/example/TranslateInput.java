package org.example;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import java.io.Serializable;
import java.util.List;

public class TranslateInput implements Serializable {

    @Getter
    private final String destLanguage;
    @Getter
    private final List<String> lines;

    public TranslateInput(String destLanguage, List<String> lines) {
        this.destLanguage=destLanguage;
        this.lines = lines;
    }


    /** This function create the json body stings for the Google Translate API.
     * The structure of the string should follow the order defined in the following link:
     * https://cloud.google.com/translate/docs/basic/translating-text
     * This function is useful in TranslateService.translate() function.
     */

    public String getDestLanguage() {
        return destLanguage;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getJsonBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("q", new JSONArray(lines)); // Convert lines to JSON array
        jsonObject.put("target", destLanguage); // Adding destination language to the request body
        return jsonObject.toString();
    }


}