package socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientSocket {

    public static void main(String[] args) {

        AtomicInteger count = new AtomicInteger(0);
        Executor executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Thread.currentThread().setName("ClientSocket %d".formatted(count.get()));
                try (Socket socket = new Socket("localhost", 1234)) {

                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                    Person person = (Person) in.readObject();
                    System.out.println("received data from server: " + person);

                    var person2 = new Person(Thread.currentThread().getName(), person.getCount(), "f");

                    out.writeObject(person2);
                    out.flush();

                    System.out.println("sent data to server: " + person2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
