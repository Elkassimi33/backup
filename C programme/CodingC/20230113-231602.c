#include<stdio.h>
#include<stdlib.h>
void pemut(float *a,float *b) {
    float temp;
    temp=*a;
    *a=*b;
    *b=temp;
}
mai() {
    int tab[10] ={1,2,5,7,12,13,6,15,17,8};
    int i;
    for(i=0; i<9; i++) {
        if(tab[i+1]<tab[i]);
        permut(&tab[i],&tab[i+1]);
    }

}
