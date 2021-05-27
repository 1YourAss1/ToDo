package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Synchronization;
import sample.model.Task;
import sample.model.ToDoTxtData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.prefs.Preferences;

public class MainController implements Initializable {
    @FXML
    public DatePicker datePicker;
    @FXML
    public MenuButton projectMenuButton;
    public Label synchronizeLabel;
    @FXML
    private MenuButton tagsMenuButton;
    @FXML
    private TextField textFieldNewTask;
    @FXML
    private ListView<Task> listViewTasks;
    @FXML
    private ComboBox<String> priorityComboBox;

    private final ObservableList<String> prioritiesObservableList = FXCollections.observableArrayList();
    private Set<String> tagsSet, projectSet;

    private final File todoFile = new File("todo.txt");
    private final ToDoTxtData toDoTxtData = new ToDoTxtData();

    private Synchronization synchronizationTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tagsSet = new LinkedHashSet<>();
        projectSet = new LinkedHashSet<>();
        // Fill priority ComboBox
        ArrayList<String> priorityArrayList = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) priorityArrayList.add("(" + c + ")");
        prioritiesObservableList.addAll(priorityArrayList.toArray(new String[0]));
        priorityComboBox.getItems().setAll(prioritiesObservableList);

        // Custom ListView
        listViewTasks.setCellFactory(taskListView -> {
            TaskListCell taskListCell = new TaskListCell();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem();
            editItem.setText("Edit");
            editItem.setOnAction(event -> {
//                taskListCell.setEditable(true);
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

        // Fill ListView
        refreshListView();

        synchronizationTask = new Synchronization();
        synchronizeLabel.textProperty().bind(synchronizationTask.messageProperty());

    }

    public void onEnter(){
        // Create task string
        StringBuilder strTask = new StringBuilder();
        // Priority
        if (priorityComboBox.getSelectionModel().getSelectedIndex() != -1) strTask.append(priorityComboBox.getSelectionModel().getSelectedItem()).append(" ");
        // Task
        strTask.append(textFieldNewTask.getText());
        // Project
        ObservableList<MenuItem> projectMenuButtonItems = projectMenuButton.getItems();
        for (int i = 2; i < projectMenuButtonItems.size(); i++) {
            RadioMenuItem radioMenuItem = (RadioMenuItem) projectMenuButtonItems.get(i);
            if (radioMenuItem.isSelected()) {
                strTask.append(" ").append("+").append(radioMenuItem.getText());
            }
        }
        // Tags
        ObservableList<MenuItem> tagsMenuButtonItems = tagsMenuButton.getItems();
        for (int i = 2; i < tagsMenuButtonItems.size(); i++) {
            CustomMenuItem customMenuItem = (CustomMenuItem) tagsMenuButtonItems.get(i);
            CheckBox checkBox = (CheckBox) customMenuItem.getContent();
            if (checkBox.isSelected()) {
                strTask.append(" ").append("@").append(checkBox.getText());
            }
        }
        // Due Date
        LocalDate dueDate = datePicker.getValue();
        if (dueDate != null) {
            strTask.append(" due:").append(dueDate);
            datePicker.getEditor().clear();
        }

        // Add and refresh
        toDoTxtData.addDataToToDoTxt(new Task(strTask.toString()));
        refreshListView();
        // Clear fo new task
        textFieldNewTask.setText(null);
        priorityComboBox.getSelectionModel().clearSelection();
    }

    public void createProjectMenuButton(Set<String> projects) {
        ToggleGroup toggleGroup = new ToggleGroup();
        projectMenuButton.getItems().clear();
        MenuItem addProjectMenuItem = new MenuItem("Add project");
        addProjectMenuItem.setOnAction(actionEvent -> {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setHeaderText(null);
            textInputDialog.setGraphic(null);
            textInputDialog.setTitle("Add new project");
            textInputDialog.setContentText("New project: ");
            textInputDialog.showAndWait()
                    .filter(res -> !res.isEmpty() && res.split(" ").length == 1)
                    .ifPresent(res -> {
                        projectSet.add(res);
                        RadioMenuItem radioMenuItem = new RadioMenuItem(res);
                        radioMenuItem.setToggleGroup(toggleGroup);
                        radioMenuItem.setSelected(true);
                        projectMenuButton.getItems().add(radioMenuItem);
                    });
        });
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        projectMenuButton.getItems().addAll(addProjectMenuItem, separatorMenuItem);


        for (String project: projects) {
            RadioMenuItem radioMenuItem = new RadioMenuItem(project);
            radioMenuItem.setToggleGroup(toggleGroup);
            projectMenuButton.getItems().add(radioMenuItem);
        }
    }

    public void createTagsMenuButton(Set<String> tags) {
        tagsMenuButton.getItems().clear();
        MenuItem addTagMenuItem = new MenuItem("Add tag");
        addTagMenuItem.setOnAction(actionEvent -> {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setHeaderText(null);
            textInputDialog.setGraphic(null);
            textInputDialog.setTitle("Add new tag");
            textInputDialog.setContentText("New tag: ");
            textInputDialog.showAndWait()
                    .filter(res -> !res.isEmpty() && res.split(" ").length == 1)
                    .ifPresent(res -> {
                        tagsSet.add(res);
                        CheckBox checkBox = new CheckBox(res);
                        checkBox.setSelected(true);
                        CustomMenuItem customMenuItem = new CustomMenuItem(checkBox);
                        customMenuItem.setHideOnClick(false);
                        tagsMenuButton.getItems().add(customMenuItem);
                    });
        });
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        tagsMenuButton.getItems().addAll(addTagMenuItem, separatorMenuItem);

        for (String tag: tags) {
            CustomMenuItem customMenuItem = new CustomMenuItem(new CheckBox(tag));
            customMenuItem.setHideOnClick(false);
            tagsMenuButton.getItems().add(customMenuItem);
        }
    }

    private void refreshListView() {
        ArrayList<Task> arrayListTask = toDoTxtData.getDataFromToDoTxt();
        ObservableList<Task> taskObservableList = FXCollections.observableArrayList(arrayListTask);
        listViewTasks.setItems(taskObservableList);

        projectSet.clear();
        tagsSet.clear();
        for (Task task: arrayListTask) {
            if (task.getProject() != null) projectSet.add(task.getProject());
            tagsSet.addAll(task.getTags());
        }
        createProjectMenuButton(projectSet);
        createTagsMenuButton(tagsSet);
    }

    // Menu Items
    public void onMenuItemSynchronize() {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        String url = preferences.get("url", "Url");
        String login = preferences.get("login", "Login");
        String pass = preferences.get("password", "Password");

        synchronizationTask.setParameters(url, login, pass,todoFile);
        new Thread(synchronizationTask).start();
    }

    public void onMenuItemExit() {
        System.exit(0);
    }

    public void onMenuItemSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/settingsView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 300, 300));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Custom ListView
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

            // Custom projects
            if (task.getProject() != null) {
                labelProjects.setText(task.getProject());
                labelProjects.setTextFill(Color.GREEN);
                labelProjects.setPadding(new Insets(0, 0, 0, 5));
            }

            // Custom tags
            if (!task.getTags().isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
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
