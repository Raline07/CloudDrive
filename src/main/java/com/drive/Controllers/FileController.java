package com.drive.Controllers;

import com.drive.DTO.FileDTO;
import com.drive.DTO.PageCountDTO;
import com.drive.UserService;
import com.drive.Zip.ThreadPool;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.drive.Entity.CustomFile;
import com.drive.Zip.FileData;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class FileController {

    @Autowired
    private UserService userService;

    @Autowired
    private ThreadPool threadPool;

    private Map<String, String> env = new HashMap<String, String>() {{
        put("create", "true");
    }};

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/files/upload")
    public ResponseEntity<Void> FileUpload(@RequestParam(value = "files[]") MultipartFile[] multipartFile) throws IOException {
        String login = getLogin();
        userService.setUploading(login, true);
        List<FileData> data = new ArrayList<>();
        for (MultipartFile mf : multipartFile) {
            data.add(new FileData(mf.getOriginalFilename(), mf.getSize(), mf.getInputStream()));
        }
        threadPool.createArch(login, data);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/files/delete")
    public ResponseEntity<Void> delFile(@RequestParam(value = "names[]") String[] names) {
        String login = getLogin();
        threadPool.deleteArch(login, userService.deleteFiles(login, names));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/files/updating")
    @ResponseBody
    public PageCountDTO updating() {
        String login = getLogin();
        if (userService.findByLogin(login).isUploading())
            return PageCountDTO.of(userService.count(login), 15);
        return null;
    }

    @GetMapping("/files/count")
    @ResponseBody
    public PageCountDTO count() {
        String login = getLogin();
        return PageCountDTO.of(userService.count(login), 15);
    }

    @GetMapping("/files")
    @ResponseBody
    public List<FileDTO> tasks(@RequestParam(required = false, defaultValue = "0") Integer page) {
        String login = getLogin();

        return userService.getFiles(login, PageRequest.of(page, 15, Sort.Direction.DESC, "id"));
    }

    @GetMapping("/files/download/{photo_id}")
    @ResponseBody
    public ResponseEntity<byte[]> onFile(@PathVariable("photo_id") String list) {
        String login = getLogin();
        List<String> names = new ArrayList<>(Arrays.asList(list.split(":")));
        names.remove(0);

        Path filepath = Paths.get("files" + File.separator + login + ".zip");
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        URI uri = URI.create("jar:" + filepath.toUri());
        try (FileSystem zip = FileSystems.newFileSystem(uri, env);
             ZipOutputStream zos = new ZipOutputStream(array)) {
            for (CustomFile customFile : userService.getFilesByArray(login, names)) {
                Path pathInZipfile = zip.getPath("/" + customFile.getName());
                ZipEntry entry = new ZipEntry(customFile.getName());
                zos.putNextEntry(entry);
                zos.write(Files.readAllBytes(pathInZipfile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "attachment; filename=archive.zip");
        return new ResponseEntity<>(array.toByteArray(), headers, HttpStatus.OK);
    }

    private String getLogin() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }
}
