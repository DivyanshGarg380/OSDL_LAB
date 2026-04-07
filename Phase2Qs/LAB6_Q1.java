import java.io.*;
import java.util.Scanner;

public class LAB6_Q1 {

    static final int NAME_LENGTH = 20; 
    static final int RECORD_SIZE = 4 + (2 * NAME_LENGTH) + 8 + 1; 

    // write
    public static void writeFixedString(String s, int length, RandomAccessFile file) throws IOException {
        StringBuilder sb = new StringBuilder(s);
        sb.setLength(length);
        file.writeChars(sb.toString());
    }

    // read
    public static String readFixedString(int length, RandomAccessFile file) throws IOException {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = file.readChar();
        }
        return new String(chars).trim();
    }

    // Add room
    public static void addRoom() throws IOException {
        RandomAccessFile file = new RandomAccessFile("rooms.dat", "rw");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Room Number: ");
        int roomNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Room Type: ");
        String type = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        boolean booked = false;

        file.seek(file.length()); // jump to end

        file.writeInt(roomNo);
        writeFixedString(type, NAME_LENGTH, file);
        file.writeDouble(price);
        file.writeBoolean(booked);

        file.close();
        System.out.println("Room added successfully!");
    }

    // Display room
    public static void displayRoom(int roomNo) throws IOException {
        RandomAccessFile file = new RandomAccessFile("rooms.dat", "r");

        long position = (roomNo - 1) * RECORD_SIZE;
        if (position >= file.length()) {
            System.out.println("Room not found!");
            file.close();
            return;
        }

        file.seek(position);

        int rNo = file.readInt();
        String type = readFixedString(NAME_LENGTH, file);
        double price = file.readDouble();
        boolean booked = file.readBoolean();

        System.out.println("\nRoom No: " + rNo);
        System.out.println("Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Booked: " + booked);

        file.close();
    }

    // Update booking status
    public static void updateStatus(int roomNo, boolean status) throws IOException {
        RandomAccessFile file = new RandomAccessFile("rooms.dat", "rw");

        long position = (roomNo - 1) * RECORD_SIZE;

        if (position >= file.length()) {
            System.out.println("Room not found!");
            file.close();
            return;
        }

        file.seek(position + 4 + (2 * NAME_LENGTH) + 8);

        file.writeBoolean(status);

        file.close();
        System.out.println("Status updated!");
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Room");
            System.out.println("2. View Room");
            System.out.println("3. Book Room");
            System.out.println("4. Vacate Room");
            System.out.println("5. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    System.out.print("Enter Room Number: ");
                    displayRoom(sc.nextInt());
                    break;
                case 3:
                    System.out.print("Enter Room Number: ");
                    updateStatus(sc.nextInt(), true);
                    break;
                case 4:
                    System.out.print("Enter Room Number: ");
                    updateStatus(sc.nextInt(), false);
                    break;
                case 5:
                    System.exit(0);
            }
        }
    }
}