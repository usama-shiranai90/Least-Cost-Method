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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        suppliersTable.setEditable(true);
        demandTable.setEditable(true);

        supplyColumn = new TableColumn("Supply");
        demandColumn = new TableColumn("Demand");

        supplyColumn.setCellValueFactory(
                new PropertyValueFactory<>("value")
        );
        suppliersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        demandColumn.setCellValueFactory(
                new PropertyValueFactory<>("value"));
        demandTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setDefaultValues();



        costTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        costTable.setEditable(true);
        costTable.getSelectionModel().setCellSelectionEnabled(true);

        configureColumn(supplyColumn);
        configureColumn(demandColumn);
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

    private void configureColumn(TableColumn column) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Cell, String>>) t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setValue(viewHelp(t.getNewValue()))
        );
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
        demandList = new ArrayList<>(INIT_VALUE);

        Cell s1 = new Cell();
        Cell s2 = new Cell();
        Cell s3 = new Cell();
        s1.setValue(40);
        s2.setValue(30);
        s3.setValue(20);

        Cell s4 = new Cell();
        Cell s5 = new Cell();
        Cell s6 = new Cell();
        s4.setValue(20);
        s5.setValue(40);
        s6.setValue(30);


        supplyList.addAll(Arrays.asList(s1, s2, s3));
        demandList.addAll(Arrays.asList(s4, s5, s6));

        observableListSupplies = FXCollections.observableArrayList(supplyList);
        observableListDemands = FXCollections.observableArrayList(demandList);


        suppliersTable.setItems(observableListSupplies);
        suppliersTable.getColumns().add(supplyColumn);

        demandTable.setItems(observableListDemands);
        demandTable.getColumns().add(demandColumn);

        configureSlider(supplySlider, supplyLabel, observableListSupplies);
        configureSlider(demandSlider, demandLabel, observableListDemands);

//        setting cost table

        List<ElementCollection> costList = new ArrayList<>();

        for (int i = 0; i < observableListSupplies.size(); i++) {
            ElementCollection temp = new ElementCollection();
            temp.setSize(observableListDemands.size());
            for (int j = 0; j < temp.getSize(); j++) {
                temp.setByIndex(j, 0);
            }
            costList.add(temp);
        }

        updatecostTableForColumn(INIT_VALUE);

        costs = FXCollections.observableArrayList(costList);

        costTable.setItems(costs);
        editRowsCostTable(INIT_VALUE);

        supplySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editRowsCostTable(newValue.intValue());
        });

        demandSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editColumnsCostTable(newValue.intValue());
            updatecostTableForColumn(newValue.intValue());
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


    private void configureSlider(Slider sliderForBoth, Label label, ObservableList<Cell> observableList) {
        sliderForBoth.setMin(1);
        sliderForBoth.setMax(6);
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


    private void updatecostTableForColumn(int size) {
        costTable.getColumns().clear();

        List<TableColumn> tableColumnList = new ArrayList<>(size);

        for (int i=0 ;  i < size ;i++){
            TableColumn<ElementCollection, String> column = new TableColumn<>("D"+(i+1));

            final int increment = i ;
            column.setCellValueFactory(elementStringCellDataFeatures -> new ReadOnlyStringWrapper(elementStringCellDataFeatures.getValue().getByIndex(increment) + ""));

            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    t -> t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setByIndex(columnIndex, Integer.parseInt(t.getNewValue())));
            tableColumnList.add(column);
        }

        for (TableColumn c :
                tableColumnList) {
            costTable.getColumns().add(c);
        }
        costTable.refresh();

    }


}
