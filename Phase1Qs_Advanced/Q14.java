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
    No Proper Question provided by the Contributor :(
*/

import java.util.*;
import java.io.*;

enum ResourceType{
    CPU(50),
    GPU(100),
    MEMORY(30),
    STORAGE(20);

    int units;

    ResourceType(int units){
        this.units = units;
    }

    int getUnits(){
        return units;
    }
}

interface ResourceOperations{
    void allocate(ResourceType type);
    void release(ResourceType type);
    void logStatus();
}

abstract class SystemResource{
    String systemName;
    int maxCapacity;
    int currentUsage;
    boolean overloaded;

    SystemResource(String systemName, int maxCapacity , int currentUsage , boolean overloaded){
        this.systemName = systemName;
        this.maxCapacity = maxCapacity;
        this.currentUsage = currentUsage;
        this.overloaded = overloaded;
    }

    abstract void checkOverload();
}

class ResourceManager extends SystemResource implements ResourceOperations{
    int cpuCount = 0;
    int gpuCount = 0;
    int memoryCount = 0;
    int storageCount = 0;

    ResourceManager(String systemName, int maxCapacity , int currentUsage , boolean overloaded){
        super(systemName, maxCapacity , currentUsage , overloaded);
    }

    @Override
    synchronized public void allocate(ResourceType type){

        int units = type.getUnits();

        while((currentUsage + units) > maxCapacity){
            overloaded = true;
            System.out.println("System overloaded... Waiting");
            try{ wait(); }catch(Exception e){}
        }

        currentUsage += units;

        switch(type){
            case CPU: cpuCount++; break;
            case GPU: gpuCount++; break;
            case MEMORY: memoryCount++; break;
            case STORAGE: storageCount++; break;
        }

        overloaded = false;

        try{
            FileWriter fw = new FileWriter("resource.txt", true);
            fw.write("Allocated " + type +
                    " | Usage: " + currentUsage + "\n");
            fw.close();
        }
        catch(IOException e){}

        System.out.println("Allocated " + type +" | Usage: " + currentUsage);
    }

    @Override
    synchronized public void release(ResourceType type){
        int units = type.getUnits();

        currentUsage -= units;

        switch(type){
            case CPU: cpuCount--; break;
            case GPU: gpuCount--; break;
            case MEMORY: memoryCount--; break;
            case STORAGE: storageCount--; break;
        }

        if(currentUsage <= maxCapacity){
            overloaded = false;
            notifyAll();
        }

        try{
            FileWriter fw = new FileWriter("resource.txt", true);
            fw.write("Allocated " + type +
                    " | Usage: " + currentUsage + "\n");
            fw.close();
        }
        catch(IOException e){}

        System.out.println("Allocated " + type +" | Usage: " + currentUsage);
    }

    @Override
    synchronized void checkOverload(){
        if(overloaded){
            System.out.println("Status : System is overloaded");
        }
        else{
            System.out.println("Status : System is Normal");
        }
    }

    synchronized public void logStatus(){
        try{
        FileWriter fw = new FileWriter("resource.txt", true);

        fw.write("STATUS CPU:" + cpuCount + " GPU:" + gpuCount +" MEMORY:" + memoryCount +" STORAGE:" + storageCount +" | Total Usage:" + currentUsage +" | Overloaded:" + overloaded + "\n");
        fw.close();
    }
    catch(IOException e){}
    }
}

class Client implements Runnable{
    ResourceManager RM;
    ResourceType type;

    Client(ResourceManager RM, ResourceType type){
        this.RM = RM;
        this.type = type;
    }

    @Override
    public void run(){
        try{
            RM.allocate(type);
            Thread.sleep(8000);
            RM.release(type);
        }
        catch(Exception e){}
    }
}

class Monitor implements Runnable{
    ResourceManager RM;

    Monitor(ResourceManager RM){
        this.RM = RM;
    }

    @Override
    public void run(){
        while(true){
            try{
                Thread.sleep(2000);
            }
            catch(Exception e){}
            RM.checkOverload();
        }
    }
}

class Auditor implements Runnable{
    ResourceManager RM;

    Auditor(ResourceManager RM){
        this.RM = RM;
    }

    @Override
    public void run(){
        while(true){
            try{
                Thread.sleep(5000);
            }
            catch(Exception e){}
            RM.logStatus();
            System.out.println("Audit Logged");
        }
    }
}

public class ResourceManagment {

    public static void main(String[] args){

        ResourceManager RM = new ResourceManager("DataCenter", 100, 0, false);

        Thread c1 = new Thread(new Client(RM, ResourceType.CPU));
        Thread c2 = new Thread(new Client(RM, ResourceType.GPU));
        Thread c3 = new Thread(new Client(RM, ResourceType.MEMORY));
        Thread c4 = new Thread(new Client(RM, ResourceType.STORAGE));

        Thread monitor = new Thread(new Monitor(RM));
        Thread auditor = new Thread(new Auditor(RM));

        monitor.start();
        auditor.start();

        c1.start();
        c2.start();
        c3.start();
        c4.start();
    }
}
