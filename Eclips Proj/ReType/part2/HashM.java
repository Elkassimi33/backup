package part2;
import java.util.HashMap;
public class HashM {

	public static void main(String[] args) {
		HashMap<Integer,String> test=new HashMap<Integer,String>();
		test.put(1, "a");
		test.put(3, "b");
		test.put(6, "c");
		test.put(2, "d");
		test.put(4, "e");
		test.put(5, "f");
		for(String i:test.values())
			System.out.println(i);
	}

}
