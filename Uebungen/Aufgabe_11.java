import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class Auto extends Thread
{
    public final int id;
    public final ParkPlace parkPlace;
    public final int parkTime;

    public void run()
    {
        driveAround();

        park();
    }

    public Auto(int id, ParkPlace parkPlace)
    {
        this.id = id;
        this.parkPlace = parkPlace;
        this.parkTime = (int) (Math.random() * 2000);
    }

    private void driveAround()
    {
        try {
            int time = (int) (Math.random() * 1000);
            System.out.println("Auto "+id+" drive around for " + time);
            sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void park()
    {
        try {
            this.parkPlace.acquire(this);

            this.parkPlace.parkAuto(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            parkPlace.release();
        }

    }

}


class ParkPlace
{

    private Semaphore semaphore;
    private Auto auto;

    public ParkPlace(int permits, boolean fair) {
        semaphore = new Semaphore(permits, fair);
    }

    public void parkAuto(Auto auto)
    {
            System.out.println("Auto " + auto.id + " park  for " + auto.parkTime);

            this.auto = auto;
            try {
                Thread.sleep(auto.parkTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Auto " + auto.id + " left park place");
    }

    public void acquire(Auto auto) throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }

}



public class Aufgabe_11
{
    public final static int N = 3;
    private static final ArrayList<Auto> autos = new ArrayList<>();

    public static void main(String[] args)
    {
        ParkPlace parkPlace = new ParkPlace(N, false);

        for(int i = 0; i < 25; i++)
        {
            autos.add(new Auto(i, parkPlace));
        }

        autos.forEach(Auto::start);
    }

}
