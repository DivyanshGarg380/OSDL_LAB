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
    Design and implement a menu-driven Java application to simulate a Restaurant Management System where food preparation and serving are handled concurrently using interfaces and threads.
    . Create an interface RestaurantService with the following methods:
        - void prepareItem(FoodItem item);
        - void serveItem();
    . Create an enum FoodItem:
        - PIZZA (250)
        - BURGER (150)  
        - PASTA (200)
    . Each food item should store:
        - price
        - method getPrice()

    . Create a class RestaurantCounter that:
        - Implements RestaurantService
        - Maintains a shared counter where only one food item can exist at a time
    . Data members:
        - FoodItem currentItem
        - boolean isPrepared
        - int totalItemsServed
        - double totalRevenue
    . Create two thread classes:
        - 1️⃣ ChefThread
         - Implements Runnable
         - Calls prepareItem()
        - 2️⃣ WaiterThread
         - Extends Thread
         - Calls serveItem()
    . LOGIC RULES:
        - Chef must WAIT if food is already prepared
        - Waiter must WAIT if no food is prepared
        - Race condition must be avoided using:
            - synchronized  
            - wait()    
            - notify()

    . Use TWO FILES:
        - service_log.txt
    . Log every action:
        - Prepared: PIZZA   
        - Served: PIZZA
        - revenue.txt
        -Store total revenue on program exit.
*/


import java.io.*;
import java.util.Scanner;

enum FoodItem {
    PIZZA(250), BURGER(150), PASTA(200);

    private int price;
    FoodItem(int price){ 
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

interface RestaurantService {
    void prepareItem(FoodItem item);
    void serveItem();
}

class RestaurantCounter implements RestaurantService {
    private FoodItem currItem;
    private boolean isPrepared = false;
    int totalItemsServed = 0;
    double totalRevenue = 0;
    
    @Override
    public synchronized void prepareItem(FoodItem item) {
        while(isPrepared) {
            try {
                wait();
            }catch (InterruptedException e){}
        }

        System.out.println("Chef preparing: " + item);

        try {
            Thread.sleep(1000);
        } catch(Exception e){}

        currItem = item;
        isPrepared = true;
        log("Prepared: " + item);
        notify();
    }

    @Override
    public synchronized void serveItem() {
        while(!isPrepared) {
            try {
                wait();
            } catch (InterruptedException e){}
        }

        System.out.println("Waiter serving: " + currItem);
        try {
            Thread.sleep(500);
        } catch(Exception e) {}

        totalRevenue += currItem.getPrice();
        totalItemsServed++;
        log("Served: " + currItem);
        currItem = null;
        isPrepared = false;
        notify();
    }

    void displayStats() {
        System.out.println("Items served: " + totalItemsServed);
        System.out.println("Total Revenue: " + totalRevenue);
    }

    void log(String msg) {
        try {
            FileWriter fw = new FileWriter("service_log.txt", true);
            fw.write(msg + "\n");
        } catch (IOException e){}
    }
}

class ChefThread implements Runnable {
    RestaurantService service;
    FoodItem item;

    ChefThread(RestaurantService service, FoodItem item) {
        this.service = service;
        this.item = item;
    }

    public void run() {
        service.prepareItem(item);
    }
}

class WaiterThread extends Thread {
    RestaurantService service;

    WaiterThread( RestaurantService service ){
        this.service = service;
    }

    public void run() {
        service.serveItem();
    }
}

public class test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RestaurantService counter = new RestaurantCounter();

        while(true) {
            System.out.println("\n1.Prepare Food");
            System.out.println("2.Serve Food");
            System.out.println("3.Display Stats");
            System.out.println("4.Read Service Log");
            System.out.println("5.Exit");

            int ch = sc.nextInt();
            switch(ch) {
                case 1:
                    System.out.println("Choose Item: 1.PIZZA 2.BURGER 3.PASTA");
                    FoodItem item = FoodItem.values()[sc.nextInt() - 1];
                    new Thread(new ChefThread(counter, item)).start();
                    break; 

                case 2:
                    new WaiterThread(counter).start();
                    break;

                case 3:
                    ((RestaurantCounter) counter).displayStats();
                    break;
                
                case 4:
                    try {
                        FileReader fr = new FileReader("service_log.txt");
                        int c;
                        while ((c = fr.read()) != -1)
                            System.out.print((char) c);
                        fr.close();
                    } catch(Exception e){}; 
                    break;

                case 5:
                    try {
                        FileWriter fw = new FileWriter("revenue.txt");
                        fw.write("Total Revenue: " +
                                ((RestaurantCounter) counter).totalRevenue);
                        fw.close();
                        System.out.println("Revenue saved. Exiting...");
                    } catch (Exception e){}
                    System.exit(0);
            }
        }

    }
}
