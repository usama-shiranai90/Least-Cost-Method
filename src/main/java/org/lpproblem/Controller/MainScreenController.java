package org.lpproblem.Controller;

import com.jfoenix.controls.JFXSlider;
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
import javafx.scene.input.MouseEvent;
import org.lpproblem.Data.Cell;
import org.lpproblem.Data.ElementCollection;
import org.lpproblem.Data.LCM;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainScreenController implements Initializable {

    @FXML
    private TableView<Cell> demandTableView = new TableView<>();

    @FXML
    private TableView<Cell> suppliersTableView = new TableView<>();

    @FXML
    private TableView<ElementCollection> costTableView = new TableView<>();


    @FXML
    private JFXSlider supplySlider = new JFXSlider();

    @FXML
    private Label supplyLabel = new Label();

    @FXML
    private JFXSlider demandSlider = new JFXSlider();

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
    private ObservableList<ElementCollection> costsObservableList;

    private int columnIndex;

    private ArrayList<Integer> argumentToPass_Supplies = null;
    private ArrayList<Integer> argumentToPass_demands = null;
    private ArrayList<ArrayList<Integer>> argumentToPass_CostTable = null;

    private int selectedRow;
    private int selectedColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initalizeArrayList();

        setTableViewEditable();

        setInitialColumnSetting();  // supply and demand table initialization , cost Table set editable

        setDefaultValues(); // setting default values in table.

        configureColumn(supplyColumn, "supply");
        configureColumn(demandColumn, "demand");

        getAnyTableViewData(suppliersTableView);
        getAnyTableViewData(demandTableView);

        costTableView.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getTableColumn() != null) {
                columnIndex = newVal.getColumn();
            }
        });


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

        observableListSupplies = FXCollections.observableArrayList(supplyList);
        observableListDemands = FXCollections.observableArrayList(demandList);

        suppliersTableView.setItems(observableListSupplies);
        suppliersTableView.getColumns().add(supplyColumn);

        demandTableView.setItems(observableListDemands);
        demandTableView.getColumns().add(demandColumn);

        setArgumentToPassBoth(argumentToPass_Supplies, sup_list);
        setArgumentToPassBoth(argumentToPass_demands, demand_list);

        configureSlider(supplySlider, supplyLabel, observableListSupplies);
        configureSlider(demandSlider, demandLabel, observableListDemands);


//        setting cost table
        List<ElementCollection> tempCost = new ArrayList<>();

        for (int i = 0; i < observableListSupplies.size(); i++) {
            ElementCollection temp = new ElementCollection();
            argumentToPass_CostTable.add(new ArrayList<>());
            temp.setSize(observableListDemands.size());

            for (int j = 0; j < temp.getSize(); j++) {
                temp.setByIndex(j, 1);
                argumentToPass_CostTable.get(i).add(j, 1);
            }
            tempCost.add(temp);
        }

        updateCostTableForColumn(INIT_VALUE + 1);

        costsObservableList = FXCollections.observableArrayList(tempCost);

        costTableView.setItems(costsObservableList);
        editRowsCostTable(INIT_VALUE);

        supplySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editRowsCostTable(newValue.intValue());
        });

        demandSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editColumnsCostTable(newValue.intValue());
            updateCostTableForColumn(newValue.intValue()); // size
