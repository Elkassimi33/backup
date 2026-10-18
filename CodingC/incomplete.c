#include <stdio.h>
#include <stdlib.h>
#include <string.h>
typedef struct file {
  char nom[30];
  struct file *suiv;
} file;
file *d = NULL;
file *f = NULL;
file *p = NULL;
file *emfile(file *j, char n[30]) {
  file *nouv = (file *)malloc(sizeof(file));
  strcpy(nouv->nom, n);
  nouv->suiv = NULL;
  if (d == NULL) {
    j = nouv;
    f = nouv;
  } else {
    f->suiv = nouv;
    f = nouv;
  }
  return j;
}
file *defile(file *j) {
  if (j == NULL) {
    printf("la file est vide.\n");
    return j;
  }
  file *temp;
  temp = j;
  j = j->suiv;
  free(temp);
  return j;
}
void affiche(file *j) {
  file *cour = j;
  while (cour != NULL) {
    printf("%s\n", cour->nom);
    cour = cour->suiv;
  }
}
file *defileempile(file *pile, file *j) {
  if (pile == NULL) {
    pile = j;
    j = j->suiv;
    pile->suiv = NULL;
  }
  return pile;
}
main() {
  char nam[30];
  for (int i = 0; i < 5; i++) {
    printf("entrez la %d nom: ", i + 1);
    gets(nam);
    d = emfile(d, nam);
  }
  printf("la file\n");
  affiche(d);
  printf("\n");
  d = defile(d);
  printf("la file\n");
  affiche(d);
  printf("\n");
  p = defileempile(p, d);
  p = defileempile(p, d);
  p = defileempile(p, d);
  printf("la pile\n");
  affiche(p);
}
