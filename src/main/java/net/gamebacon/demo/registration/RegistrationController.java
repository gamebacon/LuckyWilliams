package net.gamebacon.demo.registration;

import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.registration.token.ConfirmationTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/register") //The url request (antMatcher input in security-config)
    public String showRegister(Model model) {
        model.addAttribute("loginUser", new LoginUser());
        return "account/register"; //the relative file location
    }

    @GetMapping("/register/confirm")
    public String confirmRegistration(@RequestParam(value = "token", defaultValue = "") String token, RedirectAttributes re) {
        ConfirmationTokenResponse response = registrationService.confirmToken(token);

        System.out.println(String.format("Response name() ->%s<-", response.name()));
        re.addFlashAttribute("response", response.name());

        return "account/confirm_email";
    }

    @PostMapping("/register/save")
    public String saveUser(LoginUser loginUser, RedirectAttributes redirectAttributes) {
        RegistrationRequest registrationRequest = new RegistrationRequest(loginUser.getUsername(), loginUser.getPassword(), loginUser.getEmail());
        registrationService.register(registrationRequest);
        return "redirect:/";
    }


    /*
    @PostMapping("/login/save")
    public String loginUser(LoginUser loginUser, RedirectAttributes redirectAttributes) {
        System.out.println("Login: " + loginUser);
        return "redirect:/";
    }
     */

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginUser", new LoginUser());
        return "account/login";
    }

}
