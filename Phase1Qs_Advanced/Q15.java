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
    Sensor Data Logging System 
    - Objective:
        - Simulate a temperature sensor system that writes and reads data from a file.
    - Requirements:
        1. Create a shared file:
            -sensordata.txt
        2. Create 
        3 threads:
            (a) Sensor Thread:
                - Periodically generates temperature readings.
                - Writes readings into sensordata.txt.
                - Use FileWriter (unbuffered character stream).

            (b) Monitor Thread:
                - Reads data from sensordata.txt.
                - Displays recorded readings.
                - Use FileReader (unbuffered character stream).

            (c) Controller Thread:
                - Monitors the process.
                - Announces when all readings are recorded.
                - Displays:
                    • Total file size
                    • Confirmation message

    4. Synchronization Rules:
        - Only one thread can access the file at a time.
        - Monitor must not read while Sensor is writing.
        - No partial or corrupted data should be read.

    5. Console Output Must Show:
    - Sensor writing readings.
    - Monitor reading readings.
    - Final confirmation and file statistics from Controller.

    some batch got this.. this was the last batch now only we are left 
*/

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class SensorSystem {
    private final String filename = "sensordata.txt";
    private int readingsCount = 0;
    private int maxReadings;
    private boolean writing = false;

    public SensorSystem(int maxReadings) {
        this.maxReadings = maxReadings;
    }

    public synchronized void writeReading() {
        if(readingsCount >= maxReadings) return;

        while(writing) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        writing = true;

        int temp = new Random().nextInt(15) + 20;
        try(FileWriter fw = new FileWriter(filename, true)) {
            fw.write("Temperature: " + temp + "°C\n");
            System.out.println("Sensor wrote: " + temp + "°C\n");
        } catch (IOException e) {}

        readingsCount++;
        writing = false;

        notifyAll();
    }

    public synchronized void readData() {
        while(writing) {
            try {
                wait();
            } catch (Exception e) {}
        }

        try(FileReader fr = new FileReader(filename)) {
            int ch;
            System.out.println("\n Monitor Reading File:");
            while((ch = fr.read()) != -1){
                System.out.println((char)ch);
            }
            System.out.println();
        } catch(IOException e){}

        notifyAll();
    }

    public synchronized void showFinalReport() {
        if(readingsCount >= maxReadings) {
            File file = new File(filename);
            System.out.println("\nController Report:");
            System.out.println("All readings recorded.");
            System.out.println("Total File Size: " + file.length() + " bytes");
        }
    }

    public boolean isFinished() {
        return readingsCount >= maxReadings;
    }
}

class SensorThread extends Thread {
    SensorSystem system;

    SensorThread(SensorSystem system) {
        this.system = system;
    }

    public void run() {
        while(!system.isFinished()) {
            system.writeReading();
            try {
                Thread.sleep(1000);
            } catch (Exception e){}
        }
    }
}

class MonitorThread extends Thread {
    SensorSystem system;

    MonitorThread(SensorSystem system) {
        this.system = system;
    }

    public void run() {
        while (!system.isFinished()) {
            system.readData();
            try { Thread.sleep(1500); } catch (Exception e) {}
        }
    }
}

class ControllerThread extends Thread {
    SensorSystem system;

    ControllerThread(SensorSystem system) {
        this.system = system;
    }

    public void run() {
        while (!system.isFinished()) {
            try { Thread.sleep(2000); } catch (Exception e) {}
        }
        system.showFinalReport();
    }
}

public class Q15 {
    public static void main(String[] args) {
        SensorSystem system = new SensorSystem(5);

        new SensorThread(system).start();
        new MonitorThread(system).start();
        new ControllerThread(system).start();
    }
}
