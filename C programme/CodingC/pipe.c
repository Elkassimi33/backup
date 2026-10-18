#include<stdio.h>
#include<stdlib.h>
#include<fcntl.h>
#include<unistd.h>
typedef struct person{
	int age;
	char nom[20];
}p;
int main(void){
	int n=open("fichpersonne.text",O_RDWR|O_CREAT|O_TRUNC);
	int s1[2];
	pipe(s1);
	int i=fork();
	p per[4];
	if(i!=0){
		int s2[2];
		pipe(s2);
		int j=fork();
		if(j!=0){
			int k;
			printf("remplire le tableau\n");
			for(k=0;k<4;k++){
				printf("entrez le nom: ");
				scanf("%s",per[k].nom);
				printf("entrez l'age: ");
				scanf("%d",&per[k].age);
			}
			for(k=0;k<4;k++){
				if(per[k].age<20){
					write(s2[1],&per[k],sizeof(p));
				}
				else{
					write(s1[1],&per[k],sizeof(p));
				}
			}
			close(s1[1]); close(s2[1]);
		}else if(j==0){
			p pers;
			while(read(s2[0],&pers,sizeof(p))>0){
				write(n,&pers,sizeof(p));
			}
			close(s2[0]);		}
	}else if(i==0){
		p pers;
		while(read(s1[0],&pers,sizeof(p))>0){
			write(n,&pers,sizeof(p));
		}
		close(s1[0]);
	}
return 0;
}

