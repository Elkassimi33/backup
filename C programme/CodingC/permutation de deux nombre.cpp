#include <iostream>
#include <cstdio>

void swap(int* a, int* b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

int main() {
    int num1, num2;

    printf("entre a : ");
    scanf("%d", &num1);

    printf("entre b: ");
    scanf("%d", &num2);

    printf("premier value: num1 = %d, num2 = %d\n", num1, num2);

    int* n = &num1;
    int* m = &num2;

    swap(n, m);

    printf("final value: num1 = %d, num2 = %d\n", num1, num2);

    return 0;
}
