package net.gamebacon.demo;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


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

    @GetMapping("")
    public String showPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = !(authentication instanceof AnonymousAuthenticationToken);

        if(authenticated) {
            model.addAttribute("user", authentication.getPrincipal());
        } else {
            model.addAttribute("user", "Guest");
        }

        return "index";
    }
}
