import java.util.ArrayList;

class Parkhaus_25
{
    private int counter = 1;
    private int currentTicket = 1;
    private int ticket  = 1;


    public synchronized void park(Auto_25 auto)
    {
        auto.ticket = this.ticket++;

        while (counter == 0 || auto.ticket != this.currentTicket)
        {
            System.out.println(Thread.currentThread().getName() + " try to park ---->");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        counter--;

        System.out.println("name: " + Thread.currentThread().getName()+ "ticket number : " + auto.ticket + " : park");
    }

    public synchronized void leave()
    {
        System.out.println("name: " + Thread.currentThread().getName()+ "ticket number : " + currentTicket + " : leave");
        this.counter++;
        this.currentTicket++;
        notifyAll();
    }
}



class Auto_25 extends Thread
{
    private Parkhaus_25  parkhaus;
    public int ticket;

    @Override
    public void run()
    {
        for (int i = 0; i < 4; i++)
        {
            this.rumfahren();
            this.park();
        }
    }

    public Auto_25(String name, Parkhaus_25 parkhaus)
    {
        super(name);
        this.parkhaus = parkhaus;
    }

    public void rumfahren()
    {
        int time = (int)(Math.random() * 20000);

        System.out.println(getName() + " faehrt for " + time + " s rum");

        try {
            sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void park()
    {
        int time = (int)(Math.random() * 20000);

        parkhaus.park(this);

        System.out.println(getName() + " parkt foer " + time);

        try {
            sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        parkhaus.leave();
    }
}


public class Aufgabe_25
{
    public static void main(String[] args)
    {
        int N = 5;
        Parkhaus_25 parkhaus = new Parkhaus_25();
        ArrayList<Auto_25> autos = new ArrayList<>();
        for (int i = 0; i < N; i++)
        {
            var name ="Auto | " + i + " | ";
            Auto_25 auto = new Auto_25(name, parkhaus);
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
