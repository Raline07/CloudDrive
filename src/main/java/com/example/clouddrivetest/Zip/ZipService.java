package com.example.clouddrivetest.Zip;

import com.example.clouddrivetest.DTO.FileDTO;
import com.example.clouddrivetest.Entity.CustomFile;
import com.example.clouddrivetest.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

enum Action {CREATE, DELETE}

@Service
public class ZipService {

    private static Map<String, String> env = new HashMap<String, String>() {{
        put("create", "true");
    }};

    private ThreadPool threadPool;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void setThreadPool(ThreadPool threadPool){
        this.threadPool = threadPool;
    }

    public ZipThread createThread(){
        return new ZipThread();
    }

    public class ZipThread implements Runnable {

        private Thread thread;

        private String user;
        private Action action;
        private List<FileData> data;
        private List<String> path;

        public void deleteArch(String user, List<String> path) {
            this.action = Action.DELETE;
            this.path = path;
            this.user = user;
            thread = new Thread(this);
            thread.start();
        }

        public void createArch(String user, List<FileData> data) {
            this.action = Action.CREATE;
            this.user = user;
            this.data = data;
            thread = new Thread(this);
            thread.start();
        }

        private void dirCheck() {
            File dir = new File("files");
            if (!dir.exists())
                dir.mkdir();
        }

        private void createArchTh() {
            dirCheck();

            List<CustomFile> files = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Path filepath = Paths.get("files" + File.separator + user + ".zip");
            URI uri = URI.create("jar:" + filepath.toUri());
            try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
                for (FileData fileData : data) {
                    Path pathInZipfile = zipfs.getPath("/" + fileData.getName());
                    try (InputStream fis = fileData.getIs()) {
                        Files.copy(fis, pathInZipfile, StandardCopyOption.REPLACE_EXISTING);
                        files.add(new CustomFile(fileData.getName(), fileData.getSize(), sdf.format(new Date()), pathInZipfile.toString()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            userService.addFiles(user, files);
            List<FileDTO> dtos = new ArrayList<>();
            for (CustomFile file : files) {
                dtos.add(FileDTO.from(file));
            }
            messagingTemplate.convertAndSendToUser(""+user.hashCode(), "/uploading", dtos);
        }

        private void deleteArchTh() {
            Path filepath = Paths.get("files" + File.separator + user + ".zip");
            URI uri = URI.create("jar:" + filepath.toUri());
            try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
                for (String p : path) {
                    Path pathInZipfile = zipfs.getPath("/" + p);
                    Files.delete(pathInZipfile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            switch (action) {
                case CREATE:
                    createArchTh();
                    break;
                case DELETE:
                    deleteArchTh();
                    break;
            }
            threadPool.put(this);
        }
    }
}