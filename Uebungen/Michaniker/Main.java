package Michaniker;

import java.util.Arrays;
import java.util.Random;


class Schluessel {
    private boolean benutzt;
    public final int id;

    public  volatile int turn;
    public  boolean[] flags = {false, false};

    public Schluessel() {
        id = (int) (Math.random() * 100);
        this.benutzt = false;
    }

    public boolean isBenutzt() {
        return benutzt;
    }

    public  void setBenutzt(boolean benutzt) {
        this.benutzt = benutzt;
    }

    @Override
    public String toString() {
        return "Schluessel id : " + this.id;
    }
}

class Motor {
    private final boolean hard;
    private final int time_needed;


    public Motor() {
        Random r = new Random();

        this.hard = r.nextInt(100 - 1) + 1 <= 45;

        this.time_needed = (int) (Math.random() * 10000);
    }

    public int getTime_needed() {
        return time_needed;
    }

    public boolean isHard() {
        return hard;
    }

    @Override
    public String toString() {
        return " " + this.hard + " ";
    }
}

class Mechaniker extends Thread {

    private Schluessel left;
    private Schluessel right;
    private Mechaniker leftNeighbor;
    private Mechaniker rightNeighbor;

    private Motor[] motors;

    private final int id;



    public Mechaniker(Schluessel left, Schluessel right, int workLoad, int id) {
        this.motors = Arrays.stream(new Motor[workLoad]).toList().stream().map(m -> new Motor()).toArray(Motor[]::new);
        this.left = left;
        this.right = right;
        this.id = id;
    }


    @Override
    public void run() {

        System.out.println("Mechaniker " + this.id + " start to work " + " Neighbors : " + " LeftNeighbor : " + leftNeighbor.id + " RightNeighbor : " + rightNeighbor.id);

        for (int i = 0; i < motors.length; i++) {

            Motor motor = this.motors[i];
            System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor);

            if (motor.isHard()) {

                left.flags[0] = true;
                left.turn = 1;

                boolean busy = false;

                while (left.flags[1] && left.turn == 1){
                    if(!busy){
                        System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "watied until neighbor right ::" + rightNeighbor.id + "  is done with left :: " + left);
                        busy = true;
                    }
                }

                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "take left :: " + left);

                right.flags[1] = true;
                right.turn = 0;

                busy = false;
                while (right.flags[0] && right.turn == 0){
                    if(!busy){
                        System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "watied until neighbor left :: " + leftNeighbor.id + " is done with right :: " + right);
                        busy = true;
                    }
                }


                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "take right :: " + right);


                try {
                    int time = motor.getTime_needed();
                    System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + " need : " + time + " ms");
                    sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + " Work Done");

                right.flags[1] = false;
                left.flags[0] = false;

            } else {

                left.flags[0] = true;
                left.turn = 1;

                boolean busy = false;

                while (left.flags[1] && left.turn == 1){
                    if(!busy){
                        System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "watied until neighbor right ::" + rightNeighbor.id + "  is done with left :: " + left);
                        busy = true;
                    }
                }

                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "take left :: " + left);

                try {
                    int time = motor.getTime_needed();
                    System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + " need : " + time + " ms");
                    sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + " Work Done");

                left.flags[0] = false;
            }

        }

        System.out.println("Mechaniker " + this.id + " Work Done");
    }


    public Schluessel getRight() {
        return right;
    }

    public void setRight(Schluessel right) {
        this.right = right;
    }

    public Schluessel getLeft() {
        return left;
    }

    public void setLeft(Schluessel left) {
        this.left = left;
    }


    public Mechaniker getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(Mechaniker leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Mechaniker getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(Mechaniker rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    @Override
    public String toString() {
        return "Mechaniker : \n id : " + this.id + " \n Left : " + this.left.id + " \n Right : " + this.right.id + " \n Motors : \n" + Arrays.toString(this.motors) + "\n Neighbor : " + " \n LeftNeighbor : " + leftNeighbor.id + " \n RightNeighbor : " + rightNeighbor.id + " \n ";
    }
}


public class Main {
    private static final int N = 5;
    private static Mechaniker[] mechanikerArray = new Mechaniker[N];

    public static void main(String[] args) {
        Schluessel l = new Schluessel();
        Schluessel r = new Schluessel();

        for (int i = 0; i < mechanikerArray.length; i++) {
            Mechaniker h = new Mechaniker(l, r, N, i);
            mechanikerArray[i] = h;

            l = mechanikerArray[i].getRight();
            r = new Schluessel();

            if (i == mechanikerArray.length - 1) {
                mechanikerArray[i].setRight(mechanikerArray[0].getLeft());
            }
        }


        mechanikerArray[0].setRightNeighbor(mechanikerArray[mechanikerArray.length - 1]);
        mechanikerArray[0].setLeftNeighbor(mechanikerArray[1]);

        mechanikerArray[mechanikerArray.length - 1].setRightNeighbor(mechanikerArray[mechanikerArray.length - 2]);
        mechanikerArray[mechanikerArray.length - 1].setLeftNeighbor(mechanikerArray[0]);


        for (int i = 1; i < mechanikerArray.length - 1; i++) {
            Mechaniker h = mechanikerArray[i];

            h.setLeftNeighbor(mechanikerArray[i - 1]);
            h.setRightNeighbor(mechanikerArray[i + 1]);
        }

        Arrays.stream(mechanikerArray).toList().stream().forEach(System.out::println);

        System.out.println("end of Metadata work start !!  \n \n \n \n");


        long start_parallel = System.currentTimeMillis();

        Arrays.stream(mechanikerArray).toList().forEach(Thread::start);

        System.out.println("All Threads are running !!");

        for (int i = 1; i < mechanikerArray.length - 1; i++) {
            Mechaniker h = mechanikerArray[i];

            try {
                h.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long finish_parallel = System.currentTimeMillis();

        long timeElapsed_parallel = finish_parallel - start_parallel;

        System.out.println("All Worker are done !! \n Worker done the work parallel. time needed : " + timeElapsed_parallel + " ms");

        long start_not_parallel = System.currentTimeMillis();

        System.out.println("\n \n \n \n");

        System.out.println("Worker start sequential !!");

        Arrays.stream(mechanikerArray).toList().forEach(Mechaniker::run);

        long finish_not_parallel = System.currentTimeMillis();

        long timeElapsed_not_parallel = finish_not_parallel - start_not_parallel;

        System.out.println("All Worker are done !! \n Worker done the work sequential. time needed : " + timeElapsed_not_parallel + " ms");

    }
}
