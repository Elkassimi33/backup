#include <stdio.h>

// دالة لطباعة مثلث باسكال
void printpascaltriangle(int rows)
{
    int coef = 1;

    for (int i = 0; i < rows; i++)
    {
        for (int space = 1; space < rows - i; space++)
            printf("  ");

        for (int j = 0; j <= i; j++)
        {
            if (j == 0 || i == 0)
                coef = 1;
            else
                coef = coef * (i - j + 1) / j;

            printf("%4d", coef);
        }

        printf("\n");

    }
}

int main()
{
    int rows ;
    printf ("entre le nombre de rows :");
    scanf("%d",&rows);
    printpascaltriangle(rows);
    return 0;
}