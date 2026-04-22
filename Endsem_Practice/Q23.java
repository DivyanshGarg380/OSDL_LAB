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
    Implement a Student Course & Fee Management System using JavaFX where:

    Functionalities:
        - Add Student (ID, Name, Course, Fee)
        - Prevent duplicate IDs
        - Save all records using Serialization
        - Load records using Deserialization
        - Search student by ID
        - Display all students
        - Sort students by Fee
    
    - Requirements:
        - Use Generics (Pair<T,U>)
        - Use:
            - HashMap (ID → Student) for fast search
            - ArrayList for sorting
            - JavaFX GUI
            - Proper exception handling
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Q23 extends Application {
    static class Pair<T, U> implements Serializable {
        T first;
        U second;

        Pair(T f, U s) {
            first = f;
            second = s;
        }
    }

    static class Student implements Serializable {
        String id, name, course;
        double fee;

        Student(String id, String name, String course, double fee) {
            this.id = id;
            this.name = name;
            this.course = course;
            this.fee = fee;
        }
    }

    HashMap<String, Pair<String, Student>> map = new HashMap<>();

    @Override
    public void start(Stage stage) {
        TextField id = new TextField(); id.setPromptText("Student ID");
        TextField name = new TextField(); name.setPromptText("Name");
        TextField course = new TextField(); course.setPromptText("Course");
        TextField fee = new TextField(); fee.setPromptText("Fee");

        TextArea output = new TextArea();

        Button add = new Button("Add");
        Button save = new Button("Save");
        Button load = new Button("Load");
        Button search = new Button("Search");
        Button view = new Button("View All");
        Button sort = new Button("Sort by Fee");

        add.setOnAction(e -> {
            try {
                if (id.getText().isEmpty() || name.getText().isEmpty() ||
                        course.getText().isEmpty() || fee.getText().isEmpty())
                    throw new Exception("Fill all");

                if (map.containsKey(id.getText()))
                    throw new Exception("Duplicate ID");

                double f = Double.parseDouble(fee.getText());

                Student s = new Student(id.getText(), name.getText(), course.getText(), f);
                map.put(id.getText(), new Pair<>(name.getText(), s));

                output.setText("Added");

            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        save.setOnAction(e -> {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"));
                oos.writeIbject(map);
                oos.close();
                output.setText("Saved");
            } catch (Exception ex) { output.setText("Error Saving"); }
        });

        load.setOnAction(e -> {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"));
                map = (HashMap<String, Pair<String, Student>>) ois.readObject();
                ois.close();
                output.setText("Loaded");

            } catch (Exception ex) {
                output.setText("Error loading");
            }
        });

        search.setOnAction(e -> {
            Pair<String, Student> p = map.get(id.getText());

            if(p != null) {
                Student s = p.second;
                output.setText(s.name + " | " + s.course + " | " + s.fee);
            } else {
                output.setText("Not Found");
            }
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Student> p : map.values()) {
                Student s = p.second;
                sb.append(s.id + " | " + s.name + " | " + s.course + " | " + s.fee + "\n");
            }
            output.setText(sb.toString());
        });
        
        sort.setOnAction(e -> {
            ArrayList<Pair<String, Student>> list = new ArrayList<>(map.values());
            Collections.sort(list, (a, b) -> 
                Double.compare(a.second.fee, b.second.fee)
            );

            StringBuilder sb = new StringBuilder();
            for(Pair<String, Student> p : list) {
                Student s = p.second;
                sb.append(s.id + " | " + s.name + " | " + s.course + " | " + s.fee + "\n");
            }

            output.setText(sb.toString());
        });

        VBox root = new VBox(10,
                id, name, course, fee,
                add, save, load, search, view, sort,
                output
        );

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Student Fee System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}   