package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.MalformedURLException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
class RestController {

    @Autowired
    private TranslateService translateService;

    /** An endpoint that can be used to set the TranslateService with the API URL and API Key.
     * The endpoint receives a JSON input containing the apiKey and the apiUrl. They are then defined as the  translateService
     * parameters.
     * The endpoint returns a full string of the API URL in addition to the key passed as a path parameter.
     */

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String setApi(@RequestBody String requestPayload) {
        try {
            System.out.println("setApi Request Payload: " + requestPayload);
            JSONObject jsonPayload = new JSONObject(requestPayload);

            String apiUrl = jsonPayload.getString("apiUrl");
            String apiKey = jsonPayload.getString("apiKey");

            translateService = new TranslateService(apiUrl, apiKey);

            return "The API URL has been configured as: " + translateService.getApiConfiguration();
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL exception: " + e.getMessage());
            return handleMalformedURLException(e);
        } catch (Exception e) {
            System.err.println("Unexpected error in setApi: " + e.getMessage());
            return handleUnexpectedException(e);
        }
    }

    /** An endpoint used to translate an array of lines to a destination language defined in the request body.
     * The request body should follow the TranslateInput class. It should have the following format (Example):
     * { "destLanguage" : "es",
     *   "lines" : ["My name is ali",
     *              "This is cool!",
     *              "Hrvatska ima fantastičnu nogometnu momčad"
     *              ]
     * }
     * The return value is returned by the function TranslateService.translate() and it should be a List<String> of
     * the following format (Example):
     * [
     *     "(en) My name is ali -> (es) Mi nombre es Ali",
     *     "(en) This is cool! -> (es) ¡Esto es genial!",
     *     "(hr) Hrvatska ima fantastičnu nogometnu momčad -> (es) Croacia tiene un fantástico equipo de fútbol."
     * ]
     */
    @RequestMapping(value = "/translate", method = RequestMethod.POST)
    public List<String> Translate(@RequestBody TranslateInput translateInput) {
        return TranslateService.translate(translateInput);
    }

    private String handleMalformedURLException(MalformedURLException e) {
        // Handle the specific exception
        return "Malformed URL exception: " + e.getMessage();
    }

    private String handleUnexpectedException(Exception e) {
        // Handle unexpected exceptions
        return "An unexpected error occurred: " + e.getMessage();
    }
}