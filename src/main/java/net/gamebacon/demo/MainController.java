package net.gamebacon.demo;

import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.registration.RegistrationRequest;
import net.gamebacon.demo.registration.token.ConfirmationTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {


    @Autowired
    MainService mainService;

    @GetMapping("/error")
    public String showError() {
        return "/error";
    }

    @GetMapping("/access-denied")
    public String showDenied() {
        return "/access_denied";
    }

    @GetMapping("/terms")
    public String showTermsAndConditions() {
        return "/terms";
    }

    @GetMapping("/verify")
    public String sendVerification(RedirectAttributes re, @RequestParam(required = false, defaultValue = "") String token) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) auth.getPrincipal();

        boolean verified = mainService.isVerified(loginUser.getId());

        if(verified)
            return "redirect:/error";

        if(!token.isEmpty()) {
            ConfirmationTokenResponse response = mainService.confirmToken(token);
            re.addFlashAttribute("verificationResponse", response.name());
        } else {
            mainService.sendVerificationEmail(loginUser);
            re.addFlashAttribute("verify", true);
        }

        return "redirect:/";
    }

    @GetMapping("")
    public String showPage(Model model) {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = !((auth) instanceof AnonymousAuthenticationToken);

        if(authenticated) {
            LoginUser loginUser = (LoginUser) auth.getPrincipal();
            model.addAttribute("user", loginUser);
            model.addAttribute("not_verified", !mainService.isVerified(loginUser.getId()));
        } else {
            model.addAttribute("user", "Guest");
        }

        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", defaultValue = "false") boolean isError, Model model) {

        model.addAttribute("error", isError);

        if(model.containsAttribute("new_member")) {
            mainService.authenticateUser((RegistrationRequest) model.getAttribute("new_member"));
            return "redirect:/";
        }

        return "account/login";
    }

    @Autowired
    private LoginUserService loginUserService;


    @GetMapping("/account")
    private String viewAccount(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginUser = ((LoginUser) auth.getPrincipal());
        model.addAttribute("user", loginUser);

        return "/account/account";
    }


}
