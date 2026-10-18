package part2;

import java.util.Objects;

public class OuterClass{
	final private int a,b;
    public OuterClass(int a,int b) {
    	this.a=a;
    	this.b=b;
    }
    public void aff() {
    System.out.println("a="+a+" b="+b);
    
       }
    public int getA() {
		return a;
	}
	public int getB() {
		return b;
	}
	class InnerClass{
    	int c,d;
    	   public InnerClass(int a,int b) {
    	    	this.c=a;
    	    	this.d=b;
    	    }
    	   
    }
/*@Override
public int hashCode() {
	return Objects.hash(a, b);
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	OuterClass other = (OuterClass) obj;
	return a == other.a && b == other.b;
}*/
}