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

    private static final String PATH = "http://109.225.41.139/owncloud/remote.php/webdav/ToDo/";

    public void Synchronize (File todoFile) {
        Sardine sardine = SardineFactory.begin("login", "password");
        try {
            if (!sardine.exists(PATH)) sardine.createDirectory(PATH);

            sardine.put(PATH + "todo.txt", Files.readAllBytes(todoFile.toPath()));

            List<DavResource> resources = sardine.list(PATH);
            for (DavResource res : resources)
            {
                System.out.println(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
