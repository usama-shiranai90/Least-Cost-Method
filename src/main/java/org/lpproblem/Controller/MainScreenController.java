package org.lpproblem.Controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.lpproblem.Data.Cell;
import org.lpproblem.Data.ElementCollection;

import java.net.URL;
import java.util.*;

public class MainScreenController implements Initializable {

    @FXML
    private TableView<Cell> demandTable = new TableView<>();

    @FXML
    private TableView<Cell> suppliersTable = new TableView<>();

    @FXML
    private TableView<ElementCollection> costTable = new TableView<>();

    @FXML
    private Slider supplySlider = new Slider();

    @FXML
    private Label supplyLabel = new Label();

    @FXML
    private Slider demandSlider = new Slider();

    @FXML
    private Label demandLabel = new Label();

    @FXML
    private TableColumn supplyColumn;

    @FXML
    private TableColumn demandColumn;

    private static final int INIT_VALUE = 3;

    private List<Cell> supplyList;
    private List<Cell> demandList;

    private ObservableList<Cell> observableListSupplies;
    private ObservableList<Cell> observableListDemands;
    private ObservableList<ElementCollection> costs;

    private int columnIndex;

    private ArrayList<Integer> argumentToPass_Supplies = null;
    private ArrayList<Integer> argumentToPass_demands = null;
    private ArrayList<ArrayList<Integer>> argumentToPass_CostTable = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        argumentToPass_Supplies = new ArrayList<>();
        argumentToPass_demands = new ArrayList<>();
        argumentToPass_CostTable = new ArrayList<>();

        suppliersTable.setEditable(true);
        demandTable.setEditable(true);
        costTable.setEditable(true);

        supplyColumn = new TableColumn("Supply");
        demandColumn = new TableColumn("Demand");

        /**
         * "select" a certain part of the items of the TableView , that should be displayed in a given column.
         * PropertyValueFactory can be used as cellValueFactory in a TableColumn
         */

        supplyColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        suppliersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        demandColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        demandTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setDefaultValues(); // setting default values in table.

        costTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        costTable.getSelectionModel().setCellSelectionEnabled(true);

        configureColumn(supplyColumn, "supply");
        configureColumn(demandColumn, "demand");

        suppliersTable.getSelectionModel().setCellSelectionEnabled(true);
        demandTable.getSelectionModel().setCellSelectionEnabled(true);

        costTable.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getTableColumn() != null) {
                columnIndex = newVal.getColumn();
            }
        });

        suppliersTable.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getTableColumn() != null) {
            }
        });


    }

    private void configureColumn(TableColumn column, String tableType) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Cell, String>>) editEvent -> {
                    (editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow())).setValue(viewHelp(editEvent.getNewValue()));

                    if (tableType.equalsIgnoreCase("supply")) {

                        argumentToPass_Supplies.set(editEvent.getTablePosition().getRow(), Integer.parseInt(editEvent.getNewValue()));


                    } else if (tableType.equalsIgnoreCase("demand")) {
                        argumentToPass_demands.set(editEvent.getTablePosition().getRow(), Integer.parseInt(editEvent.getNewValue()));
                    }

//                   System.out.println("editEvent.getTablePosition().getRow() = " + editEvent.getTablePosition().getRow() + "\t\t " + editEvent.getNewValue());
//                    System.out.println("argumentToPass_Supplies = " + argumentToPass_Supplies.toString());
//                    System.out.println("argumentToPass_demands.toString() = " + argumentToPass_demands.toString());
                });


    }

    private int viewHelp(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setDefaultValues() {
        supplyList = new ArrayList<>(INIT_VALUE);
        demandList = new ArrayList<>(INIT_VALUE + 1);

        // for supply
        List<Cell> sup_list = Arrays.asList(
                new Cell(100),
                new Cell(60),
                new Cell(120));

        // for demand
        List<Cell> demand_list = Arrays.asList(
                new Cell(70),
                new Cell(50),
                new Cell(100),
                new Cell(60));

        supplyList.addAll(sup_list);
        demandList.addAll(demand_list);

        for (int i = 0; i < sup_list.size(); i++) {
            argumentToPass_Supplies.add(i, sup_list.get(i).getValue());
        }

        for (int i = 0; i < demand_list.size(); i++) {
            argumentToPass_demands.add(i, demand_list.get(i).getValue());
        }

        observableListSupplies = FXCollections.observableArrayList(supplyList);
        observableListDemands = FXCollections.observableArrayList(demandList);


        suppliersTable.setItems(observableListSupplies);
        suppliersTable.getColumns().add(supplyColumn);

        demandTable.setItems(observableListDemands);
        demandTable.getColumns().add(demandColumn);

        configureSlider(supplySlider, supplyLabel, observableListSupplies, INIT_VALUE);
        configureSlider(demandSlider, demandLabel, observableListDemands, INIT_VALUE + 1);


//        setting cost table

        List<ElementCollection> costList = new ArrayList<>();


        for (int i = 0; i < observableListSupplies.size(); i++) {
            ElementCollection temp = new ElementCollection();
            argumentToPass_CostTable.add(new ArrayList<>());
            temp.setSize(observableListDemands.size());
            for (int j = 0; j < temp.getSize(); j++) {
                temp.setByIndex(j, 0);
                argumentToPass_CostTable.get(i).add(j, 0);
            }
            costList.add(temp);

        }

        updateCostTableForColumn(INIT_VALUE + 1);

        costs = FXCollections.observableArrayList(costList);

        costTable.setItems(costs);
        editRowsCostTable(INIT_VALUE);

        supplySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editRowsCostTable(newValue.intValue());
        });

        demandSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editColumnsCostTable(newValue.intValue());
            updateCostTableForColumn(newValue.intValue());
