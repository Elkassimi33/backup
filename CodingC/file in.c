#include<stdio.h>
#include<stdlib.h>
typedef struct fille {
    int val;
    struct fille *suiv;
} file;
typedef struct fiche {
    file *d;
    file *f;
} liste;
file *enfile(liste *head,int donne) {
    file *nouv=(file*)malloc(sizeof(file));
    nouv->val=donne;
    nouv->suiv=NULL;
    if(nouv==NULL){
        printf("error de mémoire");
        exit(1);}
    else {
        if(head->d==NULL) {
            head->d=nouv;
            head->f=head->d;
        }
        else {
            head->f->suiv=nouv;
            head->f=nouv;
        }
    }
    return head;
}
file *defile(liste *head) {
    if(head==NULL)
        printf("la file est vide");
    else {
        file *temp=head->d;
        head->d=head->d->suiv;
        free(temp);
    }
    return head;
}
void affiche (liste *head) {
    if(head==NULL)
        printf("ona rien a affiche la liste est vide");
    else {
        liste *cour=head;
        while(cour!=NULL) {
            printf("%d\t",cour->d->val);
            cour=cour->d->suiv;
        }
    }
}
main() {
    liste *tete;
    tete=NULL;
    int a;
    printf("et ");
    scanf("%d",&a);
    tete=enfile(tete,a);
    
    affiche(tete);
}