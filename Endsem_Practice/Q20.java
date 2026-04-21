/*
Author :

███████╗████████╗ █████╗ ██████╗  ███╗   ███╗ █████╗ ███╗   ██╗
██╔════╝╚══██╔══╝██╔══██╗██╔══██╗ ████╗ ████║██╔══██╗████╗  ██║
███████╗   ██║   ███████║██████╔╝ ██╔████╔██║███████║██╔██╗ ██║
╚════██║   ██║   ██╔══██║██║  ██║ ██║╚██╔╝██║██╔══██║██║╚██╗██║
███████║   ██║   ██║  ██║██║  ██║ ██║ ╚═╝ ██║██║  ██║██║ ╚████║
╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝  STARMAN248
*/

/* Code provided for reference only and not intended for direct use. If found, I will trace you down 😈*/

/* ------------------ Self Made Question ---------------------------------------- */

/*
    Implement a Hotel Room Record System using JavaFX where:
    - Functionalities:
        - Add Room (Room No, Type, Price, Status)
        - Store records using RandomAccessFile (fixed size)
        - Search room by Room No (direct access using seek())
        - View all rooms
        - Update booking status (Book/Vacate)
    
    - Requirements:
        - Use Generics (Pair<T,U>)
        - Use RandomAccessFile
        - JavaFX GUI
        - Proper exception handling
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;

public class Q20 extends Application {
    static class Pair<T, U> {
        T first;
        U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    static final String FILE = "rooms.dat";
    static final int NAME_SIZE = 20; // fixing for this question
    static final int RECORD_SIZE = 4 + (NAME_SIZE * 2) + 8 + 1; // int + string + double + boolean

    void writeString(RandomAccessFile raf, String s) throws IOException {
        StringBuilder sb = new StringBuilder(s);
        sb.setLength(NAME_SIZE);
        raf.writeChars(sb.toString());
    }

    String readString(RandomAccessFile raf) throws IOException {
        char[] chars = new char[NAME_SIZE];
        for(int i = 0; i < NAME_SIZE; ++i) chars[i] = raf.readChar();
        return new String(chars).trim();
    }

    @Override
    public void start(Stage stage) {
        TextField roomNo = new TextField(); roomNo.setPromptText("Room No.");
        TextField type = new TextField(); type.setPromptText("Type");
        TextField price = new TextField(); price.setPromptText("Price");
        TextField status = new TextField(); status.setPromptText("Status");

        TextArea output = new TextArea();
        Button add = new Button("Add Room");
        Button search = new Button("Search");
        Button view = new Button("View All");
        Button update = new Button("Update Status");

        add.setOnAction(e -> {
            try {
                RandomAccessFile raf = new RandomAccessFile(FILE, "rw");
                int r = Integer.parseInt(roomNo.getText());
                double p = Double.parseDouble(price.getText());
                boolean st = Boolean.parseBoolean(status.getText());

                raf.seek(raf.length());
                raf.writeInt(r);
                writeString(raf, type.getText());
                raf.writeDouble(p);
                raf.writeBoolean(st);
                raf.close();
                output.setText("Room Added");
            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        search.setOnAction(e -> {
            try {
                RandomAccessFile raf = new RandomAccessFile(FILE, "r");
                int key = Integer.parseInt(roomNo.getText());

                while(raf.getFilePointer() < raf.length()) {
                    int r = raf.readInt();
                    String t = readString(raf);
                    double p = raf.readDouble();
                    boolean st = raf.readBoolean();

                    if(r == key) {
                        output.setText(r + " | " + t + " | " + p + " | " + st);
                        raf.close();
                        return;
                    }
                }

                raf.close();
                output.setText("Not Found!");

            } catch (Exception ex) { output.setText("Error"); }
        });

        view.setOnAction(e -> {
            try {
                RandomAccessFile raf = new RandomAccessFile(FILE, "r");
                StringBuilder sb = new StringBuilder();

                while(raf.getFilePointer() < raf.length()) {
                    int r = raf.readInt();
                    String t = readString(raf);
                    double p = raf.readDouble();
                    boolean st = raf.readBoolean();

                    sb.append(r + " | " + t + " | " + p + " | " + st + "\n");
                }

                raf.close();
                output.setText(sb.toString());
            } catch (Exception ex) { output.setText("Error!"); }
        });

        update.setOnAction(e -> {
            try {
                RandomAccessFile raf = new RandomAccessFile(FILE, "rw");
                int key = Integer.parseInt(roomNo.getText());
                boolean newStatus = Boolean.parseBoolean(status.getText());

                while (raf.getFilePointer() < raf.length()) {
                    long pos = raf.getFilePointer();

                    int r = raf.readInt();
                    readString(raf);
                    raf.readDouble();
                    raf.readBoolean();

                    if (r == key) {
                        raf.seek(pos + 4 + (2 * NAME_SIZE) + 8);
                        raf.writeBoolean(newStatus);

                        raf.close();
                        output.setText("Updated");
                        return;
                    }
                }

                raf.close();
                output.setText("Not found");

            } catch (Exception ex) {
                output.setText("Error");
            }
        });

        VBox root = new VBox(10,
                roomNo, type, price, status,
                add, search, view, update,
                output
        );

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Hotel RAF System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}