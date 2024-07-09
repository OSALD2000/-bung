import java.util.ArrayList;

class Parkhaus_22
{
    private int counter = 1;

    public synchronized void park()
    {
        while (counter == 0)
        {
            System.out.println(Thread.currentThread().getName() + " try to park ---->");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        counter--;

        System.out.println(Thread.currentThread().getName() + ": park");
    }

    public synchronized void leave()
    {
        System.out.println(Thread.currentThread().getName() + ": leave");
        counter++;
        notify();
    }
}



class Auto_22 extends Thread
{
    private Parkhaus_22  parkhaus;

    @Override
    public void run()
    {
        for (int i = 0; i < 4; i++)
        {
            this.rumfahren();
            this.park();
        }
    }

    public Auto_22(String name, Parkhaus_22 parkhaus)
    {
        super(name);
        this.parkhaus = parkhaus;
    }

    public void rumfahren()
    {
        int time = (int)(Math.random() * 10000);

        System.out.println(getName() + " faehrt for " + time + " s rum");

        try {
            sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void park()
    {
        int time = (int)(Math.random() * 10000);

        parkhaus.park();

        System.out.println(getName() + " parkt foer " + time);

        try {
            sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        parkhaus.leave();
    }
}


public class Aufgabe_22
{
    public static void main(String[] args) {
        int N = 5;
        Parkhaus_22 parkhaus = new Parkhaus_22();
        ArrayList<Auto_22> autos = new ArrayList<>();
        for (int i = 0; i < N; i++)
        {
            var name ="Auto | " + i + " | ";
            Auto_22 auto = new Auto_22(name, parkhaus);
            autos.add(auto);
        }

        autos.forEach(Thread::start);


        autos.forEach(auto ->
        {
            try {
                auto.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
