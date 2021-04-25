package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import sample.model.Task;

import java.io.IOException;

public class TaskListViewCellController extends ListCell<Task> {
    @FXML
    Label priorityLabel, taskLabel;

    @FXML
    HBox hBox;

    public TaskListViewCellController() {
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
            priorityLabel.setText(task.getPriority());
            taskLabel.setText(task.getTask());

            this.setTooltip(new Tooltip(task.toString()));

            setText(null);
            setGraphic(hBox);
        }
    }
}
