#include <stdio.h>

// الدالة لحساب معامل الثنائي
int binomial_coefficient(int n, int k) {
    int result = 1;
    if (k > n - k)
        k = n - k;
    for (int i = 0; i < k; i++) {
        result *= (n - i);
        result /= (i + 1);
    }
    return result;
}

int main() {
    int n = 9;
    int k = 3;
    int result = binomial_coefficient(n, k);
    printf("coefficient %d et %d est: %d\n", n, k, result);

    return 0;
}