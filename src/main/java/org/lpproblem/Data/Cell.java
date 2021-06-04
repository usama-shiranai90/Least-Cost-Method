package org.lpproblem.Data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Cell {

    private SimpleIntegerProperty value;

    public Cell() {

    }

    public Cell(int number) {
        value = new SimpleIntegerProperty(number);
    }

    public int getValue() {

        if (value == null) {
            return 0;
        }
        return value.get();

    }

    public SimpleStringProperty valueProperty() {
        if (value == null)
            return new SimpleStringProperty("Enter Value");

        return new SimpleStringProperty(String.valueOf(value.intValue()));
    }

    public void setValue(int value) {
        this.value = new SimpleIntegerProperty(value);
    }
}
