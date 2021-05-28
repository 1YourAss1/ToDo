package sample.model;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import javafx.concurrent.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Synchronization extends Task<Void> {
    private final String path, login, pass;
    private final File todoFile;

    public Synchronization(String path, String login, String pass, File todoFile) {
        this.path = path;
        this.login = login;
        this.pass = pass;
        this.todoFile = todoFile;
    }

    @Override
    protected Void call() {
        Sardine sardine = SardineFactory.begin(login, pass);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            updateMessage("Synchronization...");
            System.out.println("Synchronization...");

            if (!sardine.exists(path)) sardine.createDirectory(path);

            List<DavResource> resources = sardine.list(path + "todo.txt");
            DavResource res = resources.get(0);

            Date cloudDate = res.getModified();
            Date clientDate = new Date(todoFile.lastModified());

            if (cloudDate.getTime() < clientDate.getTime()) {
                sardine.put(path + "todo.txt", Files.readAllBytes(todoFile.toPath()));
            } else {
                Files.copy(sardine.get(path + "todo.txt"), todoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            updateMessage("Last synchronization: " + formatForDateNow.format(new Date(todoFile.lastModified())));
            System.out.println("Last synchronization: " + formatForDateNow.format(new Date(todoFile.lastModified())));
        } catch (Exception e) {
            e.printStackTrace();
            updateMessage("Error:" + e.getMessage());
        }
        return null;
    }



    private String getDigest (InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[1024];

        while ((inputStream.read(buffer)) != -1) {
            messageDigest.update(buffer);
        }
        byte[] digest = messageDigest.digest();

        // Bytes to HEX
        StringBuilder sb = new StringBuilder();
        for(byte b : digest){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }

}
