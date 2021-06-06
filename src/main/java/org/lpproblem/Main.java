package org.lpproblem;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        if (list.contains(30)){
            System.out.println("index : " + list.indexOf(30));
        }

    }
}
