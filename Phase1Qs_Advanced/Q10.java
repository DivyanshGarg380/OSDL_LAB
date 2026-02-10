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
    Railway Ticket Validation System (Inspector–Passenger–Control Room)
*/

import java.util.Scanner;

enum TicketStatus {
    VALID, INVALID, EXPIRED;
}

interface RailwayActions {
    void travel(TicketStatus staus);
    void inspect();
    void controlRoomDecision();
}

class Passenger implements RailwayActions {
    private String name;
    private boolean isCaught = false;
    private int totalFine = 0;

    Passenger(String name) {
        this.name = name;
    }

    @Override
    public void travel(TicketStatus status) {
        if(status == TicketStatus.VALID) {
            System.out.println(name + " is travelling with a VALID ticket.");
        } else {
            isCaught = true;
            System.out.println(name + " attempted travel with " + status + " ticket.");
        }
    }

    @Override
    public void inspect() {
        System.out.println("Inspector checking ticket of " + name);
    }

    @Override
    public void controlRoomDecision() {
        if(isCaught) {
            totalFine += 500;
            System.out.println("Control Room: " + name + " fined 500");
            System.out.println("Total Fine: " + totalFine);
            isCaught = false;
        } else {
            System.out.println("Control Room: " + name + " is clear to travel");
        }
    }
}

class PassengerThread extends Thread {
    Passenger p;
    TicketStatus status;

    public PassengerThread(Passenger p, TicketStatus s) {
        this.p = p;
        this.status = s;
    }

    public void run() {
        p.travel(status);
    }
}

class InsepectorThread extends Thread {
    Passenger p;

    InsepectorThread(Passenger p){
        this.p = p;
    }

    public void run() {
        p.inspect();
    }
}

class ControlRoomThread extends Thread {
    Passenger p;

    ControlRoomThread(Passenger p) {
        this.p = p;
    }

    public void run() {
        p.controlRoomDecision();
    }
}

public class Q10 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Passenger Name: ");
        Passenger passenger = new Passenger(sc.nextLine());

        while(true){
            System.out.println("\n1.Travel");
            System.out.println("2.Inspector Inspection");
            System.out.println("3.Control Room Decision");
            System.out.println("4.New Passenger");
            System.out.println("5.Exit");


            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){ 
                case 1:
                    System.out.println("Ticket Status: 1.VALID 2.INVALID 3.EXPIRED");
                    TicketStatus status = TicketStatus.values()[sc.nextInt() - 1];
                    new PassengerThread(passenger, status).start();
                    break;

                case 2:
                    new InsepectorThread(passenger).start();
                    break;

                case 3:
                    new ControlRoomThread(passenger).start();
                    break;

                case 4:
                    System.out.println("Enter new Passenger name: ");
                    passenger = new Passenger(sc.nextLine());
                    break;
                
                
                case 5:
                    System.exit(0);
            }
        }
    }
}
