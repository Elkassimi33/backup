 #include<stdio.h>
#include<math.h>
int main() {
    float a,b,c,d,x,y,t,r;
    printf ("write the coefficient de X^2:");
    scanf("%f",&a);
    printf("write the coefficient de X : ");
    scanf ("%f",&b);
    printf ("write the constant : ");
    scanf ("%f",&c);
    if(a==0)
        if(b==0)
            if(c==0)
                printf("the result is : the group C" );

            else
            {
                printf("impossible");
            }
        else
        {
            printf("the result is :\n X=%.2f",-c/b);
        }
    else
    {
        d=pow(b,2)-4*a*c;
        if(d==0)
        {
            x=-b/(2*a);
            printf("the  result is :\n  x=%.2f",x);
        }
        if(d>0)
        {
            x=(-b+sqrt(d))/(2*a);
            y=(-b-sqrt(d))/(2*a);
            printf (" the result is : X1=%.2f \n X2=%.2f",x,y);
        }
        if(d<0)
        {
            t=-b/(2*a);
            r=sqrt(-d)/(2*a);
            printf("the results is :\n X1=%.2f+%.2fi \n X2=%.2f-%.2fi",t,r,t,r);
        }
    }




    return 0;
}
