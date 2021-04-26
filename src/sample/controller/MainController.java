package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import sample.model.Synchronization;
import sample.model.Task;
import sample.model.ToDoTxtData;

import java.io.IOException;
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
            TaskListCell taskListCell = new TaskListCell();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem();
            editItem.setText("Edit");
            editItem.setOnAction(event -> {
                taskListCell.setEditable(true);
//                toDoTxtData.removeDataFromToDoTxt(taskListCell.getIndex());
//                refreshListView();
            });
            // Delete item in ContextMenu
            MenuItem deleteItem = new MenuItem();
            deleteItem.setText("Delete");
            deleteItem.setOnAction(event -> {
                toDoTxtData.removeDataFromToDoTxt(taskListCell.getIndex());
                refreshListView();
            });

            contextMenu.getItems().addAll(editItem, deleteItem);
            taskListCell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        taskListCell.setContextMenu(null);
                    } else {
                        taskListCell.setContextMenu(contextMenu);
                    }
                });

            return taskListCell;
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

    class TaskListCell extends ListCell<Task> {
        @FXML
        Label labelTask, labelPriority, labelProjects, labelTags, labelToDate;
        @FXML
        CheckBox checkBoxDone;
        @FXML
        HBox hBox;

        public TaskListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/taskCellView.fxml"));
                loader.setController(this);
                loader.load();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                // CheckBox
                setCheckBox(task);
                // Custom task text
                setTaskText(task);

                setTooltip(new Tooltip(task.toString()));

                setText(null);
                setGraphic(hBox);
            }
        }

        private void setCheckBox(Task task) {
            checkBoxDone.setSelected(task.isDone());
            checkBoxDone.selectedProperty().addListener((observableValue, old_val, new_val) -> {
                task.setDone(new_val);
                toDoTxtData.updateDataInToDoTxt(task, getIndex());
            });
        }

        private void setTaskText(Task task) {
            // Custom priority
            if (task.getStringPriority() != null) {
                labelPriority.setText(task.getStringPriority());
                labelPriority.setTextFill(getPriorityColor(task.getIntPriority()));
                labelPriority.setPadding(new Insets(0, 5, 0, 0));
            }

            // Custom task
            labelTask.setText(task.getTask());

            StringBuilder stringBuilder;
            // Custom projects
            if (!task.getProjects().isEmpty()) {
                stringBuilder = new StringBuilder();
                for (String project: task.getProjects()) {
                    stringBuilder.append(project).append(" ");
                }
                labelProjects.setText(stringBuilder.toString().trim());
                labelProjects.setTextFill(Color.GREEN);
                labelProjects.setPadding(new Insets(0, 0, 0, 5));
            }

            // Custom tags
            if (!task.getTags().isEmpty()) {
                stringBuilder = new StringBuilder();
                for (String tags: task.getTags()) {
                    stringBuilder.append(tags).append(" ");
                }
                labelTags.setText(stringBuilder.toString().trim());
                labelTags.setTextFill(Color.BLUE);
                labelTags.setPadding(new Insets(0, 0, 0, 5));
            }

            if (task.getToDate() != null) {
                labelToDate.setText(task.getToDate());
                labelToDate.setTextFill(Color.GRAY);
                labelToDate.setPadding(new Insets(0, 0, 0, 5));
            }
        }

        private Color getPriorityColor (int priority) {
            double val = ((double) priority - 65) / (90 - 65) * 230;
            return Color.hsb(val, 1, 1);
        }
    }

}
