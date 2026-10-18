package com.learn.exa.chap3;

class Story{
    void recite(int chapter) throws Exception{}
}

public class Question extends Story{

    final void recite(final int chapter){
        switch (chapter){
            case 2:
                System.out.println(2);
            default:
                System.out.println(9);
        }
    }

    static void main(String[] args) {

    }
}