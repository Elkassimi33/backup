#include <stdio.h>
#define ROWS 3
#define COLS 3

void drawTable(int table[ROWS][COLS]) {
    for (int i = 0; i < ROWS; i++) {
        for (int j = 0; j < COLS; j++) {
            printf("%d\t", table[i][j]);
        }
        printf("\n");
    }
}

void fillTable(int table[ROWS][COLS]) {
    for (int i = 0; i < ROWS; i++) {
        for (int j = 0; j < COLS; j++) {
            printf("Enter the value for row %d, column %d: ", i + 1, j + 1);
            scanf("%d", &table[i][j]);
        }
    }
}

int main() {
    int table[ROWS][COLS];

    fillTable(table);

    printf("\nTable:\n");
    drawTable(table);

    return 0;
}
