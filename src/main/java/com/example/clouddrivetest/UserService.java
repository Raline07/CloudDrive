package com.example.clouddrivetest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.clouddrivetest.Entity.CustomFile;
import com.example.clouddrivetest.Entity.CustomUser;
import com.example.clouddrivetest.Repositories.FileRepository;
import com.example.clouddrivetest.Repositories.UserRepository;
import com.example.clouddrivetest.DTO.FileDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Transactional(readOnly = true)
    public List<CustomUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CustomUser findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public List<CustomUser> findByIds(long[] ids) {
        List<CustomUser> list = new ArrayList<>();
        for (long id : ids) {
            list.add(userRepository.getOne(id));
        }
        return list;
    }

    @Transactional
    public void deleteUsers(long[] ids) {
        if (ids == null) return;
        for (long id : ids) {
            userRepository.deleteById(id);
        }
    }

    @Transactional
    public boolean addUser(String login, String passHash, UserRole role) {
        if (userRepository.existsByLogin(login))
            return false;

        CustomUser user = new CustomUser(login, passHash, role);
        userRepository.save(user);

        return true;
    }

    @Transactional
    public void addFiles(String login, List<CustomFile> customFiles) {
        CustomUser user = userRepository.findByLogin(login);
        for (CustomFile customFile : customFiles) {
            customFile.setUser(user);
            try {
                fileRepository.deleteByNameAndAccountLogin(customFile.getName(), login);
            } catch (Exception e) {
                //
            }
        }
        user.addFiles(customFiles);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<FileDTO> getFiles(String login, Pageable pageable) {
        List<FileDTO> result = new ArrayList<>();
        List<CustomFile> files = fileRepository.findByAccountLogin(login, pageable);

        files.forEach((x) -> {
            try {
                result.add(FileDTO.from(x));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    @Transactional(readOnly = true)
    public List<CustomFile> getFilesByArray(String login, List<String> names) {
        CustomUser user = userRepository.findByLogin(login);
        List<CustomFile> files = new ArrayList<>();
        for (String name : names) {
            files.add(fileRepository.findByNameAndAccountLogin(name, user.getLogin()));
        }
        return files;
    }

    @Transactional
    public void setUploading(String login, boolean b) {
        CustomUser user = userRepository.findByLogin(login);
        user.setUploading(b);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Long count(String login) {
        return fileRepository.countByAccountLogin(login);
    }

    @Transactional
    public List<String> deleteFiles(String login, String[] names) {
        CustomUser user = userRepository.findByLogin(login);
        List<String> path = new ArrayList<>();
        for (String name : names) {
            CustomFile cf = fileRepository.findByNameAndAccountLogin(name, user.getLogin());
            path.add(cf.getPath());
            fileRepository.deleteById(cf.getId());
        }
        userRepository.save(user);
        return path;
    }
}
