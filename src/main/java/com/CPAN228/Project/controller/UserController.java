package com.CPAN228.Project.controller;

import com.CPAN228.Project.data.UserRepository;
import com.CPAN228.Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String listUsers(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, 10));
        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "userList";
    }

}