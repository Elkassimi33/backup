#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>

#define PIPE_NAME "/tmp/my_pipe"
#define BUFFER_SIZE 128

void writer() {
    int fd;
    char message[BUFFER_SIZE];

    printf("Entrez un message à écrire dans le pipe : ");
    fgets(message, BUFFER_SIZE, stdin);
    message[strcspn(message, "\n")] = '\0'; // Supprime le saut de ligne

    fd = open(PIPE_NAME, O_WRONLY);
    if (fd == -1) {
        perror("Erreur lors de l'ouverture du pipe en mode écriture");
        exit(EXIT_FAILURE);
    }

    write(fd, message, strlen(message) + 1);
    printf("Message envoyé : %s\n", message);
    close(fd);
}

void reader() {
    int fd;
    char buffer[BUFFER_SIZE];

    fd = open(PIPE_NAME, O_RDONLY);
    if (fd == -1) {
        perror("Erreur lors de l'ouverture du pipe en mode lecture");
        exit(EXIT_FAILURE);
    }

    read(fd, buffer, sizeof(buffer));
    printf("Message reçu : %s\n", buffer);
    close(fd);
}

int main() {
    int choice;

    // Création du pipe nommé
    if (mkfifo(PIPE_NAME, 0666) == -1) {
        perror("Erreur lors de la création du pipe nommé");
        // Si le pipe existe déjà, ignorer l'erreur
    }

    while (1) {
        printf("\n--- Menu ---\n");
        printf("1. Écrire dans le pipe (Writer)\n");
        printf("2. Lire depuis le pipe (Reader)\n");
        printf("3. Quitter\n");
        printf("Choisissez une option : ");
        scanf("%d", &choice);
        getchar(); // Consomme le '\n' restant dans le buffer d'entrée

        switch (choice) {
            case 1:
                writer();
                break;
            case 2:
                reader();
                break;
            case 3:
                printf("Au revoir!\n");
                unlink(PIPE_NAME); // Supprime le pipe nommé
                exit(EXIT_SUCCESS);
            default:
                printf("Option invalide. Veuillez réessayer.\n");
        }
    }

    return 0;
}
