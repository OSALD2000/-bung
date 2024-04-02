

public class Aufgabe_1 implements Runnable
{

    @Override
    public void run(){
        System.out.println("Sleep 0.5 Second");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Name in Thread : "+ Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName() + " ende !!");

    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[5];

        for (int i = 0; i < threads.length; i++) {
            String name = "Thread nummer " + i;

            Aufgabe_1 s = new Aufgabe_1();
            threads[i] = new Thread(s);

            threads[i].setName(name);

            System.out.println("Name : "+ threads[i].getName() + "; Id : " + threads[i].getId());

            threads[i].start();

            System.out.println("Sleep 1 Second");
            Thread.sleep(1000);
        }
    }
}