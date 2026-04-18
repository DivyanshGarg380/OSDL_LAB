/*
Author :

███████╗████████╗ █████╗ ██████╗  ███╗   ███╗ █████╗ ███╗   ██╗
██╔════╝╚══██╔══╝██╔══██╗██╔══██╗ ████╗ ████║██╔══██╗████╗  ██║
███████╗   ██║   ███████║██████╔╝ ██╔████╔██║███████║██╔██╗ ██║
╚════██║   ██║   ██╔══██║██║  ██║ ██║╚██╔╝██║██╔══██║██║╚██╗██║
███████║   ██║   ██║  ██║██║  ██║ ██║ ╚═╝ ██║██║  ██║██║ ╚████║
╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝  STARMAN248
*/

/*
    Hotel Room Booking System — OOP + JavaFX

    - Design a Java application with a JavaFX GUI for a hotel room booking system. 
    - Create an abstract class Room with fields roomNumber and basePrice, and an abstract method calculateTariff(). 
    - Derive StandardRoom and LuxuryRoom from it, overriding calculateTariff() — StandardRoom adds 10% tax while LuxuryRoom adds 10% tax plus a ₹500 amenity charge. 
    - Implement an Amenities interface with provideWifi() and provideBreakfast() methods; LuxuryRoom must implement it. 
    - In the JavaFX GUI, provide a ComboBox to choose room type, a TextField for number of nights, a Button labeled 'Calculate', and a Label that shows the final tariff. 
    - Use a base class reference (Room) to call calculateTariff() so that runtime polymorphism is demonstrated when the button is clicked.
*/

import java.awt.TextField;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

abstract class Room {
    int roomNumber;
    double basePrice;

    public Room(int roomNumber, double basePrice) {
        this.roomNumber = roomNumber;
        this.basePrice = basePrice; 
    }

    public abstract double calculateTariff(int nights);
}

interface Amenities {
    void provideWifi();
    void provideBreakfast();
}

class StandardRoom extends Room {
    public StandardRoom(int roomNumber, double basePrice) {
        super(roomNumber, basePrice);
    }

    @Override
    public double calculateTariff(int nights) {
        double total = basePrice * nights;
        return total + (total * 0.10);
    }
}

class LuxuryRoom extends Room implements Amenities {
    public LuxuryRoom(int roomNumber, double price) {
        super(roomNumber, price);
    }

    @Override
    public double calculateTariff(int nights) {
        double total = basePrice * nights;
        return total + (0.10 * total) + 500;
    }

    @Override
    public void provideWifi() {
        System.out.println("Wifi Provided");
    }

    @Override
    public void provideBreakfast() {
        System.out.println("Breakfast Provided");
    }
}

public class Q1 extends Application {
    @Override
    public void start(Stage stage) {
        Label lbl = new Label("Select Room Type:");
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Standard", "Luxury");

        Label lbNights = new Label("Enter Nights:");
        TextField txtNights = new TextField();

        Button btn = new Button("Calculate");
        Label result = new Label();

        btn.setOnAction(e -> {
            try {
                String type = combo.getValue();
                int nights = Integer.parseInt(txtNights.getText());

                Room room;
                if(type.equals("Standard")) room = new StandardRoom(101, 2000);
                else room = new LuxuryRoom(102, 5000);

                double tariff = room.calculateTariff(nights);
                result.setText("Total Tariff: " + tariff);
            } catch (Exception e) {
                result.setText("Invalid Input");
            }
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(lbl, combo, lbNights, txtNights, btn, result);
        Scene scene = new Scene(root, 300, 250);
        stage.setTitle("Hotel Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}