# Translation-Exercise
A brief coding exercise designed to evaluate candidates' technical aptitude and ability to write clean, maintainable code.


## Tasks

Starting from a microservice skeleton, your objective is to create a functional Miroservice that allows us to send querys 
that contains the destination language and a list of lines to be translated. 
This is done by changing the codes of the classes: RestController, TranslateInput, TranslateService. 

You can find some  useful comments inside the code in addition to indications to where the code should be modified.
> **NOTE**: please maintain the same code structure. 


## The Microservice Description:
In this microservice we define two endpoints:

- **setApi**:An endpoint that can be used to set the TranslateService with the API URL and API Key. The endpoint receives a JSON input containing the apiKey and the apiUrl. They are then defined as the  translateService parameters.
The endpoint returns a full string of the API URL in addition to the key passed as a path parameter. The Request body follow this format: 
```json
{
"apiUrl": "https://translation.googleapis.com/language/translate/v2",
"apiKey": "<SOME_KEY>"
}
```

The reply follows this format: 
```url
The API URL has been configured as: https://translation.googleapis.com/language/translate/v2?key=<SOME_KEY>
```
> **NOTE**: the Key will be provided separately. 

- **translate**: An endpoint used to translate an array of lines to a destination language defined in the request body.
The request body should follow the TranslateInput class. It should have the following format (Example):
```json
{ "destLanguage" : "es",
  "lines" : ["My name is ali",
              "This is cool!",
              "Hrvatska ima fantastičnu nogometnu momčad"
          ]
}
```
The return value is returned by the function TranslateService.translate() and it should be a List<String> of
the following format (Example):
```json
[
   "(en) My name is ali -> (es) Mi nombre es Ali",
   "(en) This is cool! -> (es) ¡Esto es genial!",
   "(hr) Hrvatska ima fantastičnu nogometnu momčad -> (es) Croacia tiene un fantástico equipo de fútbol."
]

```

## How To Run:

The code depends on **Java 1.8**, Gradle, springframework, and Lombok.

To build: 
```bash
./gradlew build
```

To Run: 

```bash
./gradlew bootRun
```

The endpoints are: 

setApi: 
```url 
POST http://localhost:8081/api
```

translate:
```url 
POST http://localhost:8081/api/translate
```

> **NOTE**: It's advised to use tools like postman for testing. 


Good Luck!