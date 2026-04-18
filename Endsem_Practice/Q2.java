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
    Concurrent room service with GUI status — Threads + JavaFX

    - Build a JavaFX application that simulates three simultaneous hotel services: room cleaning, food delivery, and maintenance. 
    - Create a separate thread for each service using the Runnable interface. 
    - Each thread should print five progress steps with a 500ms sleep between steps. 
    - Use join() on all three threads so the main thread waits for all services to finish before printing 'All services completed.' 
    - The JavaFX window should have three TextArea boxes (one per service) displaying progress updates in real time using Platform.runLater(), and a single Start button that launches all three threads when clicked.
*/

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.contol.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class ServiceTask implements Runnable {
    private String name;
    private TextArea output;

    ServiceTask(String name, TextArea output) {
        this.name = name;
        this.output = output;
    }

    @Override
    public void run() {
        for(int i = 1; i <= 5; ++i) {
            int step = i;

            Platform.runLater(() -> {
                output.appendText(name + " - Step " + step + "\n");
            });

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }
}

public class Q2 extends Application {
    @Override
    public void start(Stage stage) {
        TextArea cleaning = new TextArea();
        cleaning.setPromptText("Room Cleaning");

        TextArea food = new TextArea();
        food.setPromptText("Food Delivery");

        TextArea maintenance = new TextArea();
        maintenance.setPromptText("Maintenance");

        Button start = new Button("Start");
        Label status = new Label();

        start.setOnAction(e -> {
            cleaning.clear();
            food.clear();
            maintenance.clear();
            status.setText("");

            Thread t1 = new Thread(new ServiceTask("Cleaning", cleaning));
            Thread t2 = new Thread(new ServiceTask("Food Delivery", food));
            Thread t3 = new Thread(new ServiceTask("Maintenance", maintenance));

            t1.start();
            t2.start();
            t3.start();

            new Thread(() -> {
                try {
                    t1.join();
                    t2.join();
                    t3.join();

                    Platform.runLater(() -> {
                        status.setText("All services completed.");
                    });
                } catch (InterruptedException e) {
                    System.out.println("Join interrupted");
                }
            }).start();
        });

        VBox root = new VBox(10, cleaning, food, maintenance, start, status);
        Scene scene = new Scene(root, 400, 500);

        stage.setTitle("Hotel Services");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
