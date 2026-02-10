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
    BATCH 1 Question on 10/02/2025
    There is a cricket game scenario. Construct a enum Shot with fields as One, Two, Four, Six, out. 
    There is a cricketPlayer class which implements a interface with three methods ( run, appeal, umpiredecision). 
    If a player isnt out ( OUT from enum -> pass 0 ), add scores of that player. 
    If he is out now display the total runs he made and print OUT but the baller can take a appeal and check if he is out so asks umpire to check ( basic check if boolean isOut = true ). 
    so Three threads needed to be operated. All in a menu driven...
*/

import java.util.Scanner;

enum Shot {
    ONE(1), TWO(2), FOUR(4), SIX(6), OUT(0);
    
    private int runs;
    
    Shot(int runs) {
        this.runs = runs;
    }

    public int getRuns() { 
        return runs;
    }
}

interface CricketActions {
    void run(Shot shot);
    void appeal();
    void umpireDecision();
}

class CricketPlayer implements CricketActions {
    private int totalRuns = 0;
    private boolean isOut = false;
    String name;

    public CricketPlayer(String name) {
        this.name = name;
    }


    @Override
    public void run(Shot shot) {
        if(shot == Shot.OUT) {
            isOut = true;
            System.out.println("Player is OUT!");
        }else {
            totalRuns += shot.getRuns();
            System.out.println(name + " scored: " + shot.getRuns());
        }
    }

    @Override
    public void appeal() {
        System.out.println("Bowler appeals for OUT!");
    }

    @Override
    public void umpireDecision() {
        if(isOut) {
            System.out.println("Umpire Decision: OUT");
            System.out.println("Total Runs Scored by " + name + " is: " + totalRuns);
        }else {
            System.out.println("Umpire Decision: NOT OUT");
        }
    }
}

class PlayerThread extends Thread {
    CricketPlayer player;
    Shot shot;

    public PlayerThread(CricketPlayer player, Shot shot) {
        this.player = player;
        this.shot = shot;
    }

    public void run() {
        player.run(shot);
    }
}

class BowlerThread extends Thread {
    CricketPlayer player;

    BowlerThread(CricketPlayer player) {
        this.player = player;
    }

    public void run() {
        player.appeal();
    }
}

class UmpireThread extends Thread {
    CricketPlayer player;

    public UmpireThread(CricketPlayer player) {
        this.player = player;
    }

    public void run() {
        player.umpireDecision();
    }
}

public class Q8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter opening batsman name: ");
        String name = sc.nextLine();

        CricketPlayer currentPlayer = new CricketPlayer(name);

        while(true) {
            System.out.println("\n1.Play Shot");
            System.out.println("2.Bowler Appeal");
            System.out.println("3.Umpire Decision");
            System.out.println("4.New Player Comes to Bat");
            System.out.println("5.Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Choose Shot:");
                    System.out.println("1.ONE 2.TWO 3.FOUR 4.SIX 5.OUT");
                    int s = sc.nextInt();
                    sc.nextLine();
                    Shot shot = Shot.values()[s - 1];
                    new PlayerThread(currentPlayer, shot).start();
                    break;

                case 2:
                    new BowlerThread(currentPlayer).start();
                    break;

                case 3:
                    new UmpireThread(currentPlayer).start();
                    break;

                case 4:
                    System.out.print("Enter new batsman name: ");
                    String newName = sc.nextLine();
                    currentPlayer = new CricketPlayer(newName);
                    System.out.println(newName + " is now batting.");
                    break;

                case 5:
                    System.exit(0);
            }
        }
    }   
}
