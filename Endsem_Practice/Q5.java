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

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.cell.propertyValueFactory;
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

class Appointment {
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

