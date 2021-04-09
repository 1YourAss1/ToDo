package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sample.model.Task;
import sample.model.ToDoTxtData;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextField textFieldNewTask;
    @FXML
    ListView<Task> listViewTasks;

    ObservableList<Task> taskObservableList = FXCollections.observableArrayList();

    ToDoTxtData toDoTxtData = new ToDoTxtData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshListView();
    }

    @FXML
    public void onEnter(ActionEvent event){
        toDoTxtData.addDataToToDoTxt(new Task(textFieldNewTask.getText()));
        refreshListView();
        textFieldNewTask.setText(null);
    }

    private void refreshListView() {
        ArrayList<Task> arrayList = toDoTxtData.getDataFromToDoTxt();
        taskObservableList = FXCollections.observableArrayList(arrayList);
        listViewTasks.setItems(taskObservableList);
    }

}
