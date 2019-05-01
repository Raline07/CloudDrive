package com.drive.Controllers;

import com.drive.Exceptions.DataErrorException;
import com.drive.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.drive.Entity.CustomUser;
import com.drive.UserRole;

import java.io.File;
import java.util.Collection;

@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }

    @PostMapping("/newuser")
    public String update(@RequestParam String login, @RequestParam String password) {
        String passHash = passwordEncoder.encodePassword(password, null);

        if ("".equals(login) ||
                !userService.addUser(login, passHash, UserRole.USER)) {
            throw new DataErrorException();
        }

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(name = "toDelete[]", required = false) long[] ids, Model model) {
        if (ids != null && ids.length > 0) {
            userService.deleteUsers(ids);
            for (CustomUser customUser : userService.findByIds(ids)) {
                new File("files" + File.separator + customUser.getLogin() + ".zip").delete();
            }
        }
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "/pages/login.html";
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }
}
