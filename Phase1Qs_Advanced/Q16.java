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
    Design a Java-based Satellite Data Processing System.
    The system simulates:
     - A Satellite generating raw signal data
     - A Data Processor validating and transforming the signal
     - A Ground Control logging verified signals to file
*/

import java.io.*;
import java.util.*;

enum SignalType {
    NORMAL, WEAK, CRITICAL, INVALID;
}

class Signal {
    int id;
    int strength;
    SignalType type;

    Signal(int id, int strength, SignalType type) {
        this.id = id;
        this.strength = strength;
        this.type = type;
    }
}

interface SignalOperations {
    void generateSignal();
    void processSignal();
    void logSignal();
}

class SatelliteSystem implements SignalOperations {
    private Signal currentSignal;
    private boolean generated = false;
    private boolean processed = false;
    private int cnt = 0;
    private final int MAX = 5;

    public synchronized void generateSignal() {
        while(generated) {
            try { wait(); } catch (InterruptedException e){}
        }

        if(cnt >= MAX) return;
        int strength = new Random().nextInt(100);
        SignalType type;

        if(strength > 70) type = SignalType.CRITICAL;
        else if (strength > 40) type = SignalType.NORMAL;
        else if (strength > 10) type = SignalType.WEAK;
        else type = SignalType.INVALID;

        currentSignal = new Signal(cnt + 1, strength, type);

        System.out.println("Satellite Generated: ID " + currentSignal.id +
                " Strength " + strength + " Type " + type);
        
        generated = true;
        processed = false;
        cnt++;
        notifyAll();
    }

    public synchronized void processSignal() {
        while(!generated) {
            if(cnt >= MAX) return;
            try { wait(); } catch (InterruptedException e) {}
        }

        if(currentSignal.type == SignalType.INVALID) {
            System.out.println("Processor: Signal Rejected");
        } else {
            System.out.println("Processor: Signal Verified");
        }


        processed = true;
        generated = false;
        notifyAll();
    }

    public synchronized void logSignal() {
        while(!processed) {
            if(cnt >= MAX) return;
            try { wait(); } catch (InterruptedException e){}
        }


        try(FileWriter fw = new FileWriter("satellite_log.txt", true)) {
            fw.write("Signal ID: " + currentSignal.id +
                    ", Strength: " + currentSignal.strength +
                    ", Type: " + currentSignal.type + "\n");
        } catch (IOException e){}

        System.out.println("Ground Control: Logged Signal " + currentSignal.id);
        processed = false;
        notifyAll();
    }

    public boolean isFinished() {
        return cnt >= MAX;
    }
}

class SatelliteThread extends Thread {
    SatelliteSystem system;

    SatelliteThread(SatelliteSystem system) {
        this.system = system;
    }

    public void run() {
        while(!system.isFinished()){
            system.generateSignal();
            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }
}

class ProcessorThread extends Thread {
    SatelliteSystem system;

    public ProcessorThread(SatelliteSystem system) {
        this.system = system;
    }

    public void run() {
        while(!system.isFinished()) {
            system.processSignal();
        }
    }
}


class GroundControlThread extends Thread {
    SatelliteSystem system;

    public GroundControlThread(SatelliteSystem system) {
        this.system = system;
    }

    public void run() {
        while(!system.isFinished()) {
            system.logSignal();
        }
    }
}

public class Q16 {
    public static void main(String[] args) {
        SatelliteSystem system = new SatelliteSystem();

        new SatelliteThread(system).start();
        new ProcessorThread(system).start();
        new GroundControlThread(system).start();
    }   
}
