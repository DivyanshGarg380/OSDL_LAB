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
    Library Book Lending System (Multithreading + OOP + Enum + Interface):
     • Define an interface LibraryOperations with methods: 
        - borrowBook() 
        - returnBook() 
     • Create a base class LibraryMember with attributes:
        -  memberName 
        - booksBorrowed 
     • Create a derived class StudentMember that extends LibraryMember and implements LibraryOperations 
     • Define an enum TransactionType { BORROW, RETURN, NOT_AVAILABLE }
     • Create three threads:
         - Borrowing Thread (student borrowing a book) 
         - Returning Thread (student returning a book) 
         - Librarian Thread (monitors book availability and announces final status) 
     • Use synchronization to ensure: 
         - Book count updates are thread-safe
         - No book can be borrowed when stock is zero. 
         - Only valid borrow/return operations are performed. 
     • After each transaction display: 
         - Transaction type 
         - Current available book count 
         - Number of books borrowed by the member 
         - Final status announced by the librarian.
*/

import java.util.Scanner;

enum TransactionType {
    BORROW,
    RETURN,
    NOT_AVAILABLE;
}

interface LibraryOperations {
    void borrowBook();
    void returnBook();
}

class LibraryMember {
    protected String memberName;
    protected int booksBorrowed = 0;

    LibraryMember(String memberName) {
        this.memberName = memberName;
    }
}

class StudentMember extends LibraryMember implements LibraryOperations {
    private static int availableBooks = 3;
    private TransactionType last;

    StudentMember(String name) {
        super(name);
    }

    @Override
    public synchronized void borrowBook() {
        if(availableBooks > 0) {
            availableBooks--;
            booksBorrowed++;
            last = TransactionType.BORROW;
        } else {
            last = TransactionType.NOT_AVAILABLE;
        }

        displayStatus();
    }

    @Override
    public synchronized void returnBook() {
        if(booksBorrowed > 0) {
            availableBooks++;
            booksBorrowed--;
            last = TransactionType.RETURN;
        } else {
            last = TransactionType.NOT_AVAILABLE;
        }

        displayStatus();
    }

    private void displayStatus() {
        System.out.println("\nTransaction: " + last);
        System.out.println("Available Books: " + availableBooks);
        System.out.println(memberName + " Borrowed: " + booksBorrowed);
    }

    public static int getAvailableBooks() {
        return availableBooks;
    }
}

class BorrowThread extends Thread {
    StudentMember member;

    public BorrowThread(StudentMember member) {
        this.member = member;
    }

    public void run() {
        member.borrowBook();
    }
}

class ReturnThread extends Thread {
    StudentMember member;

    ReturnThread(StudentMember member) {
        this.member = member;
    }

    public void run() {
        member.returnBook();
    }
}

class LibrarianThread extends Thread {
    public void run() {
        System.out.println("Librarian Announcement: "
                + "Books Remaining = "
                + StudentMember.getAvailableBooks());
    }
}

public class Q14 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Student name");
        StudentMember student = new StudentMember(sc.next());

        while (true) { 
            System.out.println("\n1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    Thread b = new BorrowThread(student);
                    b.start();
                    try {
                        b.join();
                    } catch(Exception e) {}
                    new LibrarianThread().start();
                    break;

                case 2:
                    Thread r = new ReturnThread(student);
                    r.start();
                    try {
                        r.join();
                    } catch (Exception e) {}
                    new LibrarianThread().start();
                    break;

                case 3: 
                    System.exit(0);
            }

        }   
    }
}
