#include <stdio.h>
#include<stdlib.h>
int main() {
    double num1, num2, result;
    char operator;

    printf("أدخل العملية الحسابية (+, -, *, /): ");
    scanf("%c",&operator);

    printf("أدخل العدد الأول: ");
    scanf("%lf", &num1);

    printf("أدخل العدد الثاني: ");
    scanf("%lf", &num2);

    switch (operator) {
        case '+':
            result = num1 + num2;
            printf("النتيجة: %lf\n", result);
            break;
        case '-':
            result = num1 - num2;
            printf("النتيجة: %lf\n", result);
            break;
        case '*':
            result = num1 * num2;
            printf("النتيجة: %lf\n", result);
            break;
        case '/':
            if (num2 != 0) {
                result = num1 / num2;
                printf("النتيجة: %lf\n", result);
            } else {
                printf("لا يمكن القسمة على الصفر.\n");
            }
            break;
        default:
            printf("عملية غير صحيحة.\n");
            break;
    }

    return 0;
}