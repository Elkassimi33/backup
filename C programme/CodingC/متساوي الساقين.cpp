#include <stdio.h>
#include <stdlib.h>
int main() {
    int a,i,j,k;
    printf ("enter la dimension ");
    scanf ("%d",&a);
    for(i=0; i<a; i++)
    {
        for(k=a; k>i+1; k--)
            printf (" ");
        for(j=0; j<=i; j++)
            printf ("* ");
        printf ("\n");
    }

}