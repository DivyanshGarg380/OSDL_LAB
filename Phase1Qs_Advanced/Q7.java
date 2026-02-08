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
    Design and implement a menu-driven Java application to simulate a Restaurant Order Processing System where:
    . A Chef produces food
    . A Waiter waits for food and serves it
    . Orders are processed one at a time
    . All actions are logged to files
    . Create a class OrderCounter with the following data members:
        - FoodItem currentItem
        - boolean foodReady
        - int totalOrdersPrepared
        - double totalRevenue
    . Include methods:
        - prepareFood(FoodItem item)
        - serveFood()
        - displayStats()
    . Create an enum FoodItem with:
        - PIZZA (250)
        - BURGER (150)
        - PASTA (200)
    . Each item should store:
        - Price
        - Method getPrice()

    . 1️⃣ ChefThread (Producer)
        - Takes food orders
        - Prepares food
        - If food is already ready → must WAIT

    . 2️⃣ WaiterThread (Consumer)
        - Waits until food is ready
        - Serves food
        - Updates revenue
    
    . Chef cannot prepare new food if previous food not served
    . Waiter cannot serve if no food is ready
    . Only ONE food item can exist on counter at a time
    . Race condition must be avoided

    . Log every prepared and served item
    . Store final total revenue when program exits
*/

import java.io.*;
import java.util.Scanner;

enum FoodItem {
    PIZZA(250),
    BURGER(150),
    PASTA(200);

    private int price;

    FoodItem(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

class OrderCounter {
    FoodItem currentItem;
    boolean foodReady = false;
    int totalOrdersPrepared = 0;
    double totalRevenue = 0;

    synchronized void prepareFood(FoodItem item) {
        while(foodReady){
            try {
                wait();
            } catch (Exception e) {}
        }

        System.out.println("Chef preparing: " + item);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        currentItem = item;
        foodReady = true;
        totalOrdersPrepared++;
        log("Prepared: " + item);
        notify();
    }

    synchronized void serveFood() {
        while(!foodReady) {
            try {
                wait();
            } catch (Exception e) {}
        }

        System.out.println("Waiting serving: " + currentItem);
        try {
            Thread.sleep(500);
        } catch (Exception e) {}

        totalRevenue += currentItem.getPrice();
        log("Served: " + currentItem);

        foodReady = false;
        currentItem = null;
        notify();
    }

    void displayStats() {
        System.out.println("Total Orders Prepared: " + totalOrdersPrepared);
        System.out.println("Total Revenue: " + totalRevenue);
    }

    void log(String msg) {
        try {
            FileWriter fw = new FileWriter("order.txt");
            fw.write(msg + "\n");
        } catch (IOException e) {}
    }
}

class ChefThread extends Thread {
    OrderCounter counter;
    FoodItem item;

    ChefThread(OrderCounter counter, FoodItem item){
        this.counter = counter;
        this.item = item;
    }

    public void run() {
        counter.prepareFood(item);
    }
}

class WaiterThread extends Thread {
    OrderCounter counter;
    WaiterThread(OrderCounter counter) {
        this.counter = counter;
    }

    public void run() {
        counter.serveFood();
    }
}

public class Q7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderCounter cnter = new OrderCounter();

        while(true) {
            System.out.println("\n1.Place Order");
            System.out.println("2.Serve Order");
            System.out.println("3.Display Stats");
            System.out.println("4.Read Order Log");
            System.out.println("5.Exit");

            int ch = sc.nextInt();

            switch(ch) {
                case 1:
                    System.out.println("Choose Item: 1.PIZZA 2.BURGER 3.PASTA");
                    int opt = sc.nextInt();
                    FoodItem item = FoodItem.values()[opt-1];
                    new ChefThread(cnter, item).start();
                    break;

                case 2:
                    new WaiterThread(cnter).start();
                    break;

                case 3:
                    cnter.displayStats();
                    break;
                case 4:
                    try {
                        FileReader fr = new FileReader("order.txt");
                        int c;
                        while ((c = fr.read()) != -1)
                            System.out.print((char) c);
                        fr.close();
                    } catch (Exception e) {}
                    break;

                case 5:
                    try {
                        FileWriter fw = new FileWriter("revenue.txt");
                        fw.write("Total Revenue: " + cnter.totalRevenue);
                        fw.close();
                    } catch (Exception e) {}
                    System.out.println("Revenue saved. Exiting...");
                    System.exit(0);
            }
        }
    }
}
