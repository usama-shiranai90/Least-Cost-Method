package org.lpproblem.Data;

import java.util.ArrayList;
import java.util.List;

public class ElementCollection {

    private final List<Cell> cellsData = new ArrayList<>();

    public int getSize() {
        return cellsData.size();
    }

    public void setSize(int size) {

        while (cellsData.size() != size) {
            if (cellsData.size() < size) {
                Cell cell = new Cell();
                cell.setValue(0);

                cellsData.add(cell);
            }
            else
                cellsData.remove(cellsData.size() - 1);
        }
        
    }

    public int getByIndex(int index) {
        if (cellsData.get(index).getValue() == -1) {
            return 0;
        }
        return cellsData.get(index).getValue();
    }


    public void setByIndex(int index, int value) {
        cellsData.get(index).setValue(value);
    }


}
