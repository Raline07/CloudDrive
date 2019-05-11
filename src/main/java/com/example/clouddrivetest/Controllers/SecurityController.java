package com.example.clouddrivetest.Controllers;

import com.example.clouddrivetest.Exceptions.DataErrorException;
import com.example.clouddrivetest.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.example.clouddrivetest.UserRole;


@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @PostMapping("/add-user")
    public ResponseEntity<Void> update(@RequestParam String login, @RequestParam String password) {
        String passHash = passwordEncoder.encode(password);
        if ("".equals(login) || !userService.addUser(login, passHash, UserRole.USER)) {
            throw new DataErrorException();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }
}
