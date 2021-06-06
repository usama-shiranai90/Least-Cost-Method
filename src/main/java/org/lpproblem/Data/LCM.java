package org.lpproblem.Data;

import java.util.ArrayList;

public class LCM {

    private ArrayList<Integer> supply;
    private ArrayList<Integer> demand;
    private ArrayList<ArrayList<LCMCellSet>> costTable;

    public LCM(ArrayList<Integer> supply, ArrayList<Integer> demand, ArrayList<ArrayList<Integer>> cost_table) {
        this.supply = new ArrayList<>(supply);
        this.demand = new ArrayList<>(demand);
        this.costTable = new ArrayList<>();
        storeCostTableReference(cost_table);

    }


    public void storeCostTableReference(ArrayList<ArrayList<Integer>> cost_table) {

        for (int row = 0; row < cost_table.size(); row++) {
            costTable.add(new ArrayList<>());
            for (int column = 0; column < cost_table.get(row).size(); column++) {
                int value = cost_table.get(row).get(column);
                costTable.get(row).add(new LCMCellSet(value));
            }
        }

        for ( ArrayList<LCMCellSet> list : costTable){

            for ( LCMCellSet cellSet : list){
                System.out.print(cellSet.getCellValue() + ",\t");
            }
            System.out.println();
        }

    }
}
