#include <iostream>
#include<stdio.h>
int main(){
int a;
printf (" une fille: 1 \n un garçon: 2\n deux garçons: 3\n deux filles:4\n une fille et un garçon: 5\n");
printf ("\n\n");
printf ("entrer votre cas\n");
scanf("%d",&a);
switch (a){
case 1:
printf ("la couleur de cadeaux est : rose\n");
break;
case 2:
printf ("la couleur de cadeaux est : bleu\n");
break;
case 3:
printf ("la couleur des cadeaux est : bleu et jaune\n");
break;
case 4:
printf ("la couleur des cadeaux est : rose et orange\n");
break;
case 5:
printf ("la couleur des cadeaux est : bleu et rose\n");
break ;
default :
printf("shit\n");
break;
}
return 0;

}
