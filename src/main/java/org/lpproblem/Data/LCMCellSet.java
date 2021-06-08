package org.lpproblem.Data;

public class LCMCellSet {

    int cellValue;
    int cellMinValue;

    LCMCellSet(){

    }

    public LCMCellSet(int cellValue) {
        this.cellValue = cellValue;
        cellMinValue = -1;
    }

    public LCMCellSet(int cellValue, int cellMinValue) {
        this.cellValue = cellValue;
        this.cellMinValue = cellMinValue;
    }

    public int getCellValue() {
        return cellValue;
    }

    public void setCellValue(int cellValue) {
        this.cellValue = cellValue;
    }

    public int getCellMinValue() {
        return cellMinValue;
    }

    public void setCellMinValue(int cellMinValue) {
        this.cellMinValue = cellMinValue;
    }
}
