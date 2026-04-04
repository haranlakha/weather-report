import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.Date;
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

        System.out.println("---Welcome to the Weather Report Program---");

        System.out.println("Enter location:");
        currentUser.setLocation(scnr.nextLine());

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
        }

        connection.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        WeatherResponse weatherResponse = objectMapper.readValue(responseBody, WeatherResponse.class);

        StringWriter stringResponse = new StringWriter();
        objectMapper.writeValue(stringResponse, weatherResponse);

        Date currentDate = new Date(weatherResponse.getDt() * 1000);
        Date sunrise = new Date(weatherResponse.getSys().getSunrise() * 1000);
        Date sunset = new Date(weatherResponse.getSys().getSunset() * 1000);

        System.out.println("Current Weather for " + weatherResponse.getName());

        System.out.println("---" + currentDate + "---");

        System.out.println("Temperature: " + weatherResponse.getMain().getTemp() + "°");
        System.out.println("Sunrise: " + sunrise);
        System.out.println("Sunset: " + sunset);


    }
}