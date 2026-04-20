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
    Generic hotel pair with bounded discount and JavaFX table

    - Create a generic class RoomBooking where T is the room identifier type and U is the price type. 
    - Store at least six bookings using Integer room IDs and Double prices. 
    - Write a bounded generic method double applyDiscount(T price, T discountPercent) that returns the discounted price. 
    - Use an Iterator to traverse the booking list and apply the discount to each record. 
    - Display all bookings in a JavaFX TableView with columns for room ID, original price, and discounted price. 
    - Add a TextField for entering a discount percentage and a Button 'Apply Discount' that recomputes all discounted prices in the table when clicked.
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.*;
import java.util.*;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

public class Q13 extends Application {
    public static class RoomBooking<T, U> {
        private T roomId;
        private U price;

        RoomBooking(T roomId, U price) {
            this.roomId = roomId;
            this.price = price;
        }

        public T getRoomId() { return roomId; }
        public U getPrice() { return price; }
    }

    public static class BookingData {
        private Integer roomId;
        private Double originalPrice;
        private Double discountedPrice;

        BookingData(Integer id, Double price, Double discount){
            this.roomId = id;
            this.originalPrice = price;
            this.discountedPrice = discount;
        }

        public Integer getRoomId() { return roomId; }
        public Double getOriginalPrice() { return originalPrice; }
        public Double getDiscountedPrice() { return discountedPrice; }
    }

    public static <T extends Number> double applyDiscount(T price, T discountPercentage) {
        double p = price.doubleValue();
        double d = discountPercentage.doubleValue();
        return p - (p * d / 100);
    }   

    @Override
    public void start(Stage stage) {
        ArrayList<RoomBooking<Integer, Double>> list = new ArrayList<>();
        list.add(new RoomBooking<>(101, 2000.0));
        list.add(new RoomBooking<>(102, 2500.0));
        list.add(new RoomBooking<>(103, 3000.0));
        list.add(new RoomBooking<>(104, 3500.0));
        list.add(new RoomBooking<>(105, 4000.0));
        list.add(new RoomBooking<>(106, 4500.0));

        TableView<BookingData> table = new TableView<>();

        TableColumn<BookingData, Integer> c1 = new TableColumn<>("Room ID");
        c1.setCellPropertyFactory(new PropertyValueFactory<>("roomId"));

        TableColumn<BookingData, Double> c2 = new TableColumn<>("Original Price");
        c2.setCellPropertyFactory(new PropertyValueFactory<>("originalPrice"));

        TableColumn<BookingData, Double> c3 = new TableColumn<>("Discounted Price");
        c3.setCellPropertyFactory(new PropertyValueFactory<>("discountedPrice"));

        table.getColumns().addAll(c1, c2, c3);

        TextField txtDiscount = new TextField();
        txtDiscount.setPromptText("Enter Discount %");

        Button btn = new Button("Apply Discount");


        btn.setOnAction(e -> {
            try {
                double discount = Double.parseDouble(txtDiscount.getText());
                Iterator<RoomBooking<Integer, Double>> itr = list.iterator();

                ObservableList<BookingData> data = FXCollections.observableArrayList();
                while(itr.hasNext()) {
                    RoomBooking<Integer, Double> rb = itr.next();
                    double original = rb.getPrice();
                    double discounted = applyDiscount(original, discount);
                    data.add(new BookingData(rb.getRoomId(), original, discount));
                }

                table.setItems(data);

            } catch (Exception ex) { System.out.println("Invalid Input");}
        });

        VBox root = new VBox(10, txtDiscount, btn, table);

        Scene scene = new Scene(root, 450, 400);

        stage.setTitle("Hotel Booking Discount");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}