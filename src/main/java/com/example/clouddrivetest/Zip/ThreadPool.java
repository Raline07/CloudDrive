package com.example.clouddrivetest.Zip;

import org.springframework.stereotype.Component;
import com.example.clouddrivetest.UserService;


import java.util.List;

@Component
public class ThreadPool extends AbstractPool<ZipThread> {

    private final int maxThreads = Runtime.getRuntime().availableProcessors();

    public ThreadPool(UserService userService) {
        add(new ZipThread(userService, this));
        for (int i = 1; i < maxThreads; i++) {
            add(new ZipThread());
        }
    }

    public void createArch(String user, List<FileData> data) {
        check().createArch(user, data);
    }

    public void deleteArch(String user, List<String> path) {
        check().deleteArch(user, path);

    }

    private synchronized ZipThread check() {
        ZipThread zipThread;
        while (true) {
            zipThread = get();
            if (zipThread == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            return zipThread;
        }
    }
}
