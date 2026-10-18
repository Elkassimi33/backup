#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef struct livre {
    int code,nb;
    char *titre,*dis;
} lv;
typedef struct cellule {
    lv data;
    struct cellule *suiv;
} cel;
/*إضافة كتاب*/
cel *ajou(cel *tete,lv newliv) {
cel *cour;
    cel *newcel=(cel*)malloc(sizeof(cel));
    newcel->data=newliv;
        newcel->suiv=NULL;
    if(tete==NULL) {
        
        tete=newcel;

    }
    else {
        
        while (cour->suiv!=NULL){cour=cour->suiv;}
        cour->suiv=newcel;

    }
    return tete;
}/*تعديل على عدد الأمثلة في كتاب*/
cel *retire(cel *tete,int code) {
    cel *cour=tete;
    if(cour->data.code==code) {
        if(cour->data.nb>1)
            cour->data.nb--;
        else {
            tete=cour->suiv;
            if(tete->suiv==NULL)

                free(cour);
        }
        return tete;
    }
    while(cour->suiv!=NULL) {
        if(cour->data.code==code) {
            if(cour->data.nb>1)
                cour->data.nb--;
            else {
                cel *c;
                c=cour->suiv;
                cour->suiv=cour->suiv->suiv;
                free(c);
            }
            return tete;
        }
        cour=cour->suiv;
    }
    if(cour->suiv->data.code==code) {
        if(cour->data.nb>1)
            cour->data.nb--;
        else {
            cel *c;
            c=cour->suiv;
            cour->suiv=NULL;

            free(c);
        }
    }
    return tete;
}
/*AFFICHAGE*/
void affichage(cel *tete) {
    cel *cour =tete;
    while (cour!=NULL) {
        printf ("le titre est: %s\n",cour->data.titre);
        printf ("le code est: %d\n",cour->data.code);
        printf ("le descipline est: %s\n",cour->data.dis);
        printf ("le nombre des exemples est: %d\n",cour->data.nb);
        cour=cour->suiv;
    }
}
/*ترتيب اللائحة*/
cel *tri(cel *tete,char *disc) {
    cel *cour=tete;
    cel *temp;
    for(int i=0; i<4; i++) {
        while (cour!=NULL) {
            if(cour->data.dis==disc[i]) {
                if(cour!=tete) {
                    temp=cour;
                    cour->suiv=tete;
                    tete=cour;
                    free(temp);
                }
            }
            cour=cour->suiv;
        }
    }
}
main() {
    char disc[]= {"c","java","html"};
    cel *head=NULL;
    lv ll= {11,5,"cl","html"};
    lv l= {12,2,"l","c"};
    head=ajou(head,l);
    head=ajou(head,ll);
    affichage(head);

}