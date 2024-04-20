


class MyTimer
{
    private int timer;
    private Augabe_08 worker;

    public MyTimer(int time, Augabe_08 worker)
    {
        this.timer = time;
        this.worker = worker;
        this.start();
    }

    public void start()
    {
        this.worker.start();

        System.out.println("start Timer " + this.timer + "ms");

        try {
            this.worker.join(this.timer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Time done: " + this.timer + "ms");

        if(!this.worker.isInterrupted()){
            System.out.println("Thread will be interrupted !!");

            this.worker.interrupt();

            try {
                this.worker.join();
                System.out.println("Thread is interrupted !!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else
        {
            System.out.println("Thread is dead !!");
        }

    }

}




public class Augabe_08 extends Thread
{
    @Override
    public void run() {

        while (true) {
            if(isInterrupted()){
                break;
            }
        }

    }

    public static void main(String[] args) {
        Augabe_08 a = new Augabe_08();

        MyTimer t = new MyTimer(9000, a);

    }
}
