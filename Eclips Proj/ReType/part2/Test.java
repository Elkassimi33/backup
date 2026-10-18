package part2;

public class Test {

	public static void main(String[] args) {
	   OuterClass a=new OuterClass(0, 0);
		a=new OuterClass(1, 0);
		System.out.println(a.getA());
	}

}
