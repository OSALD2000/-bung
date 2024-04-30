import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

class Resource {
    private Integer resource;
    private Semaphore semaphore;


    private boolean done;
    private boolean filled;

    public Resource() {
        this.filled = false;
        this.done = false;
        semaphore = new Semaphore(1, true);
    }

    public synchronized boolean write(Integer resource) {
        boolean operated = false;

        try {
            this.semaphore.acquire();
            if (!this.isFilled()) {
                this.resource = resource;
                int time = (int) (Math.random() * 500);
                System.out.println("writing ----> " + this.resource + " need time : " + time);
                Thread.sleep(time);
                this.setFilled(true);
                operated = true;
                System.out.println("Value  " + resource + " ist in Resource geschrieben!!");
            }
            this.semaphore.release();
            return operated;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized Integer read() {
        Integer data = null;
        try {
            this.semaphore.acquire();
            if (this.isFilled()) {
                data = this.resource;
                this.setFilled(false);
            }
            this.semaphore.release();
            return data;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public synchronized boolean isFilled() {
        return this.filled;
    }

    public synchronized void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}


class Erzeuger extends Thread {

    private Resource r;
    private ArrayList<Integer> items;

    @Override
    public void run() {
        for (Integer i : this.items)
        {
            while (!r.write(i)) /*wait*/;
        }
        r.setDone(true);
        System.out.println("Erzeuger ist fertig!!");
    }

    public Erzeuger(Resource r, int numberOfItem) {
        this.r = r;
        this.items = new ArrayList<>() {{
            for (int i = 0; i < numberOfItem; i++) {
                add(getRandomNumber());
            }
        }};
        System.out.println("Erzeuger elements !!");
        this.items.forEach(i -> System.out.print(i + " "));
        System.out.println();
    }

    private int getRandomNumber() {
        return (int) (Math.random() * 100);
    }
}

class Verbraucer extends Thread {

    private Resource r;
    private ArrayList<Integer> items;

    @Override
    public void run()
    {
        while (!r.isDone())
        {
            Integer i = r.read();
            if(i != null)
            {
                this.items.add(i);
                System.out.println("Verbraucer hat " + i + " in Resource gebraucht!!");
            }
        }

        System.out.println("Verbraucer ist fertig!!");
        System.out.println("bearbeitete Resourcen : ");
        this.items.forEach(i -> System.out.print(i + " "));
    }

    public Verbraucer(Resource r) {
        this.r = r;
        this.items = new ArrayList<>();
    }

}


public class Aufgabe_14 {

    public static void main(String[] args) {
        Resource r = new Resource();
        Verbraucer v = new Verbraucer(r);
        Erzeuger e = new Erzeuger(r , 250);

        e.start();
        v.start();
    }
}
