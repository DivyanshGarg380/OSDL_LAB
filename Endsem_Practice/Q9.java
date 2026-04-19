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
    RandomAccessFile room manager with JavaFX

    - Design a JavaFX application that manages hotel room records using RandomAccessFile with fixed-length records. 
    - Each record stores roomNumber (int, 4 bytes), roomType (20-char padded String, 40 bytes), pricePerNight (double, 8 bytes), and isBooked (boolean, 1 byte) — total 53 bytes per record. 
    - Provide GUI buttons for 'Add Room' (reads TextFields and writes a new record), 'View Room' (takes a room number, uses seek() to jump to the correct byte offset, and displays details in Labels), and 'Toggle Booking' (updates only the isBooked byte of a specific record using seek). 
    - Show error messages via JavaFX Alert dialogs for invalid input or file errors.
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import jaafx.scene.contro.*;
import java.io.*;

public class Q9 extends Application {
    static class FileUtil {
        static final int SIZE = 53;

        static void writeFixedString(String str, RandomAccessFile raf) throws IOException {
            StringBuilder sb = new StringBuilder(str);
            sb.setLength(20);

            for(int i = 0; i < 20; ++i) raf.writeChar(sb.charAt(i));
        }

        static String readFixedString(RandomAccessFile raf) throws IOException {
            char[] chars = new char[20];
            for(int i = 0; i < 20; ++i) chars[i] = raf.readChar();
            return new String(chars).trim();
        }

        static void addRoom(int num, String type, double price, boolean booked) throws IOException {
            RandomAccessFile raf = new RandomAccessFile("room.dat", "rw");
            raf.seek(raf.length());

            raf.writeInt(num);
            writeFixedString(type, raf);
            raf.writeDouble(price);
            raf.writeBoolean(booked);
            raf.close();
        }

        static long findRoom(int num) throws IOException {
            RandomAccessFile raf = new RandomAccessFile("room.dat", "r");
            long pos = 0;
            while(raf.getFilePointer() < raf.length()) {
                int rno = raf.readInt();
                if(rno == num) {
                    raf.close();
                    return pos;
                }

                raf.skipBytes(49);
                pos += SIZE;
            }
            raf.close();
            return -1;
        }

        static String viewRoom(int roomNo) throws IOException {
            RandomAccessFile raf = new RandomAccessFile("rooms.dat", "r");

            long pos = findRoom(roomNo);

            if (pos == -1) {
                raf.close();
                return "Room not found";
            }

            raf.seek(pos);

            int rno = raf.readInt();
            String type = readFixedString(raf);
            double price = raf.readDouble();
            boolean booked = raf.readBoolean();

            raf.close();
            return "Room: " + rno +
                   "\nType: " + type +
                   "\nPrice: " + price +
                   "\nBooked: " + booked;
        }

        static void toggleBooking(int num) throws IOException {
            RandomAccessFile raf = new RandomAccessFile("room.dat", "rw");
            long pos = findRoom(num);
            if(pos == -1) {
                raf.close();
                throw new IOException("Room not found");
            }

            raf.seek(pos + 52);
            boolean curr = raf.readBoolean();
            raf.seek(pos + 52);
            raf.writeBoolean(!curr);
            raf.close();
        }
    }

    static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        TextField txtRoom = new TextField(); txtRoom.setPromptText("Room Number");
        TextField txtType = new TextField(); txtType.setPromptText("Room Type");
        TextField txtPrice = new TextField(); txtPrice.setPromptText("Price");

        Label output = new Label();

        Button btnAdd = new Button("Add Room");
        Button btnView = new Button("View Room");
        Button btnToggle = new Button("Toggle Booking");

        btnAdd.setOnAction(e -> {
            try {
                int r = Integer.parseInt(txtRoom.getText());
                double p = Double.parseDouble(txtPrice.getText());

                FileUtil.addRoom(r, txtType.getText(), p, false);

                output.setText("Room Added");

            } catch (Exception ex) {
                showError("Invalid Input / File Error");
            }
        });

        btnView.setOnAction(e -> {
            try {
                int r = Integer.parseInt(txtRoom.getText());

                String data = FileUtil.viewRoom(r);

                output.setText(data);

            } catch (Exception ex) {
                showError("Error reading file");
            }
        });

        btnToggle.setOnAction(e -> {
            try {
                int r = Integer.parseInt(txtRoom.getText());

                FileUtil.toggleBooking(r);

                output.setText("Booking toggled");

            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        VBox root = new VBox(10,
                txtRoom, txtType, txtPrice,
                btnAdd, btnView, btnToggle,
                output
        );

        Scene scene = new Scene(root, 400, 350);

        stage.setTitle("Room Manager (RandomAccessFile)");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
