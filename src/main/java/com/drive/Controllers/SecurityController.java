package com.drive.Controllers;

import com.drive.Exceptions.DataErrorException;
import com.drive.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.drive.UserRole;


@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    @PostMapping("/newuser")
    public String update(@RequestParam String login, @RequestParam String password) {
        String passHash = passwordEncoder.encodePassword(password, null);

        if ("".equals(login) ||
                !userService.addUser(login, passHash, UserRole.USER)) {
            throw new DataErrorException();
        }

        return "redirect:/";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }
}
