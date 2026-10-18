#include<stdio.h>
#include<stdlib.h>
int main()
{   int b,a,r, c;
    
   
    printf("entre a:  ");
    scanf("%d",&a);
     printf ("entre op:  ");
    scanf (" %c",&c);
    printf ("enter b:  ");
    scanf ("%d",&b);
    switch (c) {
    case '+' :
        r=a+b;
        printf("result=%d",r);
        break;
    case '-':
        r=a-b;
        printf("result=%d",r);
        break;
    case '*':
        r=a*b;
        printf("result=%d",r);
        break;
    case '/' :
        if (b!=0) {
            r=a/b;
            printf("result=%d",r);
        }
        else printf("impossible de devisé sur 0");
        break;
    default:
        printf("incorrect operation");
        break;
    }


}
