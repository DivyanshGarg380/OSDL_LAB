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

/* ------------------ CCE-C Question ---------------------------------------- */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class Real_Practice_From_Here extends Application {
    static class Pair<T, U> {
        T first;
        U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    HashMap<String, Pair<String, String>> map = new HashMap<>();
    @Override
    public void start(Stage stage) {
        TextField id = new TextField();
        id.setPromptText("Student ID");

        TextField name = new TextField();
        name.setPromptText("Student Name");

        TextField course = new TextField();
        course.setPromptText("Course");

        TextArea output = new TextArea();
        Button register = new Button("Register");
        Button unregister = new Button("Unregister");
        Button view  = new Button("View");
        Button searchId = new Button("Seach Student");
        Button searchCourse = new Button("Search Course");

        register.setOnAction(e -> {
            try {
                String tid = id.getText();
                String tname = name.getText();
                String tcourse = course.getText();

                if(tid.isEmpty() || tname.isEmpty() || tcourse.isEmpty()) throw new Exception("Fill all fields");
                if(map.containsKey(tid)) throw new Exception("Student already registered");
                map.put(tid, new Pair<>(tname, tcourse));
                output.setText("Registered Successfully");
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        unregister.setOnAction(e -> {
            try {
                String tid = id.getText();
                if(!map.containsKey(tid)) throw new Exception("Student not found");
                map.remove(tid);
                output.setText("Unregistered Successfully");
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for(String x : map.keySet()) {
                Pair<String, String> p = map.get(x);
                sb.append(x + " | " + p.first + " | " + p.second + "\n");
            }
            output.setText(sb.toString());
        });

        searchId.setOnAction(e -> {
            String tid = id.getText();
            if(map.containsKey(tid)) {
                Pair<String, String> p = map.get(tid);
                output.setText("course: " + p.second);
            } else {
                output.setText("Student not found");
            }
        });

        searchCourse.setOnAction(e -> {
            String tcourse = course.getText();
            StringBuilder sb = new StringBuilder();

            for (String x : map.keySet()) {
                Pair<String, String> p = map.get(x);
                if (p.second.equals(tcourse)) {
                    sb.append(p.first + "\n");
                }
            }

            output.setText(sb.length() == 0 ? "No students found" : sb.toString());
        });

        VBox root = new VBox(10, id, name, course, register, unregister, view, searchId, searchCourse, output);
        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Student Course System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
