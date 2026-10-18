#include <stdio.h>
#include<stdlib.h>
typedef struct element {
    int vale;
    struct element*suiv;
} ls;
ls *ajoutliste(ls*tete,int val) {
    ls *nouv;
    nouv=tete;
    if (tete==NULL) {
        nouv->vale=val;
        nouv->suiv=NULL;
        tete=nouv;
    }
    else {
        while (nouv->suiv!=NULL) {
            nouv=nouv->suiv;
        }
        ls*cel=(ls*)malloc(sizeof(ls));
        cel->vale=val;
        cel->suiv=NULL;
        nouv->suiv=cel;
    }
    return tete;
}
ls *affichage(ls*tete) {
    ls*cour;

    cour=tete;
    while ( cour->suiv!=NULL) {
        printf("%d\n",cour->vale);
        cour=cour->suiv;
    }
    return tete;
}
int  main () {
    ls *head ;
    head=NULL;
    head=ajoutliste(head,4);
    head=ajoutliste(head,5);
    head=ajoutliste(head,6);
    head= affichage(head);
    printf ("%d", head->suiv->vale);
return 0;
}