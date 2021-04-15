package sample.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoTxtData {
    private final File toDoTxtFile;

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

    public ArrayList<String> getDataFromToDoTxt() {
        ArrayList<String> taskArrayList = new ArrayList<>();
        try {
            FileReader reader = new FileReader(toDoTxtFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            taskArrayList.add(new Task(line).toString());
            while (line != null) {
                line = bufferedReader.readLine();
                taskArrayList.add(new Task(line).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        taskArrayList.remove(taskArrayList.size() - 1);
        return taskArrayList;
    }

    public void addDataToToDoTxt(Task task) {
        try {
            FileWriter writer = new FileWriter(toDoTxtFile, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(task.toString() + "\n");
            bufferWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeDataFromToDoTxt(int id) {
        ArrayList<String> taskArrayList = this.getDataFromToDoTxt();
        taskArrayList.remove(id);
        try {
            FileWriter writer = new FileWriter(toDoTxtFile);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write("");
            writer = new FileWriter(toDoTxtFile, true);
            bufferWriter = new BufferedWriter(writer);
            for (String task : taskArrayList) {
                bufferWriter.write(task + "\n");
            }
            bufferWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
