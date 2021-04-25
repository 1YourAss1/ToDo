package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    ListView<Task> listViewTasks;
    @FXML
    ComboBox<String> priorityComboBox;

    ObservableList<String> prioritiesObservableList = FXCollections.observableArrayList();
    ObservableList<Task> taskObservableList = FXCollections.observableArrayList();

    ToDoTxtData toDoTxtData = new ToDoTxtData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fill priority ComboBox
        ArrayList<String> priorityArrayList = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) priorityArrayList.add("(" + c + ")");
        prioritiesObservableList.addAll(priorityArrayList.toArray(new String[0]));
        priorityComboBox.getItems().setAll(prioritiesObservableList);

        // Add data to ListView
        refreshListView();

        // Custom ListView
        listViewTasks.setCellFactory(taskListView -> {
            TaskListViewCellController taskListViewCell = new TaskListViewCellController();

            ContextMenu contextMenu = new ContextMenu();

            // Delete item in ContextMenu
            MenuItem deleteItem = new MenuItem();
            deleteItem.setText("Delete");
            deleteItem.setOnAction(event -> {
                toDoTxtData.removeDataFromToDoTxt(taskListViewCell.getIndex());
                refreshListView();
            });

            contextMenu.getItems().addAll(deleteItem);

            taskListViewCell.setContextMenu(contextMenu);

            return taskListViewCell;
        });
    }


    @FXML
    public void onEnter(){
        // Create task string
        StringBuilder strTask = new StringBuilder();
        if (priorityComboBox.getSelectionModel().getSelectedIndex() != -1) strTask.append(priorityComboBox.getSelectionModel().getSelectedItem()).append(" ");
        strTask.append(textFieldNewTask.getText());
        // Add and refresh
        toDoTxtData.addDataToToDoTxt(new Task(strTask.toString()));
        refreshListView();
        // Clear fo new task
        textFieldNewTask.setText(null);
        priorityComboBox.getSelectionModel().clearSelection();
    }

    private void refreshListView() {
        ArrayList<Task> arrayList = toDoTxtData.getDataFromToDoTxt();
        taskObservableList = FXCollections.observableArrayList(arrayList);
        listViewTasks.setItems(taskObservableList);
    }

}
