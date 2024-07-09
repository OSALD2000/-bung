import java.util.ArrayList;

class Barriere_24
{
    private int N;
    private int counter;

    public Barriere_24(int N)
    {
        this.N = N;
        this.counter = 0;
    }


    public synchronized void register(String name)
    {
        counter+=1;
        while (counter < N && counter > 0)
        {
            System.out.println("Worker : " + name + " : " + counter + " wait for other worker  -----> ");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (counter == N)
        {
            counter = 0;
            System.out.println("Worker : " + name + " : " + counter + " notify all worker");
            notifyAll();
        }
        System.out.println("Worker : " + name + " : " + counter + " leave barriere <------- ");
    }
}

class Worker_24 extends Thread
{
    private Barriere_24 barriere;
    private String name;

    @Override
    public void run()
    {
        for (int i = 0; i < 4; i++)
        {
            int time = (int)(Math.random() * 30000);
            int time1 = (int)(Math.random() * 30000);

            System.out.println(getName() + " work before barriere for " + time);

            try {
                sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            barriere.register(getName());

            System.out.println(getName() + " work after barriere for " + time1);
       }
    }

    public Worker_24(Barriere_24 barriere, String name )
    {
        super(name);
        this.barriere = barriere;
    }
}


public class Aufgabe_24
{
    public static void main(String[] args)
    {
        Barriere_24 barriere = new Barriere_24(5);
        ArrayList<Worker_24> workers = new ArrayList<Worker_24>();

        for (int i = 0; i < 5; i++)
        {
            Worker_24 worker = new Worker_24(barriere, "Worker " + i);
            workers.add(worker);
        }

        workers.forEach(Thread::start);

        workers.forEach(worker24 ->
        {
            try {
                worker24.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
