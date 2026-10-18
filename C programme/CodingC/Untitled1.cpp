#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

typedef struct person {
    int age;
    char nom[20];
} p;

int main(void) {
    int n = open("fichpersonne.text", O_RDWR | O_CREAT | O_TRUNC, 0644);
    if (n < 0) {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }

    int s1[2], s2[2];
    if (pipe(s1) == -1 || pipe(s2) == -1) {
        perror("Error creating pipe");
        exit(EXIT_FAILURE);
    }

    p per[4];
    int i = fork();
    if (i < 0) {
        perror("Error during fork");
        exit(EXIT_FAILURE);
    }

    if (i != 0) { // Parent process
        int j = fork();
        if (j < 0) {
            perror("Error during fork");
            exit(EXIT_FAILURE);
        }

        if (j != 0) { // Parent process
            close(s1[0]); // Close unused read end of s1
            close(s2[0]); // Close unused read end of s2

            printf("Remplir le tableau\n");
            for (int k = 0; k < 4; k++) {
                printf("Entrez le nom: ");
                scanf("%s", per[k].nom);
                printf("Entrez l'age: ");
                scanf("%d", &per[k].age);
            }

            for (int k = 0; k < 4; k++) {
                if (per[k].age < 20) {
                    write(s2[1], &per[k], sizeof(p));
                } else {
                    write(s1[1], &per[k], sizeof(p));
                }
            }

            close(s1[1]); // Close writing end of s1
            close(s2[1]); // Close writing end of s2
        } else { // Second child process
            close(s1[0]);
            close(s1[1]);
            close(s2[1]); // Close unused write end of s2

            p pers;
            while (read(s2[0], &pers, sizeof(p)) > 0) {
                char buffer[50];
                snprintf(buffer, sizeof(buffer), "Nom: %s, Age: %d\n", pers.nom, pers.age);
                write(n, buffer, strlen(buffer));
            }
            close(s2[0]); // Close read end of s2
        }
    } else { // First child process
        close(s2[0]);
        close(s2[1]);
        close(s1[1]); // Close unused write end of s1

        p pers;
        while (read(s1[0], &pers, sizeof(p)) > 0) {
            char buffer[50];
            snprintf(buffer, sizeof(buffer), "Nom: %s, Age: %d\n", pers.nom, pers.age);
            write(n, buffer, strlen(buffer));
        }
        close(s1[0]); // Close read end of s1
    }

    close(n); // Close the file descriptor
    return 0;
}

