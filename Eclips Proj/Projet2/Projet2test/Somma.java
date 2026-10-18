package Projet2test;

import java.util.Scanner;

public class Somma {

	public static void main(String[] args) {
		Scanner lire=new Scanner(System.in);
		System.out.print("entrez le premier nombre:");
		String premierNombre=lire.nextLine();
		System.out.print("entrez la deuxiem nombre: ");
		String deuxiemNombre=lire.nextLine();
		lire.close();
		int taileResult=premierNombre.length()<deuxiemNombre.length()?premierNombre.length():deuxiemNombre.length();
		char[] result=new char[taileResult];
		char[] retenu= {'0','0'};
		for(int i=0;i<taileResult-1;i++) {
			retenu[1]=retenu[0];
			if((premierNombre.charAt(i)+deuxiemNombre.charAt(i)+retenu[1])=='1'+'1'+'1') {
				retenu[0]='1';
				result[i]='1';
			}
			else if((premierNombre.charAt(i)+deuxiemNombre.charAt(i)+retenu[1])=='1'+'1'+'0') {
				retenu[0]='1';
				result[i]='0';	
			}
			else if((premierNombre.charAt(i)+deuxiemNombre.charAt(i)+retenu[1])=='1'+'0'+'0') {
				retenu[0]='0';
				result[i]='1';	
			}
			else if((premierNombre.charAt(i)+deuxiemNombre.charAt(i)+retenu[1])=='0'+'0'+'0') {
				retenu[0]='0';
				result[i]='0';	
			}
		}
		System.out.print("la resultat est: ");
		for(char i:result)
			System.out.print(i);
		if(retenu[0]==retenu[1])
			System.out.println("on a pas un depordoment.");
		else
			System.out.println("on a un depordoment.");
	}
}
