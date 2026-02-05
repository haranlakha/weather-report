import java.util.Scanner;

public class Weather {
    public static void main(String[] args) {

        Scanner scnr  = new Scanner(System.in);

        User currentUser = new User();

        System.out.println("Please enter your name: ");
        currentUser.setName(scnr.nextLine());

        System.out.println("Hello " + currentUser.getName() + "!\n"+"Welcome to the Weather Report Program");

        Location currentLocation = new Location();

        System.out.println("Where are you located?");
        currentLocation.setLocationName(scnr.nextLine());

        System.out.println("We will get the weather for " + currentLocation.getLocationName());


    }
}