//            argumentToPass_demands = new int[newValue.intValue()];
        });


    }

    private void configureSlider(Slider sliderForBoth, Label label, ObservableList<Cell> observableList) {
/*        sliderForBoth.setMin(1);
        sliderForBoth.setMax(10);
        sliderForBoth.setValue(INIT_VALUE);

        sliderForBoth.setBlockIncrement(1);
        sliderForBoth.setMajorTickUnit(1);
        sliderForBoth.setSnapToTicks(true);
        sliderForBoth.setShowTickMarks(true);*/

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

    // adding or deleting rows to cost table.
    private void editRowsCostTable(int size) {

        while (costsObservableList.size() != size) {
            if (costsObservableList.size() < size) {
                ElementCollection el = new ElementCollection();
                el.setSize(observableListDemands.size());
                for (int i = 0; i < observableListDemands.size(); i++) {
                    el.setByIndex(i, 1);
                }
                costsObservableList.add(el);
            } else {
                costsObservableList.remove(costsObservableList.size() - 1);
            }
        }
        costTableView.refresh();

//        System.out.println("supplySlider =  " + supplySlider.getValue() + "\t\tdemandSlider =" + demandSlider.getValue() + "\t\t");
        updateArgumentToPass_CostTable((int) supplySlider.getValue(), (int) demandSlider.getValue(), "editRow");
    }

    private void editColumnsCostTable(int size) {
        while (costsObservableList.get(0).getSize() != size) {
            for (ElementCollection e : costsObservableList)
                e.setSize(size);
        }
        costTableView.refresh();

        updateArgumentToPass_CostTable((int) supplySlider.getValue(), (int) demandSlider.getValue(), "editColumn");
    }

    private void configureColumn(TableColumn column, String tableType) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Cell, String>>) editEvent -> {
                    int oldValue ;
                    System.out.println("editEvent.getNewValue() = " + editEvent.getNewValue());
                    if (editEvent.getNewValue().equalsIgnoreCase("Enter Value")){
                        oldValue = 0 ;
                        System.out.println("fuck = " );
                    }
                     else{
                        System.out.println("lover");
                        oldValue = Integer.parseInt(editEvent.getOldValue());
                    }



                    (editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow())).setValue(viewHelp(editEvent.getNewValue()));

                    int newValue = editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow()).getValue();

                    if (tableType.equalsIgnoreCase("supply")) {

                        if (oldValue != newValue) {

                        }


                        if (argumentToPass_Supplies.size() <= (int) supplySlider.getValue()) { // 3 <  2
                            argumentToPass_Supplies.add(editEvent.getTablePosition().getRow(), Integer.parseInt(editEvent.getNewValue()));

                            System.out.println("Supply -> getRow() = " + editEvent.getTablePosition().getRow() + "\t\t" + editEvent.getNewValue());

                        } else {
                            argumentToPass_Supplies.remove(argumentToPass_Supplies.size() - 1);
                        }
//                        System.out.println("argumentToPass_Supplies = " + argumentToPass_Supplies.toString());
                    } else if (tableType.equalsIgnoreCase("demand")) {

                        if (argumentToPass_demands.size() < (int) demandSlider.getValue()) { // 3 <  2
                            argumentToPass_demands.add(editEvent.getTablePosition().getRow(), Integer.parseInt(editEvent.getNewValue()));
                            System.out.println("Demand ->getRow() = " + editEvent.getTablePosition().getRow() + "\t\t" + editEvent.getNewValue());

                        } else {
                            argumentToPass_demands.remove(argumentToPass_demands.size() - 1);
                        }

//                        System.out.println("argumentToPass_demands = " + argumentToPass_demands.toString());
                    }
