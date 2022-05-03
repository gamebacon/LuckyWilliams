package net.gamebacon.demo.registration;

import net.gamebacon.demo.login_user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("loginUser", new LoginUser());
        return "register";
    }

    @PostMapping("/register/save")
    public String saveUser(LoginUser loginUser, RedirectAttributes redirectAttributes) {
        RegistrationRequest registrationRequest = new RegistrationRequest(loginUser.getUsername(), loginUser.getPassword(), loginUser.getEmail());
        registrationService.register(registrationRequest);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

}
