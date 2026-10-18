#include<stdio.h>
main(){
int x1,x2,x3,x4,z=0;
for(int i=0;i<100;i++)
	for(int j=0;j<100;j++)
		for(int k=0;k<100;k++)
			for(int t=0;t<100;t++){
				int s;
				s=2*i+3*j-k+4*t;
					if(s>z&&(2*i+j+2*t)==6){
						z=s;
						x1=i;x2=j;x3=k;x4=t;
					}
			}
	printf("z=%d\tx1=%d\tx2=%d\tx3=%d\tx4=%d\t",z,x1,x2,x3,x4);	
			
}
