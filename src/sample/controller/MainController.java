package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.model.Task;
import sample.model.ToDoTxtData;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextField textFieldNewTask;
    @FXML
    ListView<String> listViewTasks;

    ObservableList<String> taskObservableList = FXCollections.observableArrayList();

    ToDoTxtData toDoTxtData = new ToDoTxtData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshListView();

        listViewTasks.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

//            MenuItem editItem = new MenuItem();
//            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
//            editItem.setOnAction(event -> {
//                // code to edit item...
//            });

            MenuItem deleteItem = new MenuItem();
            deleteItem.setText("Delete");
            deleteItem.setOnAction(event -> {
                toDoTxtData.removeDataFromToDoTxt(cell.getIndex());
                refreshListView();
//                listViewTasks.getItems().remove(cell.getItem());
            });

            contextMenu.getItems().addAll(deleteItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });

    }


    @FXML
    public void onEnter(ActionEvent event){
        toDoTxtData.addDataToToDoTxt(new Task(textFieldNewTask.getText()));
        refreshListView();
        textFieldNewTask.setText(null);
    }

    private void refreshListView() {
        ArrayList<String> arrayList = toDoTxtData.getDataFromToDoTxt();
        taskObservableList = FXCollections.observableArrayList(arrayList);
        listViewTasks.setItems(taskObservableList);
    }

}
