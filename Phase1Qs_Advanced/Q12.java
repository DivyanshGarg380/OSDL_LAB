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
    Design and implement a Java-based Hotel Booking Management System.
    The system should simulate a real-world hotel where multiple customers attempt to book rooms concurrently. 
    The hotel has a limited number of rooms, and booking requests must be handled safely to avoid data inconsistency.
    Each customer booking request must be processed in a separate thread. If no rooms are available, the booking thread must wait. 
    When a room is released by another customer, waiting booking threads should be notified and allowed to proceed.
    Room details such as room number, room type, price per day, and booking status must be maintained using object-oriented principles. 
    Use an enumeration to represent different room types and their base tariffs.
    All booking and cancellation details must be stored in a file and later read back and displayed to the user.
    Use appropriate thread synchronization techniques, simulate processing delays using Thread.sleep(), and handle all necessary exceptions.
*/

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

enum RoomType {
    SINGLE(1000), DOUBLE(2000), DELUXE(3500);

    private int basePrice;

    RoomType(int basePrice){
        this.basePrice = basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }
}

class Room {
    int roomNumber;
    RoomType type;
    boolean booked;

    public Room(int roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.booked = false;
    }
}

class Hotel {
    Room[] rooms;

    Hotel(Room[] rooms) {
        this.rooms = rooms;
    }

    synchronized Room bookRoom(String name) {
        while(true) {
            for(int i = 0; i < rooms.length; i++) {
                if(!rooms[i].booked) {
                    rooms[i].booked = true;
                    log(name + " booked Room " + rooms[i].roomNumber);
                    return rooms[i];
                }
            }

            try {
                System.out.println("No rooms available. Waiting...");
                wait();
            } catch (InterruptedException e) {}
        }
    }

    synchronized void cancelRoom(Room r, String name) {
        r.booked = false;
        log(name + " cancelled Room " + r.roomNumber);
        notify();
    }
    
    void log(String msg) {
        try {
            FileWriter fw = new FileWriter("hotel.txt", true);
            fw.write(msg + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("File Error in Log");
        }
    }
}

class Customer extends Thread {
    Hotel hotel;
    String name;

    Customer(Hotel hotel, String name) {
        this.hotel = hotel;
        this.name = name;
    }

    public void run() {
        Room r = hotel.bookRoom(name);
        try {
            Thread.sleep(2000); 
        } catch(InterruptedException e) {}
        hotel.cancelRoom(r, name);
    }
}

public class Q12 {
    public static void main(String[] args) {
        Room[] rooms = {
            new Room(101, RoomType.SINGLE),
            new Room(102, RoomType.DOUBLE)
        };

        Hotel hotel = new Hotel(rooms);
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("\n--- HOTEL MENU ---");
            System.out.println("1. Book Room");
            System.out.println("2. View Booking Log");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch(ch) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String name = sc.nextLine();
                    Customer c = new Customer(hotel, name);
                    c.start();
                    break;

                case 2:
                    try {
                        FileReader fr = new FileReader("hotel.txt");
                        int x;
                        while((x = fr.read()) != -1) {
                            System.out.println((char)x);
                        }
                        fr.close();
                    } catch (IOException e) {
                        System.out.println("No records found");
                    }
                    break;
                
                case 3:
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}