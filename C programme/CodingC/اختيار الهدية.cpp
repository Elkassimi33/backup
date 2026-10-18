#include <stdio.h>
#include<stdlib.h>
int main() {
    char type ;
    printf ("qu'elle le type des enfants :");
    scanf(" %c",&type);
    switch (type) {
    case 'g':
        printf ("la couleur des cadeaux est: bleu\n ");
        break;
    case 'f':
        printf ("la couleur des cadeaux est: rose\n");
        break;
    case 'n':
        printf ("la couleur des cadeaux est: bleu et jaune\n");
        break;
    case 'b':
        printf ("la couleur des cadeaux est: bleu et rose\n");
        break;
    case 'r':
        printf ("la couleur des cadeaux est: rose et orange\n");
        break;
    default:
    printf ("تبا لك و لأمثالك\n");
    break;
    }
    return 0;
}