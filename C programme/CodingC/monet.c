#include <stdio.h>
#include<stdlib.h>
float impot(int r,int n){
if(r/n<500)
return r*0.1;
return r*0.2;
}
float revenuNet(int r,int n){
return r-impot(r,n);
}
int main (){
int r,n;
float revenunet;
printf ("entre le revenu total et nombre de personnes : ");
scanf ("%d",&r);
printf ("entre le nombre de personnes : ");
scanf ("%d",&n);
revenunet=revenuNet(r,n);
printf ("le revenu net est %.2fDA",revenunet);
return 0;
}