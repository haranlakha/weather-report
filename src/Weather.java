import java.io.IOException;
import java.util.Scanner;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Weather {
    public static void main(String[] args) throws IOException {

        Scanner scnr  = new Scanner(System.in);

        User currentUser = new User();

        currentUser.setApiKey(""); //Populate with API key if not using prompt

        System.out.println("Please enter your name: ");
        currentUser.setName(scnr.nextLine());

        System.out.println("Hello " + currentUser.getName() + "!\n"+"Welcome to the Weather Report Program");

        Location currentLocation = new Location();

        System.out.println("Where are you located?");
        currentLocation.setLocationName(scnr.nextLine());

        System.out.println("We will get the weather for " + currentLocation.getLocationName());


        if(currentUser.getApiKey().isEmpty()) {
            System.out.println("Enter API Key for Endpoint: ");
            currentUser.setApiKey(scnr.nextLine());
        }

        URL currentUrl = new URL("https://api.openweathermap.org/geo/1.0/direct?q=" + currentLocation.getLocationName() + "&limit=1&appid=" + currentUser.getApiKey());

        HttpURLConnection connection = (HttpURLConnection) currentUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int httpResponseCode = connection.getResponseCode();
        if(httpResponseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP GET request failed with response code: " + httpResponseCode);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            String responseBody = reader.lines().collect(Collectors.joining());

            System.out.println("Response Body: " + responseBody);

        }

        connection.disconnect();




    }
}