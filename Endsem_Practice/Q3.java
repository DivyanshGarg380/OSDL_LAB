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
    Synchronized room booking with wait/notify + JavaFX

    - Implement a hotel room booking monitor in Java where a shared RoomMonitor class has a fixed number of available rooms (say 2). 
    - Write a bookRoom() synchronized method that calls wait() when no rooms are free, and a checkoutRoom() synchronized method that calls notify() when a room is released.
    - Spawn five customer threads that each try to book a room, stay for a random time, and then check out. 
    - Demonstrate that no more than two rooms are occupied at once. 
    - Wrap this in a JavaFX application where a TextArea logs all booking and checkout events updated via Platform.runLater(), and a Button labeled 'Start Simulation' launches the threads.
*/

import java.scene.control.TextArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

class RoomMonitor {
    private int availableRooms = 2;
    private TextArea log;

    public RoomMonitor(TextArea log) {
        this.log = log;
    }

    synchronized void bookRoom(String name) {
        while(availableRooms == 0) {
            logIt(name + " is waiting for a room...\n");
            try {
                wait();
            } catch (Exception e) { System.out.println("Thread Interruped");}
        }

        availableRooms--;
        logIt(name + " booked a room. Available rooms: " + availableRooms + "\n");
    }

    synchronized void checkout(String name) {
        availableRooms++;
        logIt(name + " checked out. Available rooms: " + availableRooms + "\n");
        notify();
    }

    void logIt(String message) {
        Platform.runLater(() -> log.appendText(message));
    }
}

class Customer implements Runnable {
    private RoomMonitor monitor;
    private String customerName;

    Customer(RoomMonitor monitor, String customerName) {
        this.monitor = monitor;
        this.customerName = customerName;
    }

    @Override
    public void run() {
        monitor.bookRoom(customerName);
        try{
            int time = 1000 + (int)(Math.random() * 2000);
            Thread.sleep(time);
        } catch(InterruptedException e)  { System.out.println("Sleep interruped"); }

        monitor.checkout(customerName);
    }
}

public class Q3 extends Application {
    @Override
    public void start(Stage stage) {
        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        Button start = new Button("Start Simulation");
        start.setOnAction(e -> {
            logArea.clear();
            RoomMonitor monitor = new RoomMonitor(logArea);
            for(int i = 1; i <= 5; ++i) {
                Thread t = new Thread(new Customer(monitor, "Customer-" + i));
                t.start();
            }
        });

        VBox root = VBox(10, logArea, start);
        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Hotel Room Booking Monitor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}