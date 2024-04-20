public class Aufgabe_2 extends Thread {
    static int val = 0;

    public Aufgabe_2(String name) {
        super(name);
    }

    private void increment() {
        Aufgabe_2.val += 1;
    }

    public void sum_up() {
        for (int i = 0; i < 2000000; i++) {
            this.increment();
        }
    }

    @Override
    public void run() {
        System.out.println("sum up start for Thread " + this.getName());
        this.sum_up();
        System.out.println("val in zweite:" + val);
    }


    public static void main(String[] args) throws InterruptedException {

        System.out.println("Haupt Thread !!");
        System.out.println("Start sum_up Haupt Thread");

        Aufgabe_2 haupt_thread = new Aufgabe_2("Haupt Thread");

        haupt_thread.sum_up();


        System.out.println("zweite Thread !!");
        System.out.println("Start sum_up zweite Thread");

        Aufgabe_2 zweite_Thread = new Aufgabe_2("zweite Thread");

        zweite_Thread.start();

        zweite_Thread.join();

        System.out.println("val in Haupt:" + val);


    }
}
