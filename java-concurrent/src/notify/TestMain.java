package notify;


public class TestMain {

    public static void main(String [] args) throws InterruptedException {

        Data data = new Data();

        Thread sender = new Thread(new Sender(data));
        Thread receiver = new Thread(new Receiver(data));

//        sender.setDaemon(true);
//        receiver.setDaemon(true);

        sender.start();
        receiver.start();

        sender.join(1000);
        receiver.join(1000);

    }
}
