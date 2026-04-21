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

/* ------------------ DSE-C Question ---------------------------------------- */

import javafx.application.Application;
import javafx.collecions.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class Q16 extends Application {
    static class Pair<T, U> {
        T first;
        U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    static class Appointment {
        String id, name, doctor, date, time;
        Appointment(String id, String name, String doc, String date, String time) {
            this.id = id;
            this.name = name;
            this.doctor = doc;
            this.date = date;
            this.time = time;
        }
    }

    HashMap<String, Pair<String, Appointment>> map = new HashMap<>();
    @Override
    public void start(Stage stage) {
        TextField id = new TextField();
        id.setPromptText("Patient ID");

        TextField name = new TextField();
        name.setPromptText("Patient Name");

        TextField doctor = new TextField();
        doctor.setPromptText("Doctor Name");

        TextField date = new TextField();
        date.setPromptText("Date");

        TextField time = new TextField();
        time.setPromptText("Time");

        TextArea output = new TextArea();

        Button add = new Button("Register");
        Button view = new Button("View All");
        Button searchId = new Button("Search by ID");
        Button searchDoc = new Button("Search by Doctor");
        Button cancel = new Button("Cancel");

        add.setOnAction(e -> {
            try {
                if(id.getText().isEmpty() || name.getText().isEmpty() || doctor.getText().isEmpty() || date.getText().isEmpty() || time.getText().isEmpty()) throw new Exception("Fill all Fields");
                if(map.containsKey(id.getText())) {
                    throw new Exception("Already exists");
                }

                Appointment a = new Appointment(id.getText(),
                    name.getText(),
                    doctor.getText(),
                    date.getText(),
                    time.getText()
                );

                map.put(id.getText(), new Pair<>(name.getText(), a));
                output.setText("Appointment Added");
            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Appointment> p : map.values()) {
                Appointment a = p.second;
                sb.append(a.id + " | " + p.first + " | " + a.doctor + " | " + a.date + " | " + a.time + "\n");
            }
            output.setText(sb.toString());
        });

        searchId.setOnAction(e -> {
            Pair<String, Appointment> p = map.get(id.getText());
            if (p != null) {
                Appointment a = p.second;
                output.setText(p.first + " | " + a.doctor + " | " + a.date + " | " + a.time);
            } else {
                output.setText("Not found");
            }
        });

        searchDoc.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Appointment> p : map.values()) {
                Appointment a = p.second;
                if (a.doctor.equals(doctor.getText())) {
                    sb.append(p.first + " | " + a.date + " | " + a.time + "\n");
                }
            }
            output.setText(sb.length() == 0 ? "No appointments" : sb.toString());
        });

        cancel.setOnAction(e -> {
            try {
                if (!map.containsKey(id.getText()))
                    throw new Exception("Not found");

                map.remove(id.getText());
                output.setText("Cancelled");

            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        VBox root = new VBox(10,
                id, name, doctor, date, time,
                add, view, searchId, searchDoc, cancel,
                output
        );

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Hospital System");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}