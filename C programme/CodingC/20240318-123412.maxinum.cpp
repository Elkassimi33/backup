#include<stdio.h>
int main() {
    int a,i,n,t;
    printf ("entre le nombre des entie : ");
    scanf("%d",&t);
    printf ("entre un entie : ");
    scanf("%d",&a);
    for (i=1; i<=t; i++)
    {
        printf ("enter le %d entie : ",i);
        scanf("%d",&n);
        if(a>=n) {
            a=a;
        }
        else {
            a=n;
        }

    }
    
    printf("le maximum est : %d",a);

}