//            argumentToPass_demands = new int[newValue.intValue()];
        });


    }

    private void configureSlider(Slider sliderForBoth, Label label, ObservableList<Cell> observableList, int INIT_VALUE) {
        sliderForBoth.setMin(1);
        sliderForBoth.setMax(10);
        sliderForBoth.setValue(INIT_VALUE);
        sliderForBoth.setBlockIncrement(1); // wr bc .

        sliderForBoth.valueProperty().addListener((observableValue, previousValue, newValue) -> {

            int temp = newValue.intValue();
            label.setText(String.valueOf(temp));

            while (temp != observableList.size()) {
                if (temp > observableList.size()) {
                    observableList.add(new Cell());
                } else {
                    observableList.remove(observableList.size() - 1);
                }
            }
        });


    }


    private void editRowsCostTable(int size) {

        while (costs.size() != size) {
            if (costs.size() < size) {
                ElementCollection el = new ElementCollection();
                el.setSize(observableListDemands.size());
                for (int i = 0; i < observableListDemands.size(); i++) {
                    el.setByIndex(i, 0);
                }
                costs.add(el);
            } else {
                costs.remove(costs.size() - 1);
            }
        }
        costTable.refresh();
    }

    private void editColumnsCostTable(int size) {
        while (costs.get(0).getSize() != size) {
            for (ElementCollection e : costs)
                e.setSize(size);
        }
        costTable.refresh();
    }


    private void updateCostTableForColumn(int size) {  // adding more Column/ demand .
        costTable.getColumns().clear();

        System.out.println("size = " + size + "\t\t" + argumentToPass_CostTable.get(0).size());

/*        for (int i = 0 ; i < argumentToPass_CostTable.size() ; i++ ){

            if (size == argumentToPass_CostTable.size() ){
                argumentToPass_CostTable.get(i).set(size, 0);
            }
            else
                argumentToPass_CostTable.get(i).add(size, 0);

        }*/


        List<TableColumn> tableColumnList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            TableColumn<ElementCollection, String> column = new TableColumn<>("D" + (i + 1));

            final int increment = i;

            column.setCellValueFactory(elementStringCellDataFeatures -> new ReadOnlyStringWrapper(elementStringCellDataFeatures.getValue().getByIndex(increment) + ""));
            column.setCellFactory(TextFieldTableCell.forTableColumn());

            /*            column.setOnEditCommit(
                                    t -> t.getTableView().getItems().get(
                                            t.getTablePosition().getRow()).setByIndex(columnIndex, Integer.parseInt(t.getNewValue())));
                                column.setOnEditCommit();*/
            column.setOnEditCommit(cellEditEvent -> {
                cellEditEvent.getRowValue().setByIndex(columnIndex, Integer.parseInt(cellEditEvent.getNewValue()));
                System.out.println("cellEditEvent.getTablePosition().getRow() = " + cellEditEvent.getTablePosition().getRow());
                System.out.println("cellEditEvent.getTablePosition().getColumn() = " + cellEditEvent.getTablePosition().getColumn());
                System.out.println("cellEditEvent.getNewValue() = " + cellEditEvent.getNewValue());
//                        System.out.println("argumentToPass_CostTable.get(i).toString() = " + argumentToPass_CostTable.get(1).toString());
            });

            tableColumnList.add(column);
        }

        for (TableColumn c : tableColumnList) {
            costTable.getColumns().add(c);
        }
        costTable.refresh();

/*        for (ArrayList<Integer> a : argumentToPass_CostTable){
            System.out.println(a);
        }*/

    }


}
