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
    Design a menu-driven Java application to simulate a Movie Ticket Booking System.
    . Create Enum -> REGULAR, PREMIUM, VIP
    . Create Interface with Methods -> bookSeat, cancelSeat, showStatus
    . Create a class MovieTheatre that:
       - Implements BookingActions
       - Maintains:
          - totalSeats
          - bookedSeats
          - totalRevenue
       - Rules:
          - If bookedSeats == totalSeats → show HOUSEFULL
          - Revenue increases based on seat price

    . Create BookingThread:
    . Multiple users try booking simultaneously
    . Synchronize booking to prevent overbooking
    . Use sleep() to simulate booking delay 
*/

import java.util.Scanner;

enum SeatType {
    REGULAR(150), PREMIUM(250), VIP(400);
    private int price;
    SeatType (int price) {
        this.price = price;
    }
    int getPrice() {
        return price;
    }
}

interface BookingActions {
    void bookSeat(SeatType type);
    void cancelSeat();
    void showStatus();
}

class MovieTheatre implements BookingActions {
    int totalSeats;
    int bookedSeats = 0;
    int totalRevenue = 0;

    public MovieTheatre(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    @Override
    public synchronized void bookSeat(SeatType type) {
        if(bookedSeats >= totalSeats) {
            System.out.println("HOUSEFULL!");
            return;
        }

        try {
            Thread.sleep(500);
        } catch (Exception e) {}

        bookedSeats++;
        totalRevenue += type.getPrice();
        System.out.println("Booked " + type + " seat");
    }

    @Override
    public synchronized void cancelSeat() {
        if(bookedSeats > 0) {
            bookedSeats--;
            System.err.println("Ticket canceled");
        }
    }

    @Override
    public void showStatus() {
        System.out.println("Booked Seats: " + bookedSeats);
        System.out.println("Available Seats: " + (totalSeats - bookedSeats));
        System.out.println("Revenue: " + totalRevenue);
    }
}

class BookingThread extends Thread {
    MovieTheatre theatre;
    SeatType type;

    BookingThread(MovieTheatre t, SeatType type){
        this.theatre = t;
        this.type = type;
    }

    public void run() {
        theatre.bookSeat(type);
    }
}

public class Q9 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MovieTheatre theatre = new MovieTheatre(5);

        while (true) { 
            System.out.println("\n1.Book Ticket");
            System.out.println("2.Cancel Ticket");
            System.out.println("3.Show Status");
            System.out.println("4.Exit");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.println("1.REGULAR 2.PREMIUM 3.VIP");
                    SeatType type = SeatType.values()[sc.nextInt() - 1];
                    new BookingThread(theatre, type).start();
                    break;

                case 2:
                    theatre.cancelSeat();
                    break;

                case 3:
                    theatre.showStatus();
                    break;

                case 4:
                    System.exit(0);
            }
        }
    }
}
