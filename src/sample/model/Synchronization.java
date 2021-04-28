package sample.model;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Synchronization {



    public void Synchronize (String path, String login, String pass, File todoFile) {
        Sardine sardine = SardineFactory.begin(login, pass);
        try {
            if (!sardine.exists(path)) sardine.createDirectory(path);

            sardine.put(path + "todo.txt", Files.readAllBytes(todoFile.toPath()));

            List<DavResource> resources = sardine.list(path);
            for (DavResource res : resources)
            {
                System.out.println(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
