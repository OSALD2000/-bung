import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

class Resource {
    private Long resource;
    private Semaphore semaphore;


    private boolean done;
    private boolean filled;

    public Resource() {
        this.filled = false;
        this.done = false;
        semaphore = new Semaphore(1, true);
    }

    public boolean write(Long resource) {
        boolean operated = false;

            if (!this.isFilled()) {
                this.resource = resource;
                int time = (int) (Math.random() *  1000);
                System.out.println("Value  " + resource + " ist in Resource geschrieben!!");
                System.out.println("writing ----> " + this.resource + " time " + time);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.setFilled(true);
                operated = true;

            }
            return operated;


    }

    public Long read() {
            Long data = null;

            if (this.isFilled()) {
                data = this.resource;
                int time = (int) (Math.random() *  1000);
                System.out.println("Verbraucer hat " + data + " in Resource gebraucht!!");
                System.out.println("reading ----> " + this.resource + " time " + time);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                this.setFilled(false);
            }
            return data;
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
    private ArrayList<Long> items;

    @Override
    public void run() {
        for (Long i : this.items)
        {
            while (!r.write(i)) /*wait*/;
        }
        r.setDone(true);
        System.out.println("Erzeuger ist fertig!!");
    }

    public Erzeuger(Resource r, int numberOfItem) {
        this.r = r;
        this.items = new ArrayList<>() {{
            for (long i = 0; i < numberOfItem; i++) {
                add(i);
            }
        }};
        System.out.println("Erzeuger elements !!");
        this.items.forEach(i -> System.out.print(i + " "));
        System.out.println();
    }

    private long getRandomNumber() {
        return (long) (Math.random() * 100);
    }
}

class Verbraucer extends Thread {

    private Resource r;
    private ArrayList<Long> items;

    @Override
    public void run()
    {
        long versuch = 0L;
        while (!r.isDone())
        {
            Long i = r.read();
            if(i != null)
            {
                this.items.add(i);
            }else System.out.println("no Resources found try number :: " + versuch++);
        }

        while (r.isFilled())
        {
            Long i = r.read();
            if(i != null)
            {
                this.items.add(i);
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
        Erzeuger e = new Erzeuger(r , 11);

        e.start();
        v.start();
    }
}
