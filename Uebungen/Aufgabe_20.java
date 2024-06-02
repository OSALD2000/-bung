import java.util.Arrays;
import java.util.Random;


class Schluessel_20
{

    private boolean benutzt;
    public final int id;

    public Schluessel_20() {
        id = (int) (Math.random() * 100);
        this.benutzt = false;
    }

    public synchronized boolean isBenutzt() {
        return benutzt;
    }

    public synchronized void setBenutzt(boolean benutzt) {
        this.benutzt = benutzt;
    }

    public synchronized void takeSchluessel(boolean left, int mechanikerID, Motor_20 motor, Mechaniker_20 neighbor)
    {
        if(isBenutzt()){
            try {
                if(left)
                {
                    System.out.println("Mechaniker " + mechanikerID+ " work with motor : " + motor + "watied until neighbor right ::" + neighbor.id + "  is done with left :: " + this);
                }else
                {
                    System.out.println("Mechaniker " + mechanikerID + " work with motor : " + motor + "watied until neighbor left :: " + neighbor.id + " is done with right :: " + this);
                }
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        setBenutzt(true);
    }

    public synchronized void leftSchluessel()
    {
        this.notifyAll();
    }
    @Override
    public String toString() {
        return "Schluessel id : " + this.id;
    }
}

class Motor_20 {
    private final boolean hard;
    private final int time_needed;


    public Motor_20() {
        Random r = new Random();

        this.hard = r.nextInt(100 - 1) + 1 <= 40;;

        this.time_needed = (int) (Math.random() * 200);
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

class Mechaniker_20 extends Thread {

    private Schluessel_20 left;
    private Schluessel_20 right;
    private Mechaniker_20 leftNeighbor;
    private Mechaniker_20 rightNeighbor;

    private Motor_20[] motors;

    public final int id;



    public Mechaniker_20(Schluessel_20 left, Schluessel_20 right, int workLoad, int id) {
        this.motors = Arrays.stream(new Motor_20[workLoad]).toList().stream().map(m -> new Motor_20()).toArray(Motor_20[]::new);
        this.left = left;
        this.right = right;
        this.id = id;
    }


    @Override
    public void run() {

        System.out.println("Mechaniker " + this.id + " start to work " + " Neighbors : " + " LeftNeighbor : " + leftNeighbor.id + " RightNeighbor : " + rightNeighbor.id);

        for (int i = 0; i < motors.length; i++) {

            Motor_20 motor = this.motors[i];
            System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor);

            if (motor.isHard()) {
                left.takeSchluessel(true, this.id, motor, rightNeighbor);
                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "take left :: " + left);

                right.takeSchluessel(false, this.id, motor, leftNeighbor);
                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "take right :: " + right);


                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + " Work Done");

                try {
                    int time = motor.getTime_needed();
                    System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + " need : " + time + " ms");
                    sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                left.leftSchluessel();
                right.leftSchluessel();
            }
            else
            {
                left.takeSchluessel(true, this.id, motor, rightNeighbor);
                System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + "take left :: " + left);

                try {
                    int time = motor.getTime_needed();
                    System.out.println("Mechaniker " + this.id + " work with motor : " + i + " " + motor + " need : " + time + " ms");
                    sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                left.leftSchluessel();
            }

            System.out.println("Mechaniker " + this.id + " Work Done");
        }
    }


    public Schluessel_20 getRight() {
        return right;
    }

    public void setRight(Schluessel_20 right) {
        this.right = right;
    }

    public Schluessel_20 getLeft() {
        return left;
    }

    public void setLeft(Schluessel_20 left) {
        this.left = left;
    }


    public Mechaniker_20 getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(Mechaniker_20 leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Mechaniker_20 getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(Mechaniker_20 rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    @Override
    public String toString() {
        return "Mechaniker : \n id : " + this.id + " \n Left : " + this.left.id + " \n Right : " + this.right.id + " \n Motors : \n" + Arrays.toString(this.motors) + "\n Neighbor : " + " \n LeftNeighbor : " + leftNeighbor.id + " \n RightNeighbor : " + rightNeighbor.id + " \n ";
    }
}


public class Aufgabe_20 {
    private static final int N = 8;
    private static Mechaniker_20[] mechanikerArray = new Mechaniker_20[N];

    public static void main(String[] args) {
        Schluessel_20 l = new Schluessel_20();
        Schluessel_20 r = new Schluessel_20();

        for (int i = 0; i < mechanikerArray.length; i++) {
            Mechaniker_20 h = new Mechaniker_20(l, r, N, i);
            mechanikerArray[i] = h;

            l = mechanikerArray[i].getRight();
            r = new Schluessel_20();

            if (i == mechanikerArray.length - 1) {
                mechanikerArray[i].setRight(mechanikerArray[0].getLeft());
            }
        }


        mechanikerArray[0].setRightNeighbor(mechanikerArray[mechanikerArray.length - 1]);
        mechanikerArray[0].setLeftNeighbor(mechanikerArray[1]);

        mechanikerArray[mechanikerArray.length - 1].setRightNeighbor(mechanikerArray[mechanikerArray.length - 2]);
        mechanikerArray[mechanikerArray.length - 1].setLeftNeighbor(mechanikerArray[0]);


        for (int i = 1; i < mechanikerArray.length - 1; i++) {
            Mechaniker_20 h = mechanikerArray[i];

            h.setLeftNeighbor(mechanikerArray[i - 1]);
            h.setRightNeighbor(mechanikerArray[i + 1]);
        }

        Arrays.stream(mechanikerArray).toList().stream().forEach(System.out::println);

        System.out.println("end of Metadata work start !!  \n \n \n \n");


        long start_parallel = System.currentTimeMillis();

        Arrays.stream(mechanikerArray).toList().forEach(Thread::start);

        System.out.println("All Threads are running !!");

        for (int i = 1; i < mechanikerArray.length - 1; i++) {
            Mechaniker_20 h = mechanikerArray[i];

            try {
                h.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long finish_parallel = System.currentTimeMillis();

        long timeElapsed_parallel = finish_parallel - start_parallel;

        System.out.println("All Worker are done !! \n Worker done the work parallel. time needed : " + timeElapsed_parallel + " ms");

//        long start_not_parallel = System.currentTimeMillis();
//
//        System.out.println("\n \n \n \n");
//
//        System.out.println("Worker start sequential !!");
//
//        Arrays.stream(mechanikerArray).toList().forEach(Mechaniker::run);
//
//        long finish_not_parallel = System.currentTimeMillis();
//
//        long timeElapsed_not_parallel = finish_not_parallel - start_not_parallel;
//
//        System.out.println("All Worker are done !! \n Worker done the work sequential. time needed : " + timeElapsed_not_parallel + " ms");

    }
}
