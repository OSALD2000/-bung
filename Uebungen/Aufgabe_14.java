import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class Resource extends Semaphore
{
    private volatile Integer resource;
    private volatile boolean done = false;

    public Resource(int permits, boolean fair)
    {
        super(permits, fair);
    }


    public void schreibeResource(int wert)
    {
        try {
            this.acquire();
            resource = wert;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(wert + " ist in Speicherplazt!!");
        this.release();
    }

    public void verbraucherResource(ArrayList<Integer> arrayList)
    {
        try {
            this.acquire();
            arrayList.add(resource);
            System.out.println(resource + " wurde verbrauct !!");

            resource = null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.release();
    }

    public void setDone(boolean done)
    {
        this.done = done;
    }

    public Integer getResource(){
        return resource;
    }

    public boolean isDone() {
        return done;
    }
}

class Erzeuger extends Thread
{
    private Resource r;
    private int[] resourceArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

    @Override
    public void run()
    {
        for (int i : resourceArray)
        {

            while (r.getResource() != null)/*wait*/;
            System.out.println("Schreibe " + i + " in Resource ");
            r.schreibeResource(i);
        }

        r.setDone(true);

        System.out.println("Erzeuger Done");
    }

    public Erzeuger(Resource r){
        this.r = r;
    }


}

class Verbraucer extends Thread
{

    private Resource r;
    private ArrayList<Integer> arrayList;

    @Override
    public void run()
    {
            while (r.getResource() == null) /*wait*/;

            while(!r.isDone()){
                r.verbraucherResource(this.arrayList);
            }

            System.out.println("Verbraucher Done !!");
    }

    public Verbraucer(Resource r){
        this.r = r;
        this.arrayList = new ArrayList<>();
    }

}


public class Aufgabe_14
{

    public static void main(String[] args)
    {
        Resource r = new Resource(1, true);
        Verbraucer v = new Verbraucer(r);
        Erzeuger e = new Erzeuger(r);

        e.start();
        v.start();



    }
}
