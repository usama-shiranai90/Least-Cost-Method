package org.lpproblem.Data;

import java.util.ArrayList;
import java.util.List;

public class LCM {

    private ArrayList<Integer> supply;
    private ArrayList<Integer> demand;
    private ArrayList<ArrayList<LCMCellSet>> costTable;
    int minColumn;
    int minRow;
    int minValue;

    private List<Integer> rowToSkipList;
    private List<Integer> columnToSkipList;


    public LCM(ArrayList<Integer> supply, ArrayList<Integer> demand, ArrayList<ArrayList<Integer>> cost_table) {
        this.supply = new ArrayList<>(supply);
        this.demand = new ArrayList<>(demand);
        this.costTable = new ArrayList<>();
        storeCostTableReference(cost_table);

        minColumn = 0;
        minRow = 0;
        minValue = costTable.get(0).get(0).getCellValue();

        rowToSkipList = new ArrayList<>();
        columnToSkipList = new ArrayList<>();
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

//        System.out.println("totalDemand = " + totalDemand);
//        System.out.println("totalSupply = " + totalSupply);

        if (totalDemand != totalSupply) {
            System.out.println("It is Unbalanced , check again.");
        } else {  // for balanced working.

            while (true) {

                findMinCell();

                // retrieving min value from supply or demand.
                if (supply.get(minRow) > demand.get(minColumn)) { // store demand value in min-value.    100 >70

                    int smallC = demand.get(minColumn);
                    costTable.get(minRow).get(minColumn).setCellMinValue(smallC);

                    demand.set(minColumn, 0);
                    supply.set(minRow, (supply.get(minRow) - smallC));


                } else {

                    int smallC = supply.get(minRow);
                    costTable.get(minRow).get(minColumn).setCellMinValue(smallC);
                    supply.set(minRow, 0);
                    demand.set(minColumn, (demand.get(minColumn) - smallC));

                }
                System.out.println("demand = " + demand.toString());
                System.out.println("supply = " + supply.toString());


                if (rowToSkipList.size() == supply.size() || columnToSkipList.size() ==  demand.size()){
                    break;
                }

            }


        }


    }

    private void findMinCell() {


        // for supply
        if (supply.contains(minRow)) {

            int indexToSkip = supply.indexOf(minRow);
            rowToSkipList.add(indexToSkip);

            for (int i = 0; i < supply.size(); i++) {

                if (!rowToSkipList.isEmpty() && rowToSkipList.contains(i)) {
                    continue;

                } else {

                    for (int j = 0; j < demand.size(); j++) {

                        if (!columnToSkipList.isEmpty() && columnToSkipList.contains(j)) {
                            continue;
                        } else {

                            if (costTable.get(i).get(j).getCellValue() < minValue) {
                                minValue = costTable.get(i).get(j).getCellValue(); // 1
                                minRow = i;
                                minColumn = j;
                            }
                        }

                    }
                }


            }


        } else if (demand.contains(minColumn)) {  // for demand

            int indexToSkip = demand.indexOf(minColumn);
            columnToSkipList.add(indexToSkip);

            for (int i = 0; i < supply.size(); i++) {

                if (!rowToSkipList.isEmpty() && rowToSkipList.contains(i)) {
                    continue;
                } else {

                    for (int j = 0; j < demand.size(); j++) {

                        if (!columnToSkipList.isEmpty() && columnToSkipList.contains(j)) {
                            continue;
                        } else {

                            if (costTable.get(i).get(j).getCellValue() < minValue) {
                                minValue = costTable.get(i).get(j).getCellValue();
                                minRow = i;
                                minColumn = j;
                            }
                        }
                    }
                }
            }


        } else {
            for (int r = 0; r < supply.size(); r++) {
                for (int c = 0; c < demand.size(); c++) {
                    if (costTable.get(r).get(c).getCellValue() < minValue) {
                        minValue = costTable.get(r).get(c).getCellValue(); // 1
                        minRow = r; // 0
                        minColumn = c; // 0
                    }
                }
            }

        }


    }


}
