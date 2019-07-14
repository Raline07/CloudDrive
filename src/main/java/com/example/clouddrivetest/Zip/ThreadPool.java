package com.example.clouddrivetest.Zip;


import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ThreadPool extends AbstractPool<ZipService.ZipThread> {

    private final int maxThreads = Runtime.getRuntime().availableProcessors();

    public ThreadPool(ZipService zipService) {
        zipService.setThreadPool(this);
        for (int i = 0; i < maxThreads; i++) {
            add(zipService.createThread());
        }
    }

    public void createArch(String user, List<FileData> data) {
        check().createArch(user, data);
    }

    public void deleteArch(String user, List<String> path) {
        check().deleteArch(user, path);

    }

    private synchronized ZipService.ZipThread check() {
        ZipService.ZipThread zipThread;
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
