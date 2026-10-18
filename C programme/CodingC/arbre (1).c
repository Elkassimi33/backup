#include<stdio.h>
#include<stdlib.h>
typedef struct arbre{
int val;
struct arbre *g;
struct arbre *r;
} arbre ;
arbre *ajout(arbre *j,int a){
if(j==NULL){
arbre *nouv=(arbre*)malloc(sizeof(arbre));
nouv->val=a;
j=nouv;
nouv->g=NULL;
nouv->r=NULL;
return j;
}
if(a<j->val)
j->g=ajout(j->g,a);
else
j->r=ajout(j->r,a);
return j;
}
void affiche (arbre *j){
if(j!=NULL){

printf ("%d\n",j->val);
affiche(j->g);
affiche(j->r);}
}
void affichepi (arbre *j){
if(j!=NULL){
if(j->val%2==0)
printf ("%d\t",j->val);
affichepi(j->g);
affichepi(j->r);
}}
void affichek (arbre *j,int k){
if(j!=NULL){
if(k==0)
printf ("%d\t",j->val);
affichek(j->g,k-1);
affichek(j->r,k-1);
}}
int som(arbre *R,int p){
if(R==NULL)
return 0;
else
return p+(som(R->g,p+1)+som(R->r,p+1));
}
main(){
int s;
arbre *head;
head=NULL;
int a,i;
for (i=0;i<10;i++){
printf("entrez la %d valeur: ",i+1);
scanf("%d",&a);
head=ajout(head,a);
}
affiche(head);
printf("\n");
affichepi(head);
printf("\n");
affichek(head,1);
s=som(head,0);
printf("%d",s);
}
