package org.lpproblem.Data;

import java.util.ArrayList;

public class LCM {

    private ArrayList<Integer> supply;
    private ArrayList<Integer> demand;
    private ArrayList<ArrayList<LCMCellSet>> costTable;
    int minColumn;
    int minRow;
    int minValue;

    public LCM(ArrayList<Integer> supply, ArrayList<Integer> demand, ArrayList<ArrayList<Integer>> cost_table) {
        this.supply = new ArrayList<>(supply);
        this.demand = new ArrayList<>(demand);
        this.costTable = new ArrayList<>();
        storeCostTableReference(cost_table);

        minColumn = 0;
        minRow = 0;
        minValue = costTable.get(0).get(0).getCellValue();

    }


    public void storeCostTableReference(ArrayList<ArrayList<Integer>> cost_table) {

        for (int row = 0; row < cost_table.size(); row++) {
            costTable.add(new ArrayList<>());
            for (int column = 0; column < cost_table.get(row).size(); column++) {
                int value = cost_table.get(row).get(column);
                costTable.get(row).add(new LCMCellSet(value));
            }
        }

        for (ArrayList<LCMCellSet> list : costTable) {

            for (LCMCellSet cellSet : list) {
                System.out.print(cellSet.getCellValue() + "\t");
            }
            System.out.println();
        }
    }

    public void solveLCM() {
        int totalSupply = supply.stream().mapToInt(value -> value).sum();
        int totalDemand = demand.stream().mapToInt(value -> value).sum();

        if (totalDemand != totalSupply) {
            System.out.println("It is Unbalanced , check again.");
        } else {  // for balanced working.


            findMinCell();

            // retriving min value from supply or demand.
            if (supply.get(minRow) > demand.get(minColumn)) {

            } else {

            }


        }
    }

    private void findMinCell() {
        for (int r = 0; r < supply.size(); r++) {
            for (int c = 0; c < demand.size(); c++) {
                if (costTable.get(r).get(c).getCellValue() < minValue) {
                    minValue = costTable.get(r).get(c).getCellValue();
                    minRow = r;
                    minColumn = c;
                }
            }
        }
    }


}
