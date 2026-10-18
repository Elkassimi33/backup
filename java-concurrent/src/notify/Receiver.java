package notify;

import java.util.concurrent.ThreadLocalRandom;

public class Receiver implements Runnable{

    private final Data load;

    public Receiver(Data data) {
        this.load = data;
    }


    @Override
    public void run() {

        for (String receivedMessages = load.receive();
              !"End".equals(receivedMessages);receivedMessages=load.receive()){
            System.out.println(receivedMessages);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000,5000));
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
    }
}
