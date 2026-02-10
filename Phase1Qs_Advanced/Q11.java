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
 SMART POWER GRID LOAD MANAGEMENT SYSTEM

    Design and implement a menu-driven Java application to simulate a
    Smart Power Grid Load Management System.

    The system must monitor electricity consumption from different appliances,
    detect overload conditions, and maintain an audit trail of grid usage using
    enums, interfaces, multithreading, synchronization, and file handling.

    Read the problem carefully. Incorrect design decisions will lead to
    logical errors.
    ────────────────────────────────────────────────────────────────────────────

    1. APPLIANCE REPRESENTATION (ENUM)

    Create an enum named ApplianceType with the following constants:

        AC        → 1500 watts
        HEATER    → 2000 watts
        FRIDGE    → 300 watts

    Each enum constant must:
        • Store power consumption per hour (in watts)
        • Provide a method getConsumption() that returns the power value

    ────────────────────────────────────────────────────────────────────────────

    2. POWER ACTION CONTRACT (INTERFACE)

    Create an interface named PowerActions with the following methods:

        void consumePower(ApplianceType appliance);
        void overloadCheck();
        void auditLog();

    Note:
        • These methods may be called by different threads concurrently.
        • No assumption should be made about the order of execution.

    ────────────────────────────────────────────────────────────────────────────

    3. CORE POWER GRID LOGIC (IMPLEMENTATION CLASS)

    Create a class named PowerGrid that implements the PowerActions interface.

    Data Members:
        • int maxLoad              → user-defined maximum load capacity
        • int currentLoad          → current total load on the grid
        • boolean overloaded       → indicates whether the grid is overloaded
        • int totalUnitsConsumed   → cumulative power consumption

    Rules:
        • Each appliance increases currentLoad based on its consumption.
        • If currentLoad exceeds maxLoad:
            - Set overloaded to true.
            - Power consumption must continue.
        • Overload must be reported but NOT prevented.
        • All shared data must be thread-safe using synchronization only.
        • Usage of wait() and notify() is strictly prohibited.

    ────────────────────────────────────────────────────────────────────────────

    4. MULTITHREADING REQUIREMENTS

    Implement THREE different thread classes, each performing a distinct role.
    All threads must operate on the same PowerGrid object.

    1) ConsumerThread
        • Simulates appliances being turned ON.
        • Calls consumePower(ApplianceType appliance).

    2) MonitorThread
        • Periodically checks the grid load.
        • Calls overloadCheck().

    3) AuditorThread
        • Records the current grid state.
        • Calls auditLog().

    Thread Behavior:
        • Threads may execute in any order.
        • Threads may be started multiple times.
        • Race conditions must be handled using synchronization only.

    ────────────────────────────────────────────────────────────────────────────

    5. FILE HANDLING (AUDIT SYSTEM)

    Create a file named "grid_audit.txt".

    Each audit entry must append the following information:
        • Current Load
        • Overload Status
        • Total Units Consumed

    Rules:
        • AuditorThread must append data to the file.
        • Existing data must not be overwritten.

    ────────────────────────────────────────────────────────────────────────────

    6. MENU-DRIVEN APPLICATION

    Implement the following menu options:

        1. Turn ON Appliance
        2. Start Load Monitor
        3. Audit Grid State
        4. Reset Grid Load
        5. Exit

    Important Conditions:
        • Menu options do NOT map directly to threads.
        • Monitor and Auditor threads may be started multiple times.
        • Reset operation:
            - Clears currentLoad
            - Does NOT reset totalUnitsConsumed
        • Program terminates only when Exit is selected.

    ────────────────────────────────────────────────────────────────────────────

    Additional Requirements:
        • Proper exception handling must be implemented.
        • Program must demonstrate correct use of:
            - Enum with state
            - Interface-based design
            - Multithreading with synchronization
            - File handling in append mode

    ────────────────────────────────────────────────────────────────────────────
*/

import java.io.*;
import java.util.Scanner;

enum ApplianceType {
    AC(1500), HEATER(2000), FRIDGE(300);

    private int consumption;

    ApplianceType (int consumption) {
        this.consumption = consumption;
    }

    public int getConsumption() {
        return consumption;
    }
}

interface PowerActions {
    void consumePower(ApplianceType appliance);
    void overloadCheck();
    void auditLog();
}

class PowerGrid implements PowerActions {
    private int maxLoad;
    private int currentLoad = 0;
    private boolean overLoaded = false;
    private int totalUnitsConsumed = 0;

    PowerGrid(int maxLoad){ 
        this.maxLoad = maxLoad;
    }

    @Override
    public synchronized void consumePower(ApplianceType appliance) {
        currentLoad += appliance.getConsumption();
        totalUnitsConsumed += appliance.getConsumption();
        System.out.println(appliance + " consuming power");

        if(currentLoad > maxLoad) {
            overLoaded = true;
        }
    }

    @Override
    public synchronized void overloadCheck() {
        if(overLoaded) {
            System.err.println("⚠ GRID OVERLOADED!");
        } else {
            System.err.println("Grid operating normally");
        }
    }

    @Override
    public synchronized void auditLog() {
        try (FileWriter fw = new FileWriter("grid_audit.txt", true)) {
            fw.write("Load: " + currentLoad +
                     ", Overloaded: " + overLoaded +
                     ", Units: " + totalUnitsConsumed + "\n");
        } catch (IOException e){}
        System.err.println("Audit logged");
    }

    synchronized void resetLoad() {
        currentLoad = 0;
        overLoaded = false;
        System.out.println("Grid load reset");
    }
}

class ConsumerThread extends Thread {
    PowerGrid grid;
    ApplianceType appliance;

    ConsumerThread(PowerGrid grid, ApplianceType appliance) {
        this.grid = grid;
        this.appliance = appliance;
    }

    public void run() {
        grid.consumePower(appliance);
    }
}

class MonitorThread extends Thread {
    PowerGrid grid;

    MonitorThread(PowerGrid grid) {
        this.grid = grid;
    }

    public void run() {
        grid.overloadCheck();
    }
}

class AuditorThread extends Thread {
    PowerGrid grid;

    AuditorThread(PowerGrid grid){
        this.grid = grid;
    }

    public void run() {
        grid.auditLog();
    }
}

public class Q11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter max Grid Load: ");
        PowerGrid grid = new PowerGrid(sc.nextInt());

        while(true){
            System.out.println("\n1.Turn ON Appliance");
            System.out.println("2.Start Load Monitor");
            System.out.println("3.Audit Grid State");
            System.out.println("4.Reset Grid Load");
            System.out.println("5.Exit");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.println("1.AC 2.HEATER 3.FRIDGE");
                    ApplianceType ap = ApplianceType.values()[sc.nextInt() - 1];
                    new ConsumerThread(grid, ap).start();
                    break;

                case 2:
                    new MonitorThread(grid).start();
                    break;

                case 3:
                    new AuditorThread(grid).start();
                    break;

                case 4:
                    grid.resetLoad();
                    break;

                case 5:
                    System.exit(0);
            }
        }
    }    
}