/*                   System.out.println("editEvent.getTablePosition().getRow() = " + editEvent.getTablePosition().getRow() + "\t\t " + editEvent.getNewValue());
                    System.out.println("argumentToPass_Supplies = " + argumentToPass_Supplies.toString());
                    System.out.println("argumentToPass_demands.toString() = " + argumentToPass_demands.toString());*/
                });


    }

    private int viewHelp(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateCostTableForColumn(int size) {  // adding more Column/ demand .
        costTableView.getColumns().clear();

        List<TableColumn> tableColumnList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            TableColumn<ElementCollection, String> column = new TableColumn<>("D" + (i + 1));

            final int increment = i;
            column.setCellValueFactory(elementStringCellDataFeatures -> {
                return new ReadOnlyStringWrapper(elementStringCellDataFeatures.getValue().getByIndex(increment) + ""); //
            });

            column.setCellFactory(TextFieldTableCell.forTableColumn());

            /*            column.setOnEditCommit(
                                    t -> t.getTableView().getItems().get(
                                            t.getTablePosition().getRow()).setByIndex(columnIndex, Integer.parseInt(t.getNewValue())));
                                column.setOnEditCommit();*/
            column.setOnEditCommit(cellEditEvent -> {
                cellEditEvent.getRowValue().setByIndex(columnIndex, Integer.parseInt(cellEditEvent.getNewValue()));
                argumentToPass_CostTable.get(cellEditEvent.getTablePosition().getRow())
                        .set(cellEditEvent.getTablePosition().getColumn(), Integer.parseInt(cellEditEvent.getNewValue()));

                System.out.println("cellEditEvent.getNewValue() = " + cellEditEvent.getNewValue());

            });
            tableColumnList.add(column);
        }

        for (TableColumn c : tableColumnList) {
            costTableView.getColumns().add(c);
        }
        costTableView.refresh();
        System.out.println("size = " + size + "\t\t Rows: " + argumentToPass_CostTable.size() + "\t\t Columns:" + argumentToPass_CostTable.get(0).size());

    }

    private void updateArgumentToPass_CostTable(int supply_row, int demand_column, String checkBi) {
/*        argumentToPass_CostTable = new ArrayList<>();

        for (int r = 0; r < supply_row; r++) {
            argumentToPass_CostTable.add(r, new ArrayList<>());
            for (int c = 0; c < demand_column; c++) {
                argumentToPass_CostTable.get(r).add(c, 0);
            }
        }*/

        if (checkBi.equalsIgnoreCase("editRow")) {  // row add ya kam .
            for (int i = 0; i < supply_row; i++) {
                if (supply_row > argumentToPass_CostTable.size()) {  //  3  3
                    argumentToPass_CostTable.add(new ArrayList<>());
                    for (int c = 0; c < argumentToPass_CostTable.get(0).size(); c++) {
                        argumentToPass_CostTable.get(supply_row - 1).add(1);
                    }
                } else if (supply_row < argumentToPass_CostTable.size()) {
                    argumentToPass_CostTable.get(argumentToPass_CostTable.size() - 1).clear();
                    argumentToPass_CostTable.remove(argumentToPass_CostTable.size() - 1);
                    break;
                }
            }
        } else {
            for (int i = 0; i < argumentToPass_CostTable.size(); i++) {  // 3 rows.

                if (demand_column > argumentToPass_CostTable.get(i).size())  // 5 > 4
                {
                    argumentToPass_CostTable.get(i).add(argumentToPass_CostTable.get(i).size(), 1);
                } else if (demand_column < argumentToPass_CostTable.get(i).size()) {  //  2 < 3
                    argumentToPass_CostTable.get(i).remove(demand_column);
                }
            }
        }
//        System.out.println("supply_row = " + supply_row + "\t\t" + argumentToPass_CostTable.size());

    }

    public void initalizeArrayList() {
        argumentToPass_Supplies = new ArrayList<>();
        argumentToPass_demands = new ArrayList<>();
        argumentToPass_CostTable = new ArrayList<>();

    }

    public void setTableViewEditable() {
        suppliersTableView.setEditable(true);
        demandTableView.setEditable(true);
        costTableView.setEditable(true);
    }

    public void setInitialColumnSetting() {
        supplyColumn = new TableColumn("Supply");
        demandColumn = new TableColumn("Demand");

        supplyColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        demandColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        suppliersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        demandTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        costTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        suppliersTableView.getSelectionModel().setCellSelectionEnabled(true);
        demandTableView.getSelectionModel().setCellSelectionEnabled(true);
        costTableView.getSelectionModel().setCellSelectionEnabled(true);

    }

    private void getAnyTableViewData(TableView<Cell> TableView) {
        TableView.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getTableColumn() != null) {
                selectedRow = newVal.getRow();
                selectedColumn = newVal.getColumn();
//                System.out.println("selectedRow = " + selectedRow + "\t\tselectedColumn = " + selectedColumn);
            }


        });

    }

    private void setArgumentToPassBoth(ArrayList<Integer> argumentToPass, List<Cell> list) {
        for (int i = 0; i < list.size(); i++) {
            argumentToPass.add(i, list.get(i).getValue());
        }
    }


    @FXML
    void performCalculation(MouseEvent event) {
/*        System.out.println("argumentToPass_Supplies = " + argumentToPass_Supplies.toString());
        System.out.println("argumentToPass_demands = " + argumentToPass_demands.toString());

        System.out.println("Testing Cost Table :");
        for (ArrayList<Integer> a : argumentToPass_CostTable) {
            System.out.println(a);
        }*/

        System.out.println("argumentToPass_Supplies = " + argumentToPass_Supplies.toString());
        System.out.println("argumentToPass_demands.toString() = " + argumentToPass_demands.toString());

//        LCM leastCostMethod = new LCM(argumentToPass_Supplies, argumentToPass_demands, argumentToPass_CostTable);
//        leastCostMethod.solveLCM();

    }
}
