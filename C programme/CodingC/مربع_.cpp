#include<stdio.h>
int main() {
    int a,j,i;
    printf ("entrer a :");
    scanf("%d",&a);
    for (int i = 0; i < a; i++) {
        if (i == 0 || i == a - 1) {
            for (int j = 0; j < a; j++) {
                printf("* ");
            }
        } else {
            printf("* ");
            for (int j = 0; j < a - 2; j++) {
                printf("  ");
            }
            printf("* ");
        }
        printf("\n");
    }

    return 0;
    }