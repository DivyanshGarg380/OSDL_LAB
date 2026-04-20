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
    Implement a Student Course Registration System using Java 

    The application should include a JavaFX-based GUI and support the following functionalities:

    1) Register Student
        - Allow users to register a student for a course with details such as:
            • Student ID
            • Student Name
            • Course Name
        - Ensure that a student cannot register for more than one course.

    2) Unregister Student
        - Allow users to remove a student’s registration from a course
            using Student ID or any unique identifier.

    3) View All Registrations
        - Display all student-course registrations in a structured format.

    4) Search by Student → Course
        - Retrieve and display the course registered by a specific student
        using Student ID.

    5) Search by Course → Students
        - Retrieve and display all students enrolled in a specific course.

    6) Exception Handling
        - Implement proper exception handling for:
            • Invalid inputs
            • Missing data
            • Duplicate registrations (same student registering again)
            • Runtime errors

    Requirements:
        - Use Java Generics where applicable.
        - Use Java Collection Framework (e.g., ArrayList, HashMap).
        - Integrate all functionalities within a JavaFX GUI.
        - Ensure user-friendly interface and proper validation.
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;  
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.*;
import java.util.*;

public class Q10 extends Application {
    static class Pair<T, U> {
        private T first;
        private U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() { return first; }
        public U getSecond() { return second; }
    }

    public static class Registration {
        private Integer id;
        private String studentName;
        private String courseName;

        public Registration(Integer id, String name, String course) {
            this.id = id;
            this.studentName = name;
            this.courseName = course;
        }

        public Integer getId() { return id; }
        public String getStudentName() { return studentName; }
        public String getCourseName() { return courseName; }
    }

    static ArrayList<Registration> list = new ArrayList<>();
    static HashMap<Integer, Registration> mp = new HashMap<>();

    @Override
    public void start(Stage stage) {
        TextField tid = new TextField(); tid.setPromptText("Student ID");
        TextField tname = new TextField(); tname.setPromptText("Student Name");
        TextField tcourse = new TextField(); tcourse.setPromptText("Course Name");

        Button register = new Button("Register");
        Button unregister = new Button("Unregister");
        Button view = new Button("View All");
        Button searchId = new Button("Search Student");
        Button searchCourse = new Button("Search Course");

        Label status = new Label();

        TableView<Registration> table = new TableView<>();

        TableColumn<Registration, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Registration, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<Registration, String> c3 = new TableColumn<>("Course");
        c3.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        table.getColumns().addAll(c1, c2, c3);

        register.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tid.getText());
                if(mp.containsKey(id)) {
                    throw new Exception("Student already registered");
                }

                String name = tname.getText();
                String course = tcourse.getText();

                if(name.isEmpty() || course.isEmpty()) throw new Exception("Missing Data!");

                Registration r = new Registration(id, name, course);
                list.add(r);
                mp.put(id, r);
                status.setText("Registered Sucessfully");

            } catch (Exception ex) { status.setText(ex.getMessage()); }
        });

        unregister.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tid. getText());

                Registration r = mp.remove(id);
                if(r == null) throw new Exception("Student not found");

                list.remove(r);
                status.setText("Unregistered");
            } catch (Exception ex) { status.setText("Error: " + ex.getMessage()); }
        });

        view.setOnAction(e -> {
            table.setItems(FXCollections.observableArrayList(list));
        });

        searchId.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tid.getText());
                Registration r = mp.get(id);
                if(r != null) {
                    table.setItems(FXCollections.observableArrayList(r));
                    status.setText("Found");
                } else {
                    status.setText("Not Found");
                }

            } catch (Exception ex) { status.setText("Invalid ID"); }
        });

        searchCourse.setOnAction(e -> {
            String course = tcourse.getText();
            ArrayList<Registration> res = new ArrayList<>();
            for(Registration r : list) {
                if(r.getCourseName().equalsIgnoreCase(course)) res.add(r);
            }

            table.setItems(FXCollections.observableArrayList(res));
        });

        VBox root = new VBox(8, tid, tname, tcourse, register, unregister, view, searchId, searchCourse, table, status);
        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Student Course Registration");
        stage.setScene(scene);
        stage.show();
    }   

    public static void main(String[] args) {
        launch(args);
    }
}