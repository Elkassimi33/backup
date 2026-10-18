#include <stdio.h>
#include<string.h>
struct Book {
    char title[100];
    char author[100];
    int year;
};

int main() {
    struct Book book1, book2;
    // تعيين قيم لمعلومات الكتاب الأول
    strcpy(book1.title, "Introduction to Programming");
    strcpy(book1.author, "John Smith");
    book1.year = 2020;
// تعيين قيم لمعلومات الكتاب الثاني
    strcpy(book2.title, "Data Structures and Algorithms");
    strcpy(book2.author, "Jane Doe");
    book2.year = 2018;
    // طباعة معلومات الكتابين
    printf("Book 1:\n");
    printf("Title: %s\n", book1.title);
    printf("Author: %s\n", book1.author);
    printf("Year: %d\n", book1.year);

    printf("\nBook 2:\n");
    printf("Title: %s\n", book2.title);
    printf("Author: %s\n", book2.author);
    printf("Year: %d\n", book2.year);

    return 0;
}