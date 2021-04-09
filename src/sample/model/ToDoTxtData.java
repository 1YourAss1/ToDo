package sample.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoTxtData {
    private File toDoTxtFile = null;

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
        ArrayList<Task> list = new ArrayList<>();
        try {
            Scanner s = new Scanner(toDoTxtFile);
            while (s.hasNext()){
                list.add(new Task(s.next()));
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
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

}
