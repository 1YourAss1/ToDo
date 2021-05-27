package sample.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoTxtData {
    private final File toDoTxtFile;
    Synchronization synchronization = new Synchronization();

    public ToDoTxtData() {
        this.toDoTxtFile = new File("todo.txt");

        if (!toDoTxtFile.exists()) {
            try {
                toDoTxtFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Task> getDataFromToDoTxt() {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        try (FileReader reader = new FileReader(toDoTxtFile);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty()) {
                taskArrayList.add(new Task(line));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskArrayList;
    }

    public void addDataToToDoTxt(Task task) {
        try (FileWriter writer = new FileWriter(toDoTxtFile, true);
             BufferedWriter bufferWriter = new BufferedWriter(writer)) {

            bufferWriter.write(task.toString() + "\n");

//            synchronization.Synchronize(toDoTxtFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeDataFromToDoTxt(int id) {
        ArrayList<Task> taskArrayList = this.getDataFromToDoTxt();
        taskArrayList.remove(id);
        try (FileWriter writer = new FileWriter(toDoTxtFile);
             BufferedWriter bufferWriter = new BufferedWriter(writer)) {

            bufferWriter.write("");
            for (Task task : taskArrayList) {
                bufferWriter.write(task.toString() + "\n");
            }

//            synchronization.Synchronize(toDoTxtFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDataInToDoTxt(Task updatedTask, int id) {
        ArrayList<Task> taskArrayList = this.getDataFromToDoTxt();
        taskArrayList.set(id, updatedTask);
        try (FileWriter writer = new FileWriter(toDoTxtFile);
             BufferedWriter bufferWriter = new BufferedWriter(writer)) {

            bufferWriter.write("");
            for (Task task : taskArrayList) {
                bufferWriter.write(task.toString() + "\n");
            }

//            synchronization.Synchronize(toDoTxtFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
