package net.gamebacon.demo.login_user;


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
public class LoginUserController {


    @Autowired
    private LoginUserService loginUserService;


    @GetMapping("/account")
    private String viewAccount() {
        return "/account/account";
    }


}
