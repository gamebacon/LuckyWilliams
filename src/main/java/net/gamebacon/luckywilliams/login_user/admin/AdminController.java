package net.gamebacon.luckywilliams.login_user.admin;

import net.gamebacon.luckywilliams.login_user.LoginUser;
import net.gamebacon.luckywilliams.login_user.LoginUserService;
import net.gamebacon.luckywilliams.registration.exception.UsernameTakenException;
import net.gamebacon.luckywilliams.login_user.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    LoginUserService loginUserService;


    @GetMapping("/admin/users")
    public String showUserList(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<LoginUser> users = loginUserService.getUsers();

        //avoid nasty bug
        LoginUser me =  ((LoginUser) auth.getPrincipal());
        users.remove(me);

        model.addAttribute("listUsers", users);
        return "admin/users";

    }

    @GetMapping("/todo")
    public String showTodoList(Model model) {
        return "admin/todo";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        try {
            loginUserService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User Deleted.");
        } catch (NoSuchUserException e) {
            redirectAttributes.addFlashAttribute("message", "No such user.");
        }

        return "redirect:/admin/users";
    }


    @GetMapping("admin/users/new")
    public String createNewUser(Model model) {
        model.addAttribute("user", new LoginUser());
        model.addAttribute("title", "New user");
        return "admin/new_user";
    }


    @PostMapping("admin/users/save")
    public String newUser(LoginUser user, RedirectAttributes redirectAttributes) {

        try {
            loginUserService.addUser(user);
            redirectAttributes.addFlashAttribute("message", "User saved.");
        } catch (UsernameTakenException e) {
            redirectAttributes.addFlashAttribute("message", "Username taken.");
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            LoginUser user = loginUserService.getUser(id);
            model.addAttribute("user", user);
            model.addAttribute("title", "Edit user");
            return "admin/new_user";
        } catch (NoSuchUserException e) {
            redirectAttributes.addFlashAttribute("messgae", "No such user.");
        }

        return "redirect:/admin/users";
    }

}
