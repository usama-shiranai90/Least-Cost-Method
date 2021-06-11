package org.lpproblem;

import org.lpproblem.Data.LCM;

import java.util.ArrayList;
import java.util.Collections;

public class Main {


    public static void main(String[] args) {

        ArrayList<Integer> supply = new ArrayList<>();
        supply.add(50);
        supply.add(30);
        supply.add(70);

        ArrayList<Integer> deamnd = new ArrayList<>();
        deamnd.add(30);
        deamnd.add(60);
        deamnd.add(20);
        deamnd.add(40);

        ArrayList<ArrayList<Integer>> cost = new ArrayList<>();
        cost.add(new ArrayList<>());
        cost.add(new ArrayList<>());
        cost.add(new ArrayList<>());


        cost.get(0).add(15);
        cost.get(0).add(18);
        cost.get(0).add(19);
        cost.get(0).add(13);

        cost.get(1).add(20);
        cost.get(1).add(14);
        cost.get(1).add(15);
        cost.get(1).add(17);


        cost.get(2).add(25);
        cost.get(2).add(12);
        cost.get(2).add(17);
        cost.get(2).add(22);



        LCM leastCostMethod = new LCM(supply , deamnd , cost);
        leastCostMethod.solveLCM();

    }
}
