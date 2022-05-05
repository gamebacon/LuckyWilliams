package net.gamebacon.demo.login_user.admin;

import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.registration.RegistrationRequest;
import net.gamebacon.demo.registration.exception.UsernameTakenException;
import net.gamebacon.demo.user.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/users")
    public String showUserList(Model model) {
        List<LoginUser> users = loginUserService.getUsers();
        model.addAttribute("listUsers", users);
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        try {
            loginUserService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User Deleted.");
        } catch (NoSuchUserException e) {
            redirectAttributes.addFlashAttribute("message", "No such user.");
        }

        return "redirect:/users";
    }


    @GetMapping("users/new")
    public String createNewUser(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        model.addAttribute("title", "New user");
        return "admin/new_user";
    }


    @PostMapping("/users/save")
    public String newUser(RegistrationRequest user, RedirectAttributes redirectAttributes) {

        try {
            loginUserService.addUser(user);
            redirectAttributes.addFlashAttribute("message", "User saved.");
        } catch (UsernameTakenException e) {
            redirectAttributes.addFlashAttribute("message", "Username taken.");
        }

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            LoginUser user = loginUserService.getUser(id);

            RegistrationRequest regRequest = new RegistrationRequest();
            regRequest.setEmail(user.getEmail());
            regRequest.setUsername(user.getUsername());
            regRequest.setPassword(user.getPassword());
            regRequest.setRepeatPassword(user.getPassword());
            regRequest.setGender(user.getGender());
            regRequest.setId(user.getId());
            regRequest.setEnabled(user.isEnabled());

            model.addAttribute("user", regRequest);
            model.addAttribute("title", "Edit user");
            return "admin/new_user";
        } catch (NoSuchUserException e) {
            redirectAttributes.addFlashAttribute("messgae", "No such user.");
        }

        return "redirect:/users";
    }

}
