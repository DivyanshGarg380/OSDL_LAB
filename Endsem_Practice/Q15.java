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
    Implement a Generic Hotel Booking Persistence System using Javafx

    1) Add Booking
        - Allow users to add a booking with details:
            • Room ID (Generic type T)
            • Price (Generic type U extends Number)
            • Guest Name
            • Booking Status
        - Store bookings in a serialized file (e.g., bookings.dat).

    2) View All Bookings
        - Deserialize and display all bookings in a TableView.

    3) Search Booking by Room ID
        - Retrieve booking using generic Room ID.

    4) Apply Discount (Generic Bounded Method)
        - Apply discount using:
            double applyDiscount(U price, U percent)
        - Update and display discounted prices.

    5) Delete Booking
        - Remove booking by Room ID and rewrite file.

    6) Exception Handling
        - Handle:
            • IOException
            • ClassNotFoundException
            • Invalid inputs
            • Duplicate entries

    Requirements:
        - Create a generic class Booking<T, U> that implements Serializable.
        - Use ArrayList<Booking<T,U>> for storage.
        - Use ObjectOutputStream / ObjectInputStream.
        - Use JavaFX controls (TextField, Button, TableView).
        - Use a bounded generic method (<U extends Number>) for discount.
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

import java.io.*;

public class Q15 extends Application {
    public static class Booking<T, U extends Number> implements Serializable {
        private T roomId;
        private U price;
        private String guest;
        private Boolean booked;

        Booking(T id, U price, String guest, Boolean b) {
            this.roomId = id;
            this.price = price;
            this.guest = guest;
            this.booked = b;
        }

        public T getRoomId() { return roomId; }
        public U getPrice() { return price; }
        public String getGuest() { return guest; }
        public Boolean getBooked() { return booked; }
    }

    public static class FileUtil {
        static final String FILE = "bookings.dat";

        static void writeAll(ArrayList<Booking<Integer, Double>> list) throws IOException {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE));
            oos.writeObject(list);
            oos.close();
        }

        static ArrayList<Booking<Integer, Double>> readAll() throws IOException, ClassNotFoundException {
            File f = new File(FILE);
            if(!f.exists()) return new ArrayList<>();

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE));
            ArrayList<Booking<Integer, Double>> list = (ArrayList<Booking<Integer, Double>>) ois.readObject();
            ois.close();
            return list;
        }
    }

    public static <U extends Number> double applyDiscount(U price, U percent) {
        double p = price.doubleValue();
        double d = percent.doubleValue();
        return p - (p * d / 100);
    }

    @Override
    public void start(Stage stage) {
        TextField txtId = new TextField(); txtId.setPromptText("Room ID");
        TextField txtPrice = new TextField(); txtPrice.setPromptText("Price");
        TextField txtGuest = new TextField(); txtGuest.setPromptText("Guest");
        TextField txtDiscount = new TextField(); txtDiscount.setPromptText("Discount %");

        Button btnAdd = new Button("Add");
        Button btnView = new Button("View");
        Button btnSearch = new Button("Search");
        Button btnDelete = new Button("Delete");
        Button btnDiscount = new Button("Apply Discount");

        Label status = new Label();

        TableView<Booking<Integer, Double>> table = new TableView<>();

        TableColumn<Booking<Integer, Double>, Integer> c1 = new TableColumn<>("Room");
        c1.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        TableColumn<Booking<Integer, Double>, Double> c2 = new TableColumn<>("Price");
        c2.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Booking<Integer, Double>, String> c3 = new TableColumn<>("Guest");
        c3.setCellValueFactory(new PropertyValueFactory<>("guest"));

        table.getColumns().addAll(c1, c2, c3);

        btnAdd.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                double price = Double.parseDouble(txtPrice.getText());

                ArrayList<Booking<Integer, Double>> list = FileUtil.readAll();

                for (Booking<Integer, Double> b : list) {
                    if (b.getRoomId().equals(id)) {
                        throw new Exception("Duplicate Room");
                    }
                }

                list.add(new Booking<>(id, price, txtGuest.getText(), true));
                FileUtil.writeAll(list);

                status.setText("Added");

            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
            }
        });

        btnView.setOnAction(e -> {
            try {
                table.setItems(FXCollections.observableArrayList(FileUtil.readAll()));
            } catch (Exception ex) {
                status.setText("Error");
            }
        });

        btnSearch.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                ArrayList<Booking<Integer, Double>> list = FileUtil.readAll();

                for (Booking<Integer, Double> b : list) {
                    if (b.getRoomId().equals(id)) {
                        table.setItems(FXCollections.observableArrayList(b));
                        return;
                    }
                }

                status.setText("Not Found");

            } catch (Exception ex) {
                status.setText("Invalid");
            }
        });

        btnDelete.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                ArrayList<Booking<Integer, Double>> list = FileUtil.readAll();

                list.removeIf(b -> b.getRoomId().equals(id));
                FileUtil.writeAll(list);

                status.setText("Deleted");

            } catch (Exception ex) {
                status.setText("Error");
            }
        });


        btnDiscount.setOnAction(e -> {
            try {
                double d = Double.parseDouble(txtDiscount.getText());

                ArrayList<Booking<Integer, Double>> list = FileUtil.readAll();
                ObservableList<Booking<Integer, Double>> updated = FXCollections.observableArrayList();

                for (Booking<Integer, Double> b : list) {
                    double newPrice = applyDiscount(b.getPrice(), d);
                    updated.add(new Booking<>(b.getRoomId(), newPrice, b.getGuest(), true));
                }

                table.setItems(updated);

            } catch (Exception ex) {
                status.setText("Invalid Discount");
            }
        });


        VBox root = new VBox(8,
                txtId, txtPrice, txtGuest, txtDiscount,
                btnAdd, btnView, btnSearch, btnDelete, btnDiscount,
                table, status
        );

        Scene scene = new Scene(root, 500, 600);

        stage.setTitle("Generic Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
