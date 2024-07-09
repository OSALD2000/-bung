import java.time.LocalDate;
import java.util.Random;
import java.time.temporal.ChronoUnit;

class DateBuffer
{
    private LocalDate[] dates;
    private boolean done = false;

    public DateBuffer(int N)
    {
        dates = new LocalDate[1];
    }

    public synchronized LocalDate read()
    {
        while (dates[0] == null)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(isDone())
            {
                return null;
            }
        }

        LocalDate date = dates[0];
        dates[0] = null;
        notifyAll();
        return date;
    }

    public synchronized  void write(LocalDate d)
    {
        while (dates[0] != null)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        dates[0] = d;
        notifyAll();
    }

    public synchronized void setDone(boolean done)
    {
        this.done = done;
        notifyAll();
    }
    public synchronized boolean isDone()
    {
        return this.done;
    }

}

class Producer extends Thread
{
    private DateBuffer dateBuffer;
    private int N;

    @Override
    public void run()
    {
        for (int i = 0; i < this.N; i++)
        {
            Random random = new Random();

            LocalDate startDate = LocalDate.now().minusYears(10);
            LocalDate endDate = LocalDate.now();
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            LocalDate randomDate = startDate.plusDays(random.nextInt((int) days + 1));

            System.out.println(randomDate.toString());

            dateBuffer.write(randomDate);
        }
        dateBuffer.setDone(true);
    }

    public Producer(DateBuffer dateBuffer, int N)
    {
        this.N = N;
        this.dateBuffer = dateBuffer;
    }
}

class Consumer extends Thread
{
    private DateBuffer dateBuffer;

    @Override
    public void run()
    {
        while (true)
        {
            LocalDate l = dateBuffer.read();
            if(l == null)
            {
                break;
            }
            System.out.println(l.toString());
        }
    }

    public Consumer(DateBuffer dateBuffer)
    {
        this.dateBuffer = dateBuffer;
    }
}


public class Aufgabe_23
{
    public static void main(String[] args)
    {
        DateBuffer d = new DateBuffer(1);
        Producer producer = new Producer(d, 10);
        Consumer consumer = new Consumer(d);
        producer.start();
        consumer.start();
    }
}
