#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdbool.h>
typedef struct produit {
    int prix;
    char lib[30];
    struct produit *suiv;
} prod;
typedef struct categories {
    char nom[30];
    int nbr;
    prod *liste;
    struct categories *next;
} cat;
cat *ajoutcategorie(cat * head,nam[20]) {
    cat *nouv=(cat*)malloc(sizeof(cat));
    nouv->nbr=0;
    strcpy(nouv->nom,nam);
    nouv->liste=NULL;
    nouv->next=NULL;
    if(head==NULL) {
        head=nouv;

    }
    else {
        cat *cour=head;
        while (cour->next!=NULL&&strcmp(cour->nom,nam)!=0) {
            cour=cour->next;
        }
        if(strcmp(cour->nom,nam)==0) {
            printf("la categorie deja exist\n");
            free(nouv);

        }
        else {
            cour->next=nouv;

        }
    }
    return head;
}
cat *ajouteproduit(cat *head,char nam[20],int prix,char lib[20]) {
    prod *nouv=(prod*)malloc(sizeof(prod));
    nouv->prix=prix;
    strcpy(nouv->lib,lib);
    nouv->suiv=NULL;
    cat *cour=head;
    while(cour->next!=NULL&&strcmp(cour->nom,nam)!=0) {
        cour=cour->next;
    }
    if(strcmp(cour->nom,nam)==0) {
        if(cour->liste==NULL) {
            cour->liste=nouv;
            cour->nbr++;
            cour->liste->suiv=NULL;
        }
        else {
            prod *c=cour->liste;
            while(c->suiv!=NULL&&strcmp(c->lib,lib)!=0) {
                c=c->suiv;
            }
            if(strcmp(c->lib,lib)==0) {
                printf("le produit déjà exist \n");
                free(nouv);
            }
            else {
                nouv->suiv=cour->liste;
                cour->liste->suiv=nouv;
                cour->nbr++;
            }
        }
    }
    else {
        printf("cette catégorie n'existe pas \n");
        free(nouv);
    }
    return head;
}
cat *supcat(cat *head,char nam[20]) {
    cat *temp;
    if(head==NULL)
        printf("on a rien a supprimer la liste est vide \n");
    if(!strcmp(head->nom,nam)) {
        temp=head;
        head=head->next;
        free(temp);
    }
    else {
        cat *cour=head;
        while (cour!=NULL&&strcmp(cour->next->nom,nam)!=0) {
            cour=cour->next;
        }
        if(!strcmp(head->next->nom,nam)) {
            temp=cour->next;
            cour=temp->next;
            free(temp);
        }
        printf("cette catégorie n'existe pas\n");
    }
    return head;
}
void affichecategorie(cat *head,char nam[20]) {
    cat *cour=head;
    while(cour!=NULL&&strcmp(cour->nom,nam)!=0) {
        cour=cour->next;
    }
    if(strcmp(cour->nom,nam)==0) {
        printf("%s\t%d:\n",cour->nom,cour->nbr);
        prod *c=cour->liste;
        while (c!=NULL) {
            printf("%s\t%d\n",c->lib,c->prix);
            c=c->suiv;
        }
    }
    else
        printf("cette catégorie n'existe pas\n");
}
void afficheall(cat *head) {
    cat *cour=head;
    while (cour!=NULL) {
        printf("%s \t %d :::\n",cour->nom,cour->nbr);
        prod *c=cour->liste;

        while (c!=NULL) {
            printf("%s \t %d \n",c->lib,c->prix);
            c=c->suiv;
        }
        cour=cour->next;
    }
}
main() {
    cat *tete=NULL;
    
    printf("ajoute catégories\n");
    for(int i=0; i<3; i++) {
        char *n=NULL;
        n=(char*)malloc(20*sizeof(char));
        printf("entrez le nom de la catégorie :");
        gets(n);
        tete=ajoutcategorie(tete,n);
    }
    afficheall(tete);
    printf("\n\n");
    char *p=NULL;
    char *c=NULL;
    int a;
    p=(char*) malloc(20*sizeof(char));
    c=(char*) malloc(20*sizeof(char));
    for(int i=0; i<3; i++) {
        printf("entrez le nom de catégorie: ");
        scanf(" %s",p);
        printf("entrez le nom de produit: ");
        scanf(" %s",c);
        printf("entrez le prix de produit: ");
        scanf(" %d",&a);
        tete=ajouteproduit(tete,p,a,c);
        printf("\n");
    }
    
    afficheall(tete);
}