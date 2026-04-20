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
    Room type enum with tariff and JavaFX display

    - Define an enum RoomType with constants STANDARD, DELUXE, and SUITE, each carrying a base price and a service charge as instance variables initialized through an enum constructor. 
    - Add an enum method calculateFinalCost(int nights) that returns basePrice × nights + serviceCharge. 
    - In a JavaFX window, display all three room types in a ComboBox, provide a Spinner or TextField for number of nights, a Button 'Calculate Cost', and a Label that shows the total cost by calling the enum method. 
    - Also add a Label that lists all enum constants and their base prices using RoomType.values() when the application starts.
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class Q11 extends Application {
    public static enum RoomType {
        STANDARD(2000, 200), DELUXE(3500, 400), SUITE(5000, 700);

        private int basePrice;
        private int serviceCharge;

        RoomType(int basePrice, int serviceCharge) {
            this.basePrice = basePrice;
            this.serviceCharge = serviceCharge;
        }

        public int getBasePrice() { return basePrice; }
        public double calcFinal(int nights) { return (basePrice * nights) + serviceCharge; }
    }

    @Override
    public void start(Start stage) {
        comboBox<RoomType> combo = new ComboBox<>();
        combo.getItems().addAll(RoomType.values());

        TextField tnights = new TextField(); tnights.setPromptText("Enter Nights");
        Button btn = new Button("Calculate Cost");

        Label result = new Label();
        Label enumInfo = new Label();

        StringBuilder sb = new StringBuilder("Room Types:\n");
        for(RoomType r : RoomType.values()) {
            sb.append(r.name()).append(" -> Price: ").append(r.getBasePrice()).append("\n");
        }

        enumInfo.setText(sb.toString());

        btn.setOnAction(e -> {
            try {
                RoomType selected = combo.getValue();
                int nights = Integer.parseInt(tnights.getText());
                double cost = selected.calcFinal(nights); 
                
                result.setText("Total Cost: " + cost);
                
            } catch (Exception ex) {
                result.setText("Invalid Input");
            }
        });

        VBox root = new VBox(10, enumInfo, combo, tnights, btn, result);
        Scene scene = new Scene(root, 350, 300);
        stage.setTitle("Room Tariff Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
