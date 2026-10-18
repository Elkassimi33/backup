#include <stdlib.h>
#include <stdio.h>
int main()
{
    int I,J,K,L;
    do
    {   printf("donner 2 entiers I>1 et J>I \n");
        scanf("%d%d",&I,&J);
    } while((I<=1)||(J<=I));
    for(K=I; K<=J; K++)
    {   for(L=1; L<=10; L++)
            printf("%d*%d=%d\n",K,L,K*L);
        printf("\n\n\n");

    }
}
