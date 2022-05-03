package net.gamebacon.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("listUsers", users);
        return "users";
    }

    @GetMapping("users/new")
    public String createNewUser(Model model) {
        model.addAttribute("user", new User());
        return "new_user";
    }


    @PostMapping("/users/save")
    public String newUser(User user, RedirectAttributes redirectAttributes) {
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "User saved.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(id);
            model.addAttribute("user", user);
            return "new_user";
        } catch (NoSuchUserException e) {
            redirectAttributes.addFlashAttribute("messgae", "No such user.");
        }

        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "User Deleted.");
        } catch (NoSuchUserException e) {
            redirectAttributes.addFlashAttribute("message", "No such user.");
        }
        return "redirect:/users";
    }

}
