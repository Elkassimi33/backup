#include<stdio.h>
#include<stdlib.h>
int main()
{
//declaration
    int N,I,NES,J,K=1;
//lecture de N
    do
    {   printf("donner un entier N impair>=3\n");
        scanf("%d",&N);
    }
    while ((N%2==0)||(N<3));
//lecture de NES
    do
    {   printf("donner un entier NES>=2\n");
        scanf("%d",&NES);
    }
    while (NES<2);
//traitement
    for(I=1; I<=N; I++)
    {   if((I==1)||(I==N))
        {   for(J=1; J<=N; J++)
                printf("*");
            for(J=1; J<=NES; J++)
                printf(" ");
            for(J=1; J<=N; J++)
                printf("*");
            printf("\n");
        }



        if((I>1)&&(I<=N/2 +1))
        {   printf("*");
            for(J=1; J<=N-2; J++)
                printf(" ");
            printf("*");
            for(J=1; J<=NES; J++)
                printf(" ");
            for(J=1; J<=I-1; J++)
                printf(" ");
            for(J=1; J<=N-(I-1)*2; J++)
                printf("*");
            printf("\n");
        }
        if((I>N/2 +1)&&(I<N))
        {   K=K+2;
            printf("*");
            for(J=1; J<=N-2; J++)
                printf(" ");
            printf("*");
            for(J=1; J<=NES; J++)
                printf(" ");
            for(J=1; J<=N-I; J++)
                printf(" ");
            for(J=1; J<=K; J++)
                printf("*");
            printf("\n");
        }
    }
}