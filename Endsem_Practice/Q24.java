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
    Implement a Task Management System using JavaFX where:
    - Functionalities:
        - Add Task (Task ID, Task Name, Priority)
        - Prevent duplicate Task ID
        - View all tasks
        - Search task by ID
        - Delete task using Iterator
        - Display all high priority tasks
    
    - Requirements:
        - Use Generics (Pair<T,U>)
        - Use ArrayList
        - Use Iterator for deletion
        - JavaFX GUI
        - Exception handling
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class Q24 extends Application {
     static class Pair<T, U> {
        T first;
        U second;

        Pair(T f, U s) {
            first = f;
            second = s;
        }
    }

    static class Task {
        String id, name, priority;

        Task(String id, String name, String priority) {
            this.id = id;
            this.name = name;
            this.priority = priority;
        }
    }

    ArrayList<Pair<String, Task>> list = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        TextField id = new TextField(); id.setPromptText("Task ID");
        TextField name = new TextField(); name.setPromptText("Task Name");
        TextField priority = new TextField(); priority.setPromptText("Priority (High/Low)");

        TextArea output = new TextArea();

        Button add = new Button("Add");
        Button view = new Button("View All");
        Button search = new Button("Search");
        Button delete = new Button("Delete");
        Button high = new Button("High Priority");

        add.setOnAction(e -> {
            try {
                if (id.getText().isEmpty() || name.getText().isEmpty() || priority.getText().isEmpty())
                    throw new Exception("Fill all");

                for(Pair<String, Task> p : list) {
                    if(p.first.equals(id.getText())) throw new Exception("Duplicate ID");
                }

                Task t = new Task(id.getText(), name.getText(), priority.getText());
                list.add(new Pair<>(id.getText(), t));
                output.setText("Task Added!");
            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Task> p : list) {
                Task t = p.second;
                sb.append(t.id + " | " + t.name + " | " + t.priority + "\n");
            }
            output.setText(sb.toString());
        });

        search.setOnAction(e -> {
            for (Pair<String, Task> p : list) {
                if (p.first.equals(id.getText())) {
                    Task t = p.second;
                    output.setText(t.name + " | " + t.priority);
                    return;
                }
            }
            output.setText("Not found");
        });

        delete.setOnAction(e -> {
            try {
                Iterator<Pair<String, Task>> it = list.iterator;
                boolean found = false;
                while(it.hasNext()) {
                    Pair<String, Task> p = it.next();
                    if(p.first.equals(id.getText())) {
                        it.remove();
                        found = true;
                        break;
                    }
                }

                if(!found) throw new Exception("Not Found!");
                output.setText("Deleted");

            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        high.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for(Pair<String, Task> p : list) {
                Task t = p.second;
                if(t.priority.equalsIgnoreCase("High")) {
                    sb.append(t.name + "\n");
                }
            }
            output.setText(sb.length() == 0 ? "No high priority tasks" : sb.toString());
        });

        VBox root = new VBox(10,
                id, name, priority,
                add, view, search, delete, high,
                output
        );

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Task System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
