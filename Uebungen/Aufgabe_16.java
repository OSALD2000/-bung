import java.util.ArrayList;
import java.util.concurrent.Semaphore;


class Barriere
{
    private Semaphore barrier;
    private Semaphore register;
    private int N;


    public Barriere(int N)
    {
        this.N = N;
        barrier = new Semaphore(N);
        register = new Semaphore(0);
    }


    public void startWork(Worker w)
    {
        try {
            barrier.acquire();

            if(barrier.availablePermits() == 0)
            {
                System.out.println("worker with name " + w.getName() + " complete the number needed to work " + this.N );

                register.release(N);
            }else
            {
                System.out.println("worker with name " + w.getName() + " Wartet auf andere Worker \n" + "Worker needed: " + barrier.availablePermits());
            }

            register.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void endWork()
    {
        register.release();
        if(register.availablePermits() == N)
        {
            this.cleanBarriere();
        }
    }

    public void printSStatus()
    {
        System.out.println("register : "+ register.availablePermits());
        System.out.println("barrier : "+ barrier.availablePermits());

    }

    private void cleanBarriere()
    {
        try {
            register.acquire(N);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        barrier.release(N);
    }

}

class Work
{
    private int N;
    private Barriere barriere;

    public Work(int N)
    {
        this.N = N;
        this.barriere = new Barriere(N);
    }


    public void work(Worker worker)
    {
            barriere.startWork(worker);

            System.out.println(worker.getName() + " start working !!");

            System.out.println("worker with name " + worker.getName() + " start to work all needed worker sind vorhanden");

            int time = (int) (Math.random() *  10000);

            System.out.println(worker.getName() + " need to finish: " + time);

            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(worker.getName() + " is done ");

            barriere.endWork();

            barriere.printSStatus();
    }
}

class Worker extends Thread
{
    private Work work;
    @Override
    public void run()
    {
        int time = (int) (Math.random() *  7000);
        System.out.println(this.getName() + " wartet time: " + time);
        try {
            sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        work.work(this);
    }

    public Worker(String name, Work work)
    {
        super(name);
        this.work = work;
    }
}


public class Aufgabe_16
{
    public static void main(String[] args) {
        int N = 5;
        Work w = new Work(N);
        for (int i = 0; i < N * 3; i++)
        {
            (new Worker(("WORKER "+i), w)).start();
        }
    }
}


