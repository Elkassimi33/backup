package notify;

public class Data {

    private String packet;
    private boolean transfer = true;

    public synchronized String receive(){
        while (transfer){
            try {
                wait();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted");
            }
        }
        transfer = true;
        String returnPacket = packet;
        notifyAll();
        return  returnPacket;
    }

    public synchronized void send(String packet){
        while (!transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted");
            }
        }
            transfer = false;

            this.packet=packet;
            notifyAll();

    }
}
