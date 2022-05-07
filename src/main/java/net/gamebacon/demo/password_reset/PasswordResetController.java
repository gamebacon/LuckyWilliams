package net.gamebacon.demo.password_reset;


import net.gamebacon.demo.login_user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {


    @Autowired
    private PasswordResetService service;


    @GetMapping("/forgot-password")
    public String showForgotPassword(Model model) {

        if(!model.containsAttribute("user"))
            model.addAttribute("user", new LoginUser());

        return "account/forgot_password";
    }

    @PostMapping("/forgot-password/send")
    public String sendForgotPassword(@ModelAttribute LoginUser user, RedirectAttributes redirectAttributes) {

        String email = user.getEmail();

        service.sendMail(email);
        redirectAttributes.addFlashAttribute("response", email);

        return "redirect:/forgot-password";
    }

}
