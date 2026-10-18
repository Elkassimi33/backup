package TesT;

public class Main {

	public static void main(String[] args) {
		Departement dept =new Departement(12,"math");
	 Employes emp =new Employes(111, 2465, "youssef", "el kassimi"," 13A", "moroccan", dept);
     System.out.println(emp.toString());
	}

}
