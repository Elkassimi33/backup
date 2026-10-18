package treads;

public class Interrupt {

    public void talk(){
        try {
            Thread.sleep(10000);
        }catch (InterruptedException e){
            System.out.println("how rude");
        }
    }

    public static void main(String[] args)  {

        var i = new Interrupt();
        var t =  new Thread(i::talk);
        t.start();
        t.interrupt();
        t.interrupt();
        t.interrupt();

    }
}
