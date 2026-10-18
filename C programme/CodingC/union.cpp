#include <stdio.h>
#include<stdlib.h>
int main()
{
    int i,j,*a,*b,m,n,*u,p;

    printf("donner dim de t1 : ");
    scanf("%d",&n);
    printf("donner dim de t2 : ");
    scanf("%d",&m);
    a=(int*)malloc(n*sizeof(int));
    b=(int*)malloc(n*sizeof(int));
    u=(int*)malloc((n+m)*sizeof(int));
    printf ("remplir le 1 tableau\n");
    for(i=0; i<n; i++) {
        printf("enter la valeur %d : ",i);
        scanf("%d",&a[i]);
    }
    printf ("remplir la 2 tableau\n");
    for(i=0; i<m; i++) {
        printf("enter la valeur %d : ",i);
        scanf("%d",&b[i]);
    }
    for(i=0; i<m+n; i++)
    {
        if(i<n)
            u[i]=a[i];
        else
            u[i]=b[i-n];
    }
    for(i=0; i<n+m; i++) {
        for(j=0; j<n+m; j++) {
            if(j!=i) {
                if(u[i]==u[j]) {
                    u[j]=0;
                }
            }

        }

    }
    p=0;
    for(i=0;i<n+m;i++){
    u[p]=u[i];
    if(u[i]!=0){
    p++;
    }
    }

    for(i=0; i<p; i++)
        printf("u[%d]=%d\n",i,u[i]);
}
