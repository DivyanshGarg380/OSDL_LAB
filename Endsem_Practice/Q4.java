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
    Employee payroll with generics, collections, and JavaFX

    - Create a generic class Pair with two type parameters to store an employee ID (Integer) and their monthly salary (Double).
    - Build an ArrayList of such Pair objects for at least five employees. 
    - Write a generic static method double computeTotal(ArrayList> list) using a bounded type to sum all salary values. 
    - Use an Iterator to traverse the list and display each employee record. 
    - Build a JavaFX GUI with a TableView showing employee ID and salary columns, a Label showing the total payroll computed by your generic method, and a Button 'Load Data' that populates the table.
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import javafx.collections.*;
import java.util.*;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

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

class Employee {
    private Integer id;
    private Double salary;

    Employee(Integer id, Double salary) {
        this.id = id;
        this.salary = salary;
    }

    public Integer getId() { return id; }
    public Double getSalary() { return salary; }
}

class PayrollUtil {
    public static <T extends Number> double computeTotal(ArrayList<Pair<Integer, T>> list) {
        double total = 0;
        for(Pair<Integer, T> p : list) {
            total += p.getSecond().doubleValue();
        } 
        return total;
    }
}

public class Q4 extends Application {
    @Override
    public void start(Stage stage) {
        TableView<Employee> table = new TableView<>();
        TableColumn<Employee, Integer> colId = new TableColumn<>("Employee ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Employee, Double> colSalary = new TableColumn<>("Salary");
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        table.getColumns().addAll(colId, colSalary);

        Label totalLabel = new Label("Total Payroll: ");
        Button loadBtn = new Button("Load Data");

        loadBtn.setOnAction(e -> {

            ArrayList<Pair<Integer, Double>> list = new ArrayList<>();

            list.add(new Pair<>(101, 50000.0));
            list.add(new Pair<>(102, 60000.0));
            list.add(new Pair<>(103, 55000.0));
            list.add(new Pair<>(104, 70000.0));
            list.add(new Pair<>(105, 65000.0));

            Iterator<Pair<Integer, Double>> itr = list.iterator();

            ObservableList<Employee> data = FXCollections.observableArrayList();

            while (itr.hasNext()) {
                Pair<Integer, Double> p = itr.next();
                data.add(new Employee(p.getFirst(), p.getSecond()));
            }

            table.setItems(data);

            double total = PayrollUtil.computeTotal(list);
            totalLabel.setText("Total Payroll: " + total);
        });

        VBox root = new VBox(10, table, loadBtn, totalLabel);

        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Employee Payroll");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



