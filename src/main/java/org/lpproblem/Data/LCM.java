package org.lpproblem.Data;

import com.sun.javafx.print.Units;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LCM {

    private ArrayList<Integer> supply;
    private ArrayList<Integer> demand;
    private ArrayList<ArrayList<LCMCellSet>> costTable;
    int minColumn;
    int minRow;
    int minValue;

    private List<Integer> rowToSkipList;
    private List<Integer> columnToSkipList;
    private String checkSkip;


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
        checkSkip = null;
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

            while (true) {

                findMinCell();

                // retrieving min value from supply or demand.
                if (supply.get(minRow) > demand.get(minColumn)) { // store demand value in min-value.    100 >70

                    int smallC = demand.get(minColumn);
                    costTable.get(minRow).get(minColumn).setCellMinValue(smallC);

                    demand.set(minColumn, 0);
                    supply.set(minRow, (supply.get(minRow) - smallC));
                    checkSkip = "demand";

                } else {

                    int smallC = supply.get(minRow);
                    costTable.get(minRow).get(minColumn).setCellMinValue(smallC);
                    supply.set(minRow, 0);
                    demand.set(minColumn, (demand.get(minColumn) - smallC));
                    checkSkip = "sup";  // skip row.
                }
                System.out.println("demand = " + demand.toString() + "\t\t" + "supply = " + supply.toString() +"\t\tTo Enter = "+checkSkip+ "\n");

                if (rowToSkipList.size() == supply.size() || columnToSkipList.size() == demand.size()) {
                    break;
                }

            }

            for (ArrayList<LCMCellSet> row : costTable) {

                for (LCMCellSet column : row) {
                    if (column.getCellMinValue() != -1) {
                        System.out.print(column.getCellValue() + " * " + column.getCellMinValue() + "+");
                    }
                }

            }

        }


    }

    /**
     * demand 0 then column skip and if supply 0 row skip.
     */
    private void findMinCell() {
        System.out.println("Demand.toString() = " + demand.toString() + "\t\t" + "supply.toString() = " + supply.toString());
        // for supply

        if ( (checkSkip != null) && checkSkip.equals("sup")  /*supply.contains(minRow) && supply.get(minRow) == 0 */) {
//            int indexToSkip = supply.indexOf(minRow);
//            System.out.println("indexToSkip = " + indexToSkip);
//            System.out.println("indexToSkip Of Supply = " + indexToSkip);

            rowToSkipList.add(minRow);

            for (int i = 0; i < supply.size(); i++) {

                if (!rowToSkipList.isEmpty() && rowToSkipList.contains(i)) {
                    System.out.print("Found!");
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


        } else if ( (checkSkip != null) && checkSkip.equals("demand") /*demand.contains(minColumn) && demand.get(minColumn) == 0 */ ) {  // for demand

//            int indexToSkip = demand.indexOf(minColumn);
//            System.out.println("indexToSkip Demand = " + indexToSkip);
            columnToSkipList.add(minColumn);


            for (int i = 0; i < supply.size(); i++) { // 3

                if (!rowToSkipList.isEmpty() && rowToSkipList.contains(i)) {
                    continue;
                } else {
                    System.out.println("let me in");
                    for (int j = 0; j < demand.size(); j++) { // 4

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
            System.out.println("minRow = " + minRow + "\t\tminColumn = " + minColumn + "\t\tValue = " + minValue);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


}
