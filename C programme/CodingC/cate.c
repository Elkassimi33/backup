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

cat *init(char nm[30]) {
    cat *nouv = (cat*) malloc(sizeof(cat));
    if(nouv == NULL) {
        printf("Error de memoir\n");
        exit(1);
    }
    nouv->liste = NULL;
    nouv->nbr = 0;
    nouv->next = NULL;
    strncpy(nouv->nom, nm, 29);
    nouv->nom[29] = '\0';
    return nouv;
}

prod *cre(int p, char n[30]) {
    prod *nouv = (prod*) malloc(sizeof(prod));
    if(nouv == NULL) {
        printf("Error   de memoir\n");
        exit(1);
    }
    nouv->prix = p;
    strncpy(nouv->lib, n, 29);
    nouv->lib[29] = '\0';
    nouv->suiv = NULL;
    return nouv;
}

prod *empile(prod *t, int pri, char na[30]) {
    prod *nouv = cre(pri, na);
    nouv->suiv = t;
    return nouv;
}
depile(prod *head) {
    prod *temp=head;
    head=head->suiv;
    free(temp);
    return head;
}
cat *ajoutcategorie(cat *head, char nam[30]) {
    if(head == NULL) {
        head = init(nam);
    } else {
        cat *cour = head;
        while (cour->next != NULL && strcmp(cour->nom, nam) != 0) {
            cour = cour->next;
        }
        if(strcmp(cour->nom, nam) == 0) {
            printf("La catégorie existe déjà\n");
        } else {
            cour->next = init(nam);
        }
    }
    return head;
}

cat *ajouteproduit(cat *head, char nam[30], int pr, char l[30]) {
    cat *cour = head;
    while(cour != NULL && strcmp(cour->nom, nam) != 0) {
        cour = cour->next;
    }
    if(cour != NULL && strcmp(cour->nom, nam) == 0) {
        cour->liste = empile(cour->liste, pr, l);
        cour->nbr += 1;
    } else {
        printf("Cette catégorie n'existe pas\n");
    }
    return head;
}

cat *supcat(cat *head, char nam[30]) {
    if(head == NULL) {
        printf("La liste est vide, rien à supprimer\n");
        return head;
    }
    cat *temp = NULL;
    if(strcmp(head->nom, nam) == 0) {
        temp = head;
        for(int i=0; i<head->nbr; i++)
            head->liste=depile(head->liste);
        head = head->next;
        free(temp);
    } else {
        cat *cour = head;
        while(cour->next != NULL && strcmp(cour->next->nom, nam) != 0) {
            cour = cour->next;
        }
        if(cour->next != NULL && strcmp(cour->next->nom, nam) == 0) {
            temp = cour->next;
            for(int i=0; i<temp->nbr; i++)
                temp->liste=depile(temp->liste);
            cour->next = temp->next;
            free(temp);
        } else {
            printf("Cette catégorie n'existe pas\n");
        }
    }
    return head;
}

void affichecategorie(cat *head, char nam[30]) {
    cat *cour = head;
    while(cour != NULL && strcmp(cour->nom, nam) != 0) {
        cour = cour->next;
    }
    if(cour != NULL && strcmp(cour->nom, nam) == 0) {
        printf("%s\t%d:\n", cour->nom, cour->nbr);
        prod *c = cour->liste;
        while(c != NULL) {
            printf("%s\t%d\n", c->lib, c->prix);
            c = c->suiv;
        }
    } else {
        printf("Cette catégorie n'existe pas\n");
    }
}

void afficheall(cat *head) {
    cat *cour = head;
    while(cour != NULL) {
        printf("%s\t%d:\n", cour->nom, cour->nbr);
        prod *nn = cour->liste;
        while(nn != NULL) {
            printf("%s\t%d\n", nn->lib, nn->prix);
            nn = nn->suiv;
        }
        cour = cour->next;
    }
}
int main() {
    cat *tete = NULL;

    printf("Ajoutez des catégories\n");
    for(int i = 0; i < 3; i++) {
        char n[30];
        printf("Entrez le nom de la catégorie : ");
        scanf("%s", n);
        tete = ajoutcategorie(tete, n);
    }
    afficheall(tete);
    printf("\n\n");

    for(int i = 0; i < 3; i++) {
        char p[30];
        char c[30];
        int a;
        printf("Entrez le nom de la catégorie: ");
        scanf("%s", p);
        printf("Entrez le nom du produit: ");
        scanf("%s", c);
        printf("Entrez le prix du produit: ");
        scanf("%d", &a);
        tete = ajouteproduit(tete, p, a, c);
        printf("\n");
    }
    afficheall(tete);
    char n[20];
    printf("entre vi");
    scanf("%s",n);
    tete=supcat(tete,n);
    afficheall(tete);
    return 0;
}