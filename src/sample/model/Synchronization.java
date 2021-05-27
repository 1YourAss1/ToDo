package sample.model;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Synchronization extends Task<Void> {
    private String path, login, pass;
    private File todoFile;

    public void setParameters(String path, String login, String pass, File todoFile) {
        this.path = path;
        this.login = login;
        this.pass = pass;
        this.todoFile = todoFile;
    }

    @Override
    protected Void call() {
        Sardine sardine = SardineFactory.begin(login, pass);
        try {
            updateMessage("Synchronization...");

            if (!sardine.exists(path)) sardine.createDirectory(path);

            sardine.put(path + "todo.txt", Files.readAllBytes(todoFile.toPath()));

//            List<DavResource> resources = sardine.list(path);
//            for (DavResource res : resources) {
//                System.out.println(res);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            updateMessage("Error:" + e.getMessage());
        }

        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        updateMessage("Last synchronization: " + formatForDateNow.format(dateNow));
        return null;
    }


}
