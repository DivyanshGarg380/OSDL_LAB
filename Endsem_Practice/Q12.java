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
    Thread-safe bank account with synchronization and JavaFX

    - Implement a BankAccount class with a balance field and two synchronized methods: deposit(int amount) and withdraw(int amount). 
    - Withdraw should print an insufficient balance message and return without changing balance if funds are too low. 
    - Create four customer threads — two depositing and two withdrawing — all operating on the same account object. 
    - Use a synchronized block (not a synchronized method) in at least one of the four thread classes to demonstrate the difference. 
    - Build a JavaFX TextArea log that shows each thread's action and the balance after each operation, updated via Platform.runLater().
*/

import java.awt.TextArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Q12 extends Application {
    public static class BankAccount {
        private int balance = 1000;
        private TextArea log;

        public BankAccount(TextArea log) {
            this.log = log;
        }

        public synchronized void deposit(int amount, String name) {
            balance += amount;
            log(name + " deposited " + amount + " | Balance: " + balance);
        }

        public synchronized void withdraw(int amount, String name) {
            if(balance < amount) {
                log(name + " tried to withdraw " + amount + " | Insufficient Balance!");
                return;
            }

            balance -= amount;
            log(name + " withdrew " + amount + " | Balance: " + balance);
        }

        private void log(String msg) {
            Platform.runLater(() -> log.appendText(msg + "\n"));
        }
    }

    public static class DepositThread implements Runnable {
        private BankAccount acc;
        private String name;

        DepositThread(BankAccount acc, String name) {
            this.acc = acc;
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {
                acc.deposit(500, name);
                try { Thread.sleep(500); } catch (Exception e) {}
            }
        }
    }

    public static class WithdrawThread implements Runnable {
        private BankAccount acc;
        private String name;

        WithdrawThread(BankAccount acc, String name) {
            this.acc = acc;
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {

                synchronized (acc) {
                    acc.withdraw(700, name);
                }

                try { Thread.sleep(500); } catch (Exception e) {}
            }
        }
    }

    @Override
    public void start(Stage stage) {

        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        Button startBtn = new Button("Start");

        startBtn.setOnAction(e -> {

            logArea.clear();

            BankAccount account = new BankAccount(logArea);

            Thread t1 = new Thread(new DepositThread(account, "Depositor-1"));
            Thread t2 = new Thread(new DepositThread(account, "Depositor-2"));
            Thread t3 = new Thread(new WithdrawThread(account, "Withdrawer-1"));
            Thread t4 = new Thread(new WithdrawThread(account, "Withdrawer-2"));

            t1.start();
            t2.start();
            t3.start();
            t4.start();
        });

        VBox root = new VBox(10, logArea, startBtn);

        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("Bank Account Simulation");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}