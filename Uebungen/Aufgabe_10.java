import java.lang.reflect.Array;
import java.util.ArrayList;

class Flag
{
    public volatile static int turn;
    public volatile static boolean[] flags = {false, false};
}


public class Aufgabe_10 extends Thread
{
    static int val = 0;

    private int N;


    public Aufgabe_10(String name, int N){
        super(name);
        this.N = N;
    }

    private void increment(){
        Aufgabe_10.val += 1;
    }

    public void sum_up(){
        for (int i = 0; i < 3000000; i++)
        {
            Flag.flags[N] = true;
            Flag.turn = N == 0 ? 1 : 0;

            while (Flag.flags[N==0 ? 1 : 0] && Flag.turn == (N == 0 ? 1 : 0)) /*Wait*/;

            this.increment();

            Flag.flags[N] = false;
        }
    }

    @Override
    public void run(){

        System.out.println("sum up start for Thread " + this.getName());

        this.sum_up();

    }


    public static void main(String[] args) throws InterruptedException {

        System.out.println("Haupt Thread !!");
        System.out.println("Start sum_up Haupt Thread");


        Aufgabe_10 haupt_thread = new Aufgabe_10("Haupt Thread", 0);

        Aufgabe_10 zweite_Thread = new Aufgabe_10("zweite Thread", 1);


        zweite_Thread.start();
        haupt_thread.start();


        System.out.println("zweite Thread !!");
        System.out.println("Start sum_up zweite Thread");


        haupt_thread.join();
        zweite_Thread.join();

        System.out.println("val:" + val);

    }
}
