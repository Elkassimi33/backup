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
cat *init(char nm[20]) {
    cat *nouv=(cat*) malloc(sizeof(cat));
    if(nouv==NULL)
        printf("error de stockage");
    nouv->liste=NULL;
    nouv->nbr=0;
    nouv->next=NULL;
    strcpy(nouv->nom,nm);
    return nouv;
}
prod *cre(int p,char n[20]) {
    prod *nouv=(prod*)malloc(sizeof(prod));
    if(nouv==NULL)
        printf("error de stockage");
    else {
        nouv->prix=p;
        strcpy(nouv->lib,n);
        nouv->suiv=NULL;
    }
    return nouv;
}
prod *empile(prod *t,int pri,char na[20]) {
prod *nouv=cre(pri,na);
if(t==NULL){
t=nouv;
t->suiv=NULL;}
    else{
    nouv->suiv=t;
    t->suiv=nouv;
    }
    return t;
}
cat *ajoutcategorie(cat * head,char nam[20]) {
    if(head==NULL) {
        head=init(nam);
    }
    else {
        cat *cour=head;
        while (cour->next!=NULL&&strcmp(cour->nom,nam)!=0) {
            cour=cour->next;
        }
        if(strcmp(cour->nom,nam)==0) {
            printf("la categorie deja exist\n");
        }
        else {
            cour->next=init(nam);

        }
    }
    return head;
}
cat *ajouteproduit(cat *head,char nam[20],int pr,char l[20]) {
    cat *cour=head;
    while(cour!=NULL&&strcmp(cour->nom,nam)!=0) {
        cour=cour->next;
    }
    if(strcmp(cour->nom,nam)==0)
    {
        cour->liste=empile(cour->liste,pr,l);
        cour->nbr+=1;
    }
    else {
        printf("cette catégorie n'existe pas \n");
    }
    return head;
}
cat *supcat(cat *head,char nam[20])
{
    cat *temp;
    if(head==NULL)
        printf("on a rien a supprimer la liste est vide \n");
    if(!strcmp(head->nom,nam)) {
        temp=head;
        head=head->next;
        free(temp);
    }
    else  {
        cat *cour=head;
        while (cour!=NULL&&strcmp(cour->next->nom,nam)!=0)
            cour=cour->next;
        if(!strcmp(head->next->nom,nam))
        {
            temp=cour->next;
            cour=temp->next;
            free(temp);
        }
        else
            printf("cette catégorie n'existe pas\n");
    }
    return head;
}
void affichecategorie(cat *head,char nam[20]) {
    cat *cour=head;
    while(cour!=NULL&&strcmp(cour->nom,nam)!=0)
        cour=cour->next;
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
    while (cour!=NULL)  {
        printf("%s \t %d :::\n",cour->nom,cour->nbr);
        prod *nn=cour->liste;
        while(nn!=NULL) {
            printf("%s \t %d \n",nn->lib,nn->prix);
            nn=nn->suiv;
        }
        cour=cour->next;
    }
}
int main() {
    cat *tete=NULL;

    printf("ajoute catégories\n");
    for(int i=0; i<3; i++) {
        char *n=NULL;
        n=(char*)malloc(20*sizeof(char));
        printf("entrez le nom de la catégorie :");
        scanf("%s",n);
        tete=ajoutcategorie(tete,n);
    }
    afficheall(tete);
    printf("\n\n");

    for(int i=0; i<3; i++) {
        char *p=NULL;
        char *c=NULL;
        int a;
        p=(char*) malloc(20*sizeof(char));
        c=(char*) malloc(20*sizeof(char));
        printf("entrez le nom de catégorie: ");
        scanf(" ");
        gets(p);
        printf("entrez le nom de produit: ");
        gets(c);
        printf("entrez le prix de produit: ");
        scanf("%d",&a);
        tete=ajouteproduit(tete,p,a,c);
        printf("\n");

    }
    afficheall(tete);
    return 0;
}