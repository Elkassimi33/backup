#include <stdio.h>
#include<stdlib.h>
#include <iostream>
#include <math.h>
int main () {
    int a,b,m,c,s,i;
    float n,t;
    std::cout<<"enter a:";
    std::cin>>a;
    std::cout<<"enter b:";
    std::cin >> b;
    std::cout <<"enter c :";
    std::cin >> c;
//حساب المحددة
    double base =b;
    double exponenet=2;
    s=pow(base,exponenet);
    m=s-4*a*c;
    // حلول المعادلة
    if (m==0) {
        t=-b/(2*a);
        printf("results: %.2f\n",t);
    }
    else if (m>0) {
        t=(-b-sqrt(m))/(2*a);
        n=(-b+sqrt(m))/(2*a);
        printf("result %.2f,%.2f\n",t,n);
    }
    else {
        double imaginarypart=sqrt(-m)/(2*a);
        t=-b/(2*a);
        printf("result %.2f-%.2fi\n,%.2f+%.2fi\n",t, imaginarypart,t, imaginarypart);
    }

    return 0;
}