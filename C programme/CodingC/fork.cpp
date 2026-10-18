#include<unistd.h>
#include<stdio.h>
#include<stdlib.h>
int main(void){
	fork();
	(fork()||fork())
	printf("1");
	sleep(2);
	return 0;
}
