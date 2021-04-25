package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import sample.model.Task;

import java.io.IOException;

public class TaskListViewCellController extends ListCell<Task> {
    @FXML
    Label labelTask, labelPriority, labelProjects, labelTags, labelToDate;
    @FXML
    CheckBox checkBoxDone;
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

            this.setTooltip(new Tooltip(task.toString()));

            setText(null);
            setGraphic(hBox);
        }
    }

    private Color getPriorityColor (int priority) {
        double val = ((double) priority - 65) / (90 - 65) * 230;
        return Color.hsb(val, 1, 1);
    }
}
