package part2;

public class Thread1 extends Thread {
	public void run() {
		System.out.println("insid the thread.");
	}
	public static void main(String[] args)   {
		Thread1 thread=new Thread1();
		thread.start();
		try{
			sleep(3000);
		}
		catch(InterruptedException e) {} 
		System.out.println("fh");
	
	}

}
