package part2;

public class ImpRunnable implements Runnable {
	static int i=0;
	public void run() {
		i++;
	}
	public static void main(String[] args) {
		ImpRunnable obj=new ImpRunnable();
		Thread thread=new Thread(obj);
		thread.start();
		while(thread.isAlive())
			System.out.println("waiting...");
		System.out.println(i);
		i++;
		System.out.println(i);
		
	}

}
