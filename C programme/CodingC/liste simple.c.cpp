#include <stdio.h>
#include<stdlib.h>
typedef struct element {
    int vale;
    struct element *suiv;
} ls;
ls *ajoutliste(ls *tete,int val) {
    ls *cel=(ls*)malloc(sizeof(ls));
    cel->vale=val;
    cel->suiv=NULL;
    
    if (tete==NULL) {
        tete=cel;
        
    }
    else {
    ls *nouv;
    nouv=tete;
        while (nouv->suiv!=NULL) {
            nouv=nouv->suiv;
        }
        nouv->suiv=cel;
        
    }
    return tete;
}
void affichage(ls*tete) {
    ls*cour;

    cour=tete;
    while ( cour!=NULL) {
        printf("%d\n",cour->vale);
        cour=cour->suiv;
    }
    
}
 main () {
    ls *head ;
    head=NULL;
    head=ajoutliste(head,4);
    head=ajoutliste(head,5);
    head=ajoutliste(head,6);
    head=ajoutliste(head,3);
    affichage(head);

}
