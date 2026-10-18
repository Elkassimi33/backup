#include<stdio.h>
#include<stdlib.h>
int main () {
    int  i,**t,**m,**p,pr,j,k,d;
    printf ("enter la dimension de la matrice ");
    scanf("%d",&d);
    t=(int**)malloc(d*sizeof(int*));
    p=(int**)malloc(d*sizeof(int*));
    m=(int**)malloc(d*sizeof(int*));
    for(i=0; i<d; i++) {
        *(t+i)=(int*)malloc(d*sizeof(int));
        for(j=0; j<d; j++) {
            printf ("entre t[%d][%d]\n",i,j);
            scanf ("%d",*(t+i)+j);
        }
    }
    printf ("\n");
    for(i=0; i<d; i++) {
        *(m+i)=(int*)malloc(d*sizeof(int));
        for(j=0; j<d; j++) {
            printf ("entre m[%d][%d]\n",i,j);
            scanf ("%d",*(m+i)+j);
        }
    }
    for(i=0; i<d; i++) {
        *(p+i)=(int*)malloc(d*sizeof(int));
        for(j=0; j<d; j++) {
            pr=0;
            for(k=0; k<d; k++) {
                pr+=t[i][k]*m[k][j];

            }
            *(*(p+i)+j)=pr;
        }
    }
    for(i=0; i<d; i++) {
        for(j=0; j<d; j++) {
            printf ("  %d",*(*(p+i)+j));

        }
        printf ("\n");
    }


}