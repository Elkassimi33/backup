package part2;
import java.io.*;

import javax.swing.JOptionPane;

public class Cfile {
	static {
System.loadLibrary("NativeLib");
	}
	
	public static void main(String[] args) throws IOException {
//		String s=JOptionPane.showInputDialog("enter the path and the file name");
//		if(s==null)
//			return;
//		C:\\Users\\elkay\\Desktop\\Sert\\file.txt
//		File file = null;
//		try{
//			file=new File(s);
//			if(file.exists()) {
//			System.out.println("This File is alredy exists.");
//			System.out.println("File name: " +file.getName());
//			System.out.println("File path: "+file.getAbsolutePath());
//			System.out.println("Writeable: "+file.canWrite());
//			System.out.println("Readable: "+file.canRead());
//			System.out.println("File size: "+file.length());
//			System.out.println("Exuteable: "+file.canExecute());
//			}
//		else {
//			System.out.println("File does not exists");
//			file.createNewFile();
//		}	
//		}	
//      catch(Exception ex) {
//    	  System.err.println("Unable to create File");
//		ex.printStackTrace();
//      }
//	file.delete();
		test();
	}
	private static native void test();
}
