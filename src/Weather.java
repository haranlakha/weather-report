import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;

import json.*;

public class Weather {
    public static void main(String[] args) throws IOException {

        Scanner scnr  = new Scanner(System.in);

        User currentUser = new User();
        String responseBody;

        String fileName = "config.properties"; //file name for API_KEY

        Properties properties = new Properties();
        properties.load(new FileInputStream(fileName));

        String apiKey = properties.getProperty("API_KEY");

        if (apiKey == null) {
            System.out.println("Please enter a valid API Key:");
            apiKey = scnr.nextLine();
        }

        System.out.println("Please enter your name: ");
        currentUser.setName(scnr.nextLine());

        System.out.println("Hello " + currentUser.getName() + "!\n"+"Welcome to the Weather Report Program");

        System.out.println("Where are you located?");
        currentUser.setLocation(scnr.nextLine());

        System.out.println("We will get the weather for " + currentUser.getLocation());

        URL currentUrl = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + currentUser.getLocation() + "&appid=" + apiKey + "&units=metric");

        HttpURLConnection connection = (HttpURLConnection) currentUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int httpResponseCode = connection.getResponseCode();

        if (httpResponseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP GET request failed with response code: " + httpResponseCode);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
                    responseBody = reader.lines().collect(Collectors.joining());
                    System.out.println("Response Body: " + responseBody + "\n");
        }

        connection.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        WeatherResponse weatherResponse = objectMapper.readValue(responseBody, WeatherResponse.class);

        StringWriter stringResponse = new StringWriter();
        objectMapper.writeValue(stringResponse, weatherResponse);

        System.out.println("weatherResponse JSON: " + stringResponse);
    }
}