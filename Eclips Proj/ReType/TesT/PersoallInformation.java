package TesT;

public class PersoallInformation {
 private String firstName;
 private String lasteName;
 private String acountNumber;
 private String nationalyty;
 public PersoallInformation(){}
 public PersoallInformation(String firstName, String lasteName, String acountNumber, String nationalyty) {
	super();
	this.firstName = firstName;
	this.lasteName = lasteName;
	this.acountNumber = acountNumber;
	this.nationalyty = nationalyty;
}
@Override
 public String toString() {
	return "PersoallInformation [firstName=" + firstName + ", lasteName=" + lasteName + ", acountNumber=" + acountNumber
			+ ", nationalyty=" + nationalyty + "]";
}
 
}
