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
    Design and implement a Java-based File Chat System using multithreading and synchronization mechanisms.
    . The system must simulate a chat application where communication happens through a shared file named chatlog.txt.
    . System Requirements:
        - The system must contain three types of threads:
            - Sender Thread
            - Receiver Thread
            - Moderator Thread
        - Sender Thread:
            - Writes messages to the shared file chatlog.txt.
            - Only one message should be written at a time.
            - Must notify other threads after writing a message.
        - Receiver Thread:
            - Reads messages from chatlog.txt.
            - Should only read after a new message is written.
            - Must not read while the sender is writing (avoid race conditions).
        - Moderator Thread:
            - Reads each message from chatlog.txt.
            - Prints whether the message is:
                - "Message Approved" (if valid)
                - "Message Rejected" (if it contains inappropriate words)
                - Should ensure it reads only after the sender writes the message.
        - Proper synchronization must be implemented using:
            - synchronized keyword
            - wait()
            - notify() / notifyAll()
        - The program must ensure:
            - No data inconsistency
            - No simultaneous read/write conflicts
            - Proper coordination between all three threads
        .The program should run until a predefined number of messages are exchanged.
*/

import java.io.*;
import java.util.*;

class ChatRoom {
    private String currMessage;
    private boolean newMessageAvailable = false;
    private int count = 0;
    private int maxMessages;

    ChatRoom(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public synchronized void sendMessage(String message) {
        while(newMessageAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        if(count >= maxMessages) return;
        try (FileWriter fw = new FileWriter("chatlog.txt", true)) {
            fw.write(message + "\n");
        } catch (IOException e){}

        currMessage = message;
        newMessageAvailable = true;
        count++;

        System.out.println("Sender: " + message);
        notifyAll();
    }

    public synchronized void recieveMessage() {
        while(!newMessageAvailable) {
            if(count >= maxMessages) return;

            try {
                wait();
            } catch (InterruptedException e) {}
        }

        System.out.println("Reciever read: " + currMessage);
        newMessageAvailable = false;
        notifyAll();
    }

    public synchronized void moderateMessage() {
        while(!newMessageAvailable) {
            if(count >= maxMessages) return;

            try {
                wait();
            } catch (InterruptedException e) {}
        }

        if(currMessage.toLowerCase().contains("bad")) {
            System.out.println("Moderator: Message Rejected");
        } else {
            System.out.println("Moderator: Message approved");
        }
    }

    public boolean isFinished() {
        return count >= maxMessages;
    }
}

class RecieverThread extends Thread {
    ChatRoom chat;

    public RecieverThread(ChatRoom chat) {
        this.chat = chat;
    }

    public void run() {
        chat.recieveMessage();
    }
}

class ModeratorThread extends Thread {
    ChatRoom chat;

    ModeratorThread(ChatRoom chat) {
        this.chat = chat;
    }

    public void run() {
        chat.moderateMessage();
    }
}

public class Q13 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of messages to exchange: ");
        int max = sc.nextInt();
        sc.nextLine();

        ChatRoom chat = new ChatRoom(max);
        while(!chat.isFinished()) {
            System.out.println("\n1. Send Message");
            System.out.println("2. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    System.out.println("Enter message: ");
                    String msg = sc.nextLine();

                    chat.sendMessage(msg);
                    new ModeratorThread(chat).start();
                    new RecieverThread(chat).start();
                    break;

                case 2:
                    System.exit(0);
            }
        }
    }
}
