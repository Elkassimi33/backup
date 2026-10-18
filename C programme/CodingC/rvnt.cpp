#include<stdio.h>
float impot(int r,int n){
if(r/n<500)
return r*0.1;
return r*0.2;}
float revenunet (int r,int n){
return r-impot(r,n);}
int main() {
    int r,n;
    float re;
    printf ("entre le nombre des entie : ");
    scanf("%d",&r);
    printf ("entre un entie : ");
    scanf("%d",&n);
    re=revenunet(r,n);
    
    printf("le maximum est : %f",re);
    
}