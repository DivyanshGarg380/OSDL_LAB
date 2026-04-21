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
    Implement a Student Record Management System using JavaFX where:
    Functionalities:
        - Add Student (ID, Name, Marks)
        - Save all students to file using Serialization
        - Load students from file using Deserialization
        - Search student by ID
        - View all students
        - Requirements:
            - Use Generics (Pair<T,U>)
            - Use ArrayList for storage
            - Use Serialization for file handling
            - JavaFX GUI
            - Exception handling
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;

public class Q18 extends Application {
    static class Pair<T, U> {
        T first;
        U second;
        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    static class Student implements Serializable {
        String id, name;
        double marks;

        Student(String id, String name, double marks) {
            this.id = id;
            this.name = name;
            this.marks = marks;
        }
    }

    ArrayList<Pair<String, Student>> list = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        TextField id = new TextField(); id.setPromptText("ID");
        TextField name = new TextField(); name.setPrompText("Name");
        TextField marks = new TextField(); marks.setPromptText("Marks");
        TextArea output = new TextArea();

        Button add = new Button("Add");
        Button save = new Button("Save");
        Button load = new Button("Load");
        Button search = new Button("Search");
        Button view = new Button("View All");

        add.setOnAction(e -> {
            try {
                if (id.getText().isEmpty() || name.getText().isEmpty() || marks.getText().isEmpty())
                    throw new Exception("Fill all");

                double m = Double.parseDouble(marks.getText());
                Student s = new Student(id.getText(), name.getText(), m);
                list.add(new Pair<>(id.getText(), s));
            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        save.setOnAction(e -> {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.dat"));
                oos.writeObject(list);
                oos.close();
                output.setText("saved");
            } catch (Exception ex) { output.setText("Error Saving"); }
        });

        load.setOnAction(e -> {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.dat"));
                list = (ArrayList<Pair<String, Student>>) ois.readObject();
                ois.close();
                output.setText("Added"); 
            } catch (Exception ex) { output.setText("Error Loading!"); }
        });

        search.setOnAction(e -> {
            for(Pair<String, Student> p : list) { 
                if(p.first.equals(id.getText())) {
                    Student s = p.second;
                    output.setText(s.name + " | " + s.marks);
                    return;
                }
            }
            outut.setText("Not Found!");
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Student> p : list) {
                Student s = p.second;
                sb.append(s.id + " | " + s.name + " | " + s.marks + "\n");
            }
            output.setText(sb.toString());
        });

        VBox root = new VBox(10,
                id, name, marks,
                add, save, load, search, view,
                output
        );

        Scene scene = new Scene(root, 400, 450);
        stage.setScene(scene);
        stage.setTitle("Student System");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
