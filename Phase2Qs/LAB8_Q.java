import java.util.*;

class Room {
    int roomNo;
    String type;
    double price;
    boolean available;

    Room(int roomNo, String type, double price) {
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
        this.available = true;
    }
}

class Customer {
    int id;
    String name;
    String phone;
    int roomNo;

    Customer(int id, String name, String phone, int roomNo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.roomNo = roomNo;
    }
}

public class LAB8_Q {
    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    static HashMap<Integer, Customer> bookingMap = new HashMap<>();
    
    static Scanner sc = new Scanner(System.in);

    static void addRoom() {
        System.out.println("Enter Room No: ");
        int no = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter type: ");
        String type = sc.nextLine();

        System.out.println("Enter Price: ");
        double price = sc.nextDouble();

        rooms.add(new Room(no, type, price));
        System.out.println("Room added!");
    }

    static void displayAvailableRooms() {
        Collections.sort(rooms, (a, b) -> Double.compare(a.price, b.price));

        for(Room r : rooms) {
            if(r.available) {
                System.out.println("Room: " + r.roomNo + " Type: " + r.type + " Price: " + r.price);
            }
        }

    }

    static void addCustomer() {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        customers.add(new Customer(id, name, phone, -1));
        System.out.println("Customer added!");
    }

    static void bookRoom() {
        System.out.println("Enter Customer ID: ");
        int id = sc.nextInt();

        System.out.println("Enter Room No: ");
        int rno = sc.nextInt();

        Room curr = null;
        for(Room r : rooms) {
            if(r.roomNo == rno) {
                curr = r;
                break;
            }
        }

        if(curr == null) {
            System.out.println("Room not found!");
            return;
        } 
        if(!curr.available) {
            System.out.println("Room already booked!");
            return;
        }

        for(Customer c : customers) {
            if(c.id == id) {
                curr.available = false;
                c.roomNo = rno;
                bookingMap.put(rno, c);
                System.out.println("Room booked sccessfully!");
                return;
            }
        }

        System.out.println("Customer not found!");
    }

    static void checkout() {
        System.out.println("Enter Room No: ");
        int rno = sc.nextInt();

        if(!bookingMap.containsKey(rno)) {
            System.out.println("No booking found!");
            return;
        }

        Customer c = bookingMap.get(rno);

        for(Room r : rooms) {
            if(r.roomNo == rno) {
                r.available = true;
                break;
            }
        }

        c.roomNo = -1;
        bookingMap.remove(rno);
        System.out.println("Checkout successfull!");
    }

    static void displayCustomers() {
        Iterator<Customer> it = customers.iterator();
        while(it.hasNext()) {
            Customer c = it.next();
            System.out.println("ID: " + c.id + "Name: " + c.name + "Phone: " + c.phone + " Room: " + c.roomNo);
        }
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n--- HOTEL SYSTEM ---");
            System.out.println("1. Add Room");
            System.out.println("2. Display Available Rooms");
            System.out.println("3. Add Customer");
            System.out.println("4. Book Room");
            System.out.println("5. Checkout");
            System.out.println("6. Display Customers");
            System.out.println("7. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1: addRoom(); break;
                case 2: displayAvailableRooms(); break;
                case 3: addCustomer(); break;
                case 4: bookRoom(); break;
                case 5: checkout(); break;
                case 6: displayCustomers(); break;
                case 7: System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }
}
