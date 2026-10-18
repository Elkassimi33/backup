#include<stdio.h>
#include<stdlib.h>
#include<string.h>
typedef struct birth {
    int day,month,year;
} date;
typedef struct customer {
    date naiss;
    char tel[20];
    char nam[40];
    char adresse[40];
    float solde;
} customer ;
typedef struct controle {
    customer data;
    struct controle *suiv;
} controle;

controle *new_acc(controle *head) {
    controle *new_acc=(controle*)malloc(sizeof(controle));
    printf("entrez le nom de client : ");
    scanf("%s",new_acc->data.nam);
    printf("entrez le numéro de téléphone de client: ");
    scanf("%s",new_acc->data.tel);
    printf("entrez l'adresse de client: ");
    scanf("%s",new_acc->data.adresse);
    printf("entrez le solde de client ");
    scanf("%f",&new_acc->data.solde);
    printf("entrez la date de naissance:\n");
    printf("day: ");
    scanf("%d",&new_acc->data.naiss.day);
    printf("month : ");
    scanf("%d",&new_acc->data.naiss.month);
    printf("year: ");
    scanf("%d",&new_acc->data.naiss.year);
    new_acc->suiv=NULL;
    if(new_acc==NULL) {
        printf(" error of allocating memory \n");
        exit(1);
    }
    else {
        new_acc->suiv=head;
        head=new_acc;
    }
    return head;
}
controle *edit(controle *head) {
    if(head==NULL) {
        printf("la liste est vide \n\n");
        exit(1);
    }
    else {
        char chernom[40];
        printf("entrez le nom de client: ");
        gets(chernom);
        controle *cour;
        cour=head;
        while(cour!=NULL&&strcmp(chernom,cour->data.nam)!=0) {
            cour=cour->suiv;
        }
        if(cour=NULL)
            printf("ce client n'exist pas\n\n ");
        else {
            printf("entrez la nouvelle adresse : ");
            scanf("%s",cour->data.adresse);
            printf("entrez le nouveau numéro de téléphone: ");
            scanf("%s",cour->data.tel);
        }
    }
    return head;
}
controle *transact(controle *head) {
    char chernom[40];
    printf("entrez le nom de client: ");
    gets(chernom);
    controle *cour;
    cour=head;
    while(cour!=NULL&&strcmp(chernom,cour->data.nam)!=0) {
        cour=cour->suiv;
    }
    if(cour=NULL)
        printf("ce client n'exist pas\n \n");
    else {
        int choix,money;
        printf("if you want to depose money chose: 1\nif you want to withdraw  money chose: 2");
        scanf("%d",&choix);
        if(choix==1) {
            printf("how much money you want to deposit: ");
            scanf("%f",&money);
            cour->data.solde+=money ;
        }
        if(choix==3) {
        char y;
            do {
                      printf("how much money you want to withdraw: ");
                scanf("%f",&money);
                if(money<=cour->data.solde){
                    cour->data.solde-=money ;
                    break;
                    }
                else {
                    printf("the client has only: %f",cour->data.solde);
                    printf("do you want to withdraw less money Y\N");
                    
                    scanf("%c",&y);
                }
            } while (y='Y');
        }
    }
    return head;
}
void affiche (controle *head) {
    if(head==NULL)
        printf("la liste est vide\n\n");
    else {
        controle *cour=head;
        while (cour!=NULL) {
            printf(" le nom de client est: %s\n ",cour->data.nam);
            printf(" le numéro de téléphone de client est : %s\n",cour->data.tel);
            printf(" l'adresse de client est : %s\n",cour->data.adresse);
            printf(" le solde de client est : %d\n ",cour->data.solde);
            printf(" la date de naissance est :%d/%d/%d\n ",cour->data.naiss.day,cour->data.naiss.month,cour->data.naiss.year);
            printf("\n");
            cour=cour->suiv;
        }
    }
}
controle *erase(controle *head) {
if(head==NULL){
printf("la liste est vide");
exit(5);}
    char nom[40];
    printf("put the nam of the client youa want to erase: ");
    gets(nom);
    controle *cour=head;
    while(cour!=NULL&&strcmp(nom,cour->suiv->data.nam)!=0) {
        cour=cour->suiv;
    }
    if(cour==NULL)
        printf("this client is not existed\n");
    else {
        controle *temp=cour->suiv;
        cour->suiv=temp->suiv;
        free(temp);
    }
    return head;
}
void affi_account(controle *head) {
    char chernom[40];
    printf("entrez le nom de client: ");
    gets(chernom);
    controle *cour=head;
    while(cour!=NULL&&strcmp(chernom,cour->data.nam)!=0) {
        cour=cour->suiv;
    }
    if(cour=NULL)
        printf("ce client n'exist pas\n ");
    else {
        printf(" le nom de client est: %s\n ",cour->data.nam);
        printf(" le numéro de téléphone de client est : %s\n",cour->data.tel);
        printf(" l'adresse de client est : %s\n",cour->data.adresse);
        printf(" le solde de client est : %d\n ",cour->data.solde);
        printf(" la date de naissance est :%d/%d/%d\n ",cour->data.naiss.day,cour->data.naiss.month,cour->data.naiss.year);
    }
}
int menu() {
    int choice;
    printf("\tCOSTUMER ACCOUNT BANKING MANAGEMENT SYSTEM\n");
    printf("\t     #####WELCOM TO THE MAIN MENU#####\n\n\n");
    printf("1. create a new account\n");
    printf("2.update information of existing account\n");
    printf("3. for transaction\n");
    printf("4.check the detail of existing account\n");
    printf("5.removing existing account\n");
    printf("6. view the customer's liste \n");
    printf("7. exit\n\n");
    printf("entre your choice:\n ");
    scanf("%d",&choice);
    return choice;
}
main() {
    controle *tete=(controle*)malloc(sizeof(controle));
    tete=NULL;
    int a;
    do {
        a=menu();
        switch(a) {
        case 1:
            tete=new_acc(tete);
            break;
        case 2:
            tete=edit(tete);
            break;
        case 3:
            tete=transact(tete);
            break;
        case 4:
            affi_account(tete);
            break;
        case 5:
            tete=erase(tete);
            break;
        case 6:
            affiche(tete);
            break;
        default :
            printf("programme has completed");
            break;
        }
    } while (a<7&&a>0);
}

    