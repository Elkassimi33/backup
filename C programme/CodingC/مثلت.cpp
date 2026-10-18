#include <stdio.h>
int main()
{
    int i,j,a,b;

    printf("donner 2 entiers la dim de triangle : ");
    scanf("%d",&a);
    printf("\n");
    for(i=1; i<=a; i++)
    {
    for(j=a; j>i; j--)
        {
            printf(" ");
        }
        for(j=-i; j<i-1; j++)
        {
            printf("*");
        }
        printf("\n");
    }
}
    