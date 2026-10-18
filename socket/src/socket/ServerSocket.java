package socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class ServerSocket {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newCachedThreadPool();

        AtomicInteger count = new AtomicInteger(0);

        try(java.net.ServerSocket serverSocket = new java.net.ServerSocket(1234);){
            System.out.println("Server started");
            while(true){
                Socket socket = serverSocket.accept();

               executor.execute(()->{
                   try {

                       ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                       ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                       Person person = new Person("a", count.incrementAndGet(), "c");

                       out.writeObject(person);
                       out.flush();
                       System.out.println("Message sent"+person);
                       Person read = (Person) input.readObject();
                       System.out.println("Message received " + read);
                       socket.close();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}