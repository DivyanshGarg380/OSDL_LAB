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
    Implement a Hotel Room Booking Persistence System using Java 

    The application should store and retrieve booking data using 
    serialization (ObjectOutputStream and ObjectInputStream).

    1) Add Room Booking
        - Allow users to add a booking with details such as:
            • Room Number
            • Room Type
            • Price per Night
            • Booking Status (true/false)
            • Guest Name
        - Store all bookings in a serialized file (e.g., rooms.dat).

    2) View All Bookings
        - Deserialize data from the file and display all room bookings 
          in a structured format (e.g., TableView).

    3) Search Booking by Room Number
        - Retrieve and display booking details for a specific room 
          using its room number.

    4) Update Booking Status
        - Modify the booking status (booked/vacant) of a room:
            • Deserialize all objects
            • Update the selected room object
            • Serialize the updated list back to file

    5) Delete Booking
        - Remove a booking using room number:
            • Deserialize list
            • Remove object
            • Serialize updated list

    6) Exception Handling
        - Implement proper exception handling for:
            • File not found
            • ClassNotFoundException
            • Invalid inputs
            • Empty file or missing data
            • Runtime errors

    Requirements:
        - Create a Room class that implements Serializable.
        - Use ArrayList to store room objects.
        - Use serialization to persist data (no database).
        - Integrate all functionalities within a JavaFX GUI.
        - Use controls like TextField, Button, TableView, Label.
        - Ensure proper validation and user-friendly interface.
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

public class Q14 extends Application {
    public static class Room implements Serializable {
        private Integer roomNumber;
        private String roomType;
        private Double price;
        private Boolean booked;
        private String guest;

        public Room(Integer r, String t, Double p, Boolean b, String g) {
            this.roomNumber = r;
            this.roomType = t;
            this.price = p;
            this.booked = b;
            this.guest = g;
        }

        public Integer getRoomNumber() { return roomNumber; }
        public String getRoomType() { return roomType; }
        public Double getPrice() { return price; }
        public Boolean getBooked() { return booked; }
        public String getGuest() { return guest; }

        public void setBooked(Boolean b) { booked = b; }
    }

    public static class FileUtil {
        static final String FILE = "rooms.dat";
        static void writeAll(ArrayList<Room> list) throws IOException {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE));
            oos.writeObject(list);
            oos.close();
        }

        static ArrayList<Room> readAll() throws IOException, ClassNotFoundException {
            File f = new File(FILE);
            if(!f.exists()) return new ArrayList<>();

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE));
            ArrayList<Room> list = (ArrayList<Room>) ois.readObject();
            ois.close();
            return list;
        }
    }

    @Override
    public void start(Stage stage) {
        TextField txtRoom = new TextField(); txtRoom.setPromptText("Room Number");
        TextField txtType = new TextField(); txtType.setPromptText("Room Type");
        TextField txtPrice = new TextField(); txtPrice.setPromptText("Price");
        TextField txtGuest = new TextField(); txtGuest.setPromptText("Guest Name");

        Button btnAdd = new Button("Add Booking");
        Button btnView = new Button("View All");
        Button btnSearch = new Button("Search");
        Button btnToggle = new Button("Toggle Booking");
        Button btnDelete = new Button("Delete");

        Label status = new Label();

        TableView<Room> table = new TableView<>();

        TableColumn<Room, Integer> c1 = new TableColumn<>("Room");
        c1.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> c2 = new TableColumn<>("Type");
        c2.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        TableColumn<Room, Double> c3 = new TableColumn<>("Price");
        c3.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Room, Boolean> c4 = new TableColumn<>("Booked");
        c4.setCellValueFactory(new PropertyValueFactory<>("booked"));

        TableColumn<Room, String> c5 = new TableColumn<>("Guest");
        c5.setCellValueFactory(new PropertyValueFactory<>("guest"));

        table.getColumns().addAll(c1, c2, c3, c4, c5); 

        btnAdd.setOnAction(e -> {
            try {
                int r = Integer.parseInt(txtRoom.getText());
                double p = Double.parseDouble(txtPrice.getText());

                ArrayList<Room> list = FileUtil.readAll();

                for (Room room : list) {
                    if (room.getRoomNumber().equals(r)) {
                        throw new Exception("Room already exists");
                    }
                }

                list.add(new Room(r, txtType.getText(), p, false, txtGuest.getText()));
                FileUtil.writeAll(list);

                status.setText("Booking Added");

            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
            }
        });

        btnView.setOnAction(e -> {
            try {
                table.setItems(FXCollections.observableArrayList(FileUtil.readAll()));
            } catch (Exception ex) {
                status.setText("Error reading file");
            }
        });

        btnSearch.setOnAction(e -> {
            try {
                int r = Integer.parseInt(txtRoom.getText());
                ArrayList<Room> list = FileUtil.readAll();

                for (Room room : list) {
                    if (room.getRoomNumber() == r) {
                        table.setItems(FXCollections.observableArrayList(room));
                        return;
                    }
                }

                status.setText("Not Found");

            } catch (Exception ex) {
                status.setText("Invalid Input");
            }
        });

        btnToggle.setOnAction(e -> {
            try {
                int r = Integer.parseInt(txtRoom.getText());
                ArrayList<Room> list = FileUtil.readAll();

                for (Room room : list) {
                    if (room.getRoomNumber() == r) {
                        room.setBooked(!room.getBooked());
                    }
                }

                FileUtil.writeAll(list);
                status.setText("Booking Updated");

            } catch (Exception ex) {
                status.setText("Error");
            }
        });

        btnDelete.setOnAction(e -> {
            try {
                int r = Integer.parseInt(txtRoom.getText());
                ArrayList<Room> list = FileUtil.readAll();

                list.removeIf(room -> room.getRoomNumber() == r);

                FileUtil.writeAll(list);
                status.setText("Deleted");

            } catch (Exception ex) {
                status.setText("Error");
            }
        });

        VBox root = new VBox(8,
                txtRoom, txtType, txtPrice, txtGuest,
                btnAdd, btnView, btnSearch, btnToggle, btnDelete,
                table, status
        );

        Scene scene = new Scene(root, 500, 600);

        stage.setTitle("Room Booking System");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}