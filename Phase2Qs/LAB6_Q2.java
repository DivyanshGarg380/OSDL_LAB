import java.io.*;
import java.util.*;

class Room implements Serializable {
    int roomNo;
    String type;
    double price;
    boolean booked;
    String name;

    Room(int roomNo, String type, double price) {
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
        this.booked = false;
        this.name = "";
    }
}

public class LAB6_Q2 {
    static final String FILE_NAME = "rooms.ser";
    
    // save array to file
    public static void save(Room[] rooms) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        oos.writeObject(rooms);
        oos.close();
    }

    // load array from file
    public static Room[] load() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if(!file.exists()) return new Room[0];

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
        Room[] rooms = (Room[]) ois.readObject();
        ois.close();
        return rooms;
    }

    // adding nwe room
    public static void addRoom() throws Exception {
        Scanner sc = new Scanner(System.in);
        Room[] old = load();

        System.out.println("Enter Room Number: ");
        int no = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Type: ");
        String type = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        Room[] newRoom = new Room[old.length + 1];
        for(int i = 0; i < old.length; ++i) {
            newRoom[i] = old[i];
        }

        newRoom[old.length] = new Room(no, type, price);
        save(newRoom);
        System.out.println("Room added successfully!!");
    }

    public static void displayAll() throws Exception {
        Room[] rooms = load();

        if (rooms.length == 0) {
            System.out.println("No rooms found!");
            return;
        }

        for (int i = 0; i < rooms.length; i++) {
            Room r = rooms[i];
            System.out.println("\nRoom No: " + r.roomNo);
            System.out.println("Type: " + r.type);
            System.out.println("Price: " + r.price);
            System.out.println("Booked: " + r.booked);
            System.out.println("Guest: " + r.name);
        }
    }

    public static void search(int roomNo) throws Exception {
        Room[] rooms = load();
        for(int i = 0; i < rooms.length; ++i) {
            if(rooms[i].roomNo == roomNo) {
                System.out.println("Room Found!");
                System.out.println("Type: " + rooms[i].type);
                System.out.println("Price: " + rooms[i].price);
                return;
            }
        }
        System.out.println("Room not found!");
    }

    public static void updateBooking(int roomNo, boolean status) throws Exception {
        Scanner sc = new Scanner(System.in);
        Room[] rooms = load();

        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].roomNo == roomNo) {
                rooms[i].booked = status;

                if (status) {
                    System.out.print("Enter Guest Name: ");
                    sc.nextLine();
                    rooms[i].name = sc.nextLine();
                } else {
                    rooms[i].name = "";
                }

                save(rooms);
                System.out.println("Booking updated!");
                return;
            }
        }

        System.out.println("Room not found!");
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Room");
            System.out.println("2. Display All Rooms");
            System.out.println("3. Search Room");
            System.out.println("4. Book Room");
            System.out.println("5. Vacate Room");
            System.out.println("6. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    displayAll();
                    break;
                case 3:
                    System.out.print("Enter Room Number: ");
                    search(sc.nextInt());
                    break;
                case 4:
                    System.out.print("Enter Room Number: ");
                    updateBooking(sc.nextInt(), true);
                    break;
                case 5:
                    System.out.print("Enter Room Number: ");
                    updateBooking(sc.nextInt(), false);
                    break;
                case 6:
                    System.exit(0);
            }
        }
    }
}
