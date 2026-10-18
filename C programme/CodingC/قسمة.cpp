#include<stdio.h>
#include<stdlib.h>
int main() {
    int a,i,j,b,s,c;
    printf ("entre a  : ");
    scanf("%d",&a);
    printf ("entre b : ");
    scanf("%d",&b);
    i=0;

    if(a<=0&&b<=0||a>=0&&b>=0) {
        a=abs(a);
        b=abs(b);
        if(b>a)
            s=b;
        else {
            c=a;
            a=b;
            b=c;
            s=b;
        }
        while(s>=0) {
            s-=a;
            i++;
        }
        printf ("la division de %d/%d donne la Q=%d et R=%d",b,a,i-1,s+a);
    }
    else {
        a=abs(a);
        b=abs(b);
        if(b>a)
            s=b;
        else {
            c=a;
            a=b;
            b=c;
            s=b;
        }
        while(s>=0) {
            s-=a;
            i++;
        }
        printf ("la division de - %d/%d donne la Q=-%d et R=-%d",b,a,i-1,s+a);
    }


}

    