package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.model.Task;

import java.awt.*;
import java.io.IOException;

public class TaskListViewCellController extends ListCell<Task> {
    @FXML
    Label labelTask, labelPriority;
    @FXML
    CheckBox checkBoxDone;

    @FXML
    ImageView priorityImage;

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
            labelPriority.setText(task.getPriority());
            labelTask.setText(task.getTask());

            this.setTooltip(new Tooltip(task.toString()));

            setText(null);
            setGraphic(hBox);
        }
    }
}
