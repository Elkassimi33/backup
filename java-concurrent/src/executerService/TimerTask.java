package executerService;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

public  class TimerTask implements Runnable {

    private  static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public static void main(String[] args){

//        TimerTask task = new TimerTask();
//
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
//
//        ScheduledFuture resultFuture = scheduledExecutorService.scheduleAtFixedRate(task,100,450, TimeUnit.MILLISECONDS);

        beepForAnHour();




    }

    public static void beepForAnHour() {
        Runnable beeper = () -> System.out.println("beep");
        ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(beeper, 0, 100, MILLISECONDS);
        Runnable canceller = () -> beeperHandle.cancel(false);
        scheduler.schedule(canceller, 1, SECONDS);
    }

    @Override
    public void run()  {
        System.out.println("null");
//        return "null";
    }
}
