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
    Serializable room bookings with JavaFX search

    - Create a Room class that implements Serializable with fields roomNumber (int), roomType (String), pricePerNight (double), bookingStatus (boolean), and guestName (String). 
    - Write code to serialize a list of five Room objects to a file called rooms.dat. 
    - Write deserialization code to read them back. 
    - Build a JavaFX GUI with a TextField for entering a room number, a Button 'Search', and a TextArea that displays the matching room's details after deserializing from the file. 
    - Also provide a 'Show All' Button that lists all deserialized rooms in the TextArea. 
    - Handle IOException and ClassNotFoundException with appropriate error labels.
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.io.*;
import java.util.*;

public class Q7 extends Application {
    static class Room implements Serializable {
        int number;
        String type;
        double price;
        boolean status;
        String name;

        Room(int number, String type, double price, boolean status, String name) {
            this.number = number;
            this.type = type;
            this.price = price;
            this.status = status;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Room: " + number + ", Type: " + type + 
                ", price: " + price + ", Booked: " + status +
                ", Guest: " + name + "\n";
        }
    }

    static class FileUtil {
        static void writeData() throws IOException {
            ArrayList<Room> list = new ArrayList<>();
            list.add(new Room(101, "Single", 2000, true, "Amit"));
            list.add(new Room(102, "Double", 3000, false, ""));
            list.add(new Room(103, "Deluxe", 4000, true, "Ravi"));
            list.add(new Room(104, "Suite", 6000, false, ""));
            list.add(new Room(105, "Single", 2500, true, "Neha"));

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("rooms.dat"));
            oos.writeObject(list);
            oos.close();
        }

        static ArrayList<Room> readData() throws IOException, ClassNotFoundException {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("rooms.dat"));

            ArrayList<Room> list = (ArrayList<Room>) ois.readObject();
            ois.close();
            return list;
        }
    }

    @Override
    public void start(Stage stage) {
        TextField txtRoom = new TextField();
        txtRoom.setPromptText("Enter Room Number");

        Button search = new Button("Search");
        Button showAll = new Button("Show All");

        TextArea output = new TextArea();
        Label status = new Label();

        try {
            FileUtil.writeData();
        } catch (IOException e) { status.setText("Write Error"); }

        search.setOnAction(e -> {
            try {
                int num = Integer.parseInt(txtRoom.getText());
                ArrayList<Room> list = FileUtil.readData();

                boolean found = false;
                for(Room r : list) {
                    if(r.number == num) {
                        output.setText(r.toString());
                        found = true;
                        break;
                    }
                }

                if(!found) {
                    output.setText("Room not Found");
                }
            } catch(IOException ex) {
                status.setText("IO Error: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                status.setText("Class Error");
            } catch (Exception ex) {
                status.setText("Invalid Input");
            }
        });

        showAll.setOnAction(e -> {
            try {
                ArrayList<Room> list = FileUtil.readData();
                StringBuilder sb = new StringBuilder();

                for(Room r : list) {
                    sb.append(r.toString());
                }

                output.setText(sb.toString());
            } catch (IOException ex) {
                status.setText("IO Error");
            } catch (ClassNotFoundException ex) {
                status.setText("Class Error");
            }
        });

        VBox root = new VBox(10, txtRoom, search, showAll, output, status);

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Room Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}