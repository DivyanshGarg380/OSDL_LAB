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
    Implement a Hospital Appointment System using Java (Generics + Collection Framework) fully integrated with JavaFX.

    - The application should include a JavaFX-based GUI and support the following functionalities:
        - 1) Register Appointment  
            - Allow users to add a new appointment with details such as Patient ID, Patient Name, Doctor Name, Date, and Time.

        - 2) View All Appointments  
            - Display all stored appointments in a structured format (e.g., TableView).

        - 3) Search Appointment by Patient ID  
            - Retrieve and display appointment details based on a given Patient ID.

        - 4) Search Appointment by Doctor Name  
            - Retrieve and display all appointments associated with a specific doctor.

        - 5) Cancel Appointment  
            - Remove an appointment using Patient ID or any unique identifier.

        - 6) Exception Handling  
            - Implement proper exception handling for invalid inputs, missing data, and runtime errors.

    Requirements:
    - Use Java Generics where applicable.
    - Use Java Collection Framework (e.g., ArrayList, HashMap).
    - Integrate all functionalities within a JavaFX GUI.
    - Ensure user-friendly interface and proper validation.
*/

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.collections.*;
import java.util.*;

class Pair<T, U> {
    private T first;
    private U second;

    Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
}

public class Q5 extends Application {

    static class Appointment {  
        private Integer patientId;
        private String patientName;
        private String doctorName;
        private String date;
        private String time;

        Appointment(Integer id, String name, String dname, String date, String time) {
            this.patientId = id;
            this.patientName = name;
            this.doctorName = dname;
            this.date = date;
            this.time = time;
        }

        public Integer getPatientId() { return patientId; }
        public String getPatientName() { return patientName; }
        public String getDoctorName() { return doctorName; }
        public String getDate() { return date; }
        public String getTime() { return time; }
    }
    
    ArrayList<Appointment> list = new ArrayList<>();
    HashMap<Integer, Appointment> map = new HashMap<>();

    @Override
    public void start(Stage stage) {
        TextField id = new TextField(); id.setPromptText("Patient ID");
        TextField name = new TextField(); name.setPromptText("Patient Name");
        TextField doc = new TextField(); doc.setPromptText("Doctor Name");
        TextField date = new TextField(); date.setPromptText("Date");
        TextField time = new TextField(); time.setPromptText("Time");

        Button add = new Button("Register");
        Button view = new Button("View All");
        Button searchId = new Button("Search by ID");
        Button searchDoc = new Button("Search by Doctor");
        Button delete = new Button("Cancel");

        Label status = new Label();

        TableView<Appointment> table = new TableView<>();

        TableColumn<Appointment, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<Appointment, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<Appointment, String> c3 = new TableColumn<>("Doctor");
        c3.setCellValueFactory(new PropertyValueFactory<>("doctorName"));

        TableColumn<Appointment, String> c4 = new TableColumn<>("Date");
        c4.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Appointment, String> c5 = new TableColumn<>("Time");
        c5.setCellValueFactory(new PropertyValueFactory<>("time"));

        table.getColumns().addAll(c1, c2, c3, c4, c5);

        add.setOnAction(e -> {
            try {
                int tid = Integer.parseInt(id.getText());
                Appointment a = new Appointment (
                    tid,
                    name.getText(),
                    doc.getText(),
                    date.getText(),
                    time.getText()
                );

                list.add(a);
                map.put(tid, a);

                status.setText("Appointment Added");
            } catch (Exception ex) { status.setText("Invalid Input!"); }
        });

        view.setOnAction(e -> {
            ObservableList<Appointment> data = FXCollections.observableArrayList(list);
            table.setItems(data);
        }); 

        searchId.setOnAction(e -> {
            try {
                int tid = Integer.parseInt(id.getText());

                Appointment a = map.get(tid);
                if(a != null) {
                    table.setItems(FXCollections.observableArrayList(a));
                    status.setText("Found");
                } else {
                    status.setText("Not found");
                }

            } catch(Exception ex) { status.setText("Invalid ID"); }
        });

        searchDoc.setOnAction(e -> {
            String tdoc = doc.getText();

            ArrayList<Appointment> res = new ArrayList<>();
            for(Appointment a : list) {
                if(a.getDoctorName().equalsIgnoreCase(tdoc)) res.add(a);
            }

            table.setItems(FXCollections.observableArrayList(res));
        });

        delete.setOnAction(e -> {
            try {
                int tid = Integer.parseInt(id.getText());

                Appointment a = map.remove(tid);
                list.remove(a);

                status.setText("Appointment Cancelled");
            } catch (Exception ex) { status.setText("Error in Cancel"); }
        });

        VBox root = new VBox(8, id, name, doc, date, time, add, view, searchId, searchDoc, delete, table,status);

        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Hospital Appointment System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}