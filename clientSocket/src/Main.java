
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Main {
    public static void main(String[] args) {

        try(Socket socket = new Socket("localhost",1234)) {

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Person person = (Person) in.readObject();
            System.out.println("received data from server: " + person);

            var person2=new Person("d","e","f");

            out.writeObject(person2);
            out.flush();

            System.out.println("sent data to server: " + person2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}