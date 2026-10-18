package TesT;

public class Employes {
	private int id;
	private float salary;
	private PersoallInformation personalInfo;
	private Departement dept;

	public Employes(int id, float salary, String firstName, String lasteName, String acountNumber, String nationalyty,
			Departement dept) {
		this.id = id;
		this.salary = salary;
		this.personalInfo = new PersoallInformation(firstName, lasteName, acountNumber, nationalyty);
		this.dept = dept;//agrigation
	}

	@Override
	public String toString() {
		return "Employes [id=" + id + ", salary=" + salary + ", personalInfo=" + personalInfo + ", deptno=" + dept.getDeptNo() + ",deptName="+dept.getDeptName()+"]";
	}
	

}
