package net.gamebacon.luckywilliams.password_reset;


import net.gamebacon.luckywilliams.login_user.LoginUser;
import net.gamebacon.luckywilliams.registration.RegistrationRequest;
import net.gamebacon.luckywilliams.registration.exception.PasswordsNotMatchingException;
import net.gamebacon.luckywilliams.registration.token.BadTokenException;
import net.gamebacon.luckywilliams.registration.token.ConfirmationTokenResponse;
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

        if(!model.containsAttribute("user")) {
            model.addAttribute("init", true);
            model.addAttribute("user", new LoginUser());
        }

        return "account/forgot_password";
    }

    @PostMapping("/forgot-password/send")
    public String sendForgotPassword(@ModelAttribute LoginUser user, RedirectAttributes redirectAttributes) {

        String email = user.getEmail();
        service.sendResetRequest(email);
        redirectAttributes.addFlashAttribute("email", email);

        return "redirect:/forgot-password";
    }

    @GetMapping("/forgot-password/reset")
    public String forgotPasswordReset(@RequestParam(value = "", required = true) String token, RedirectAttributes re) {

        ConfirmationTokenResponse response = service.confirmToken(token);

        if(response == ConfirmationTokenResponse.SUCCESS) {
            re.addFlashAttribute("token", token);
            return "redirect:/forgot-password-reset";
        } else {
            re.addFlashAttribute("error", response);
        }

        return "redirect:/forgot-password";
    }


    @GetMapping("/forgot-password-reset")
    public String resetPassword(Model model, RedirectAttributes redirectAttributes) {

        if(!model.containsAttribute("userRequest")) {
            RegistrationRequest request = new RegistrationRequest();
            request.setFirstname( (String) model.getAttribute("token"));
            model.addAttribute("userRequest", request);
        }

        return "account/forgot_password_reset";
    }

    @PostMapping("/forgot-password-reset/new")
    public String actuallyResetPassword(@ModelAttribute RegistrationRequest userRequest, RedirectAttributes re) {

        try {
            service.requestNewPassword(userRequest);
        } catch (PasswordsNotMatchingException e) {

            re.addFlashAttribute("error", true);
            re.addFlashAttribute("userRequest", userRequest);

            return "redirect:/forgot-password-reset";
        } catch (BadTokenException e) {
            re.addFlashAttribute("error", "BAD_TOKEN");
            return "redirect:/forgot-password";
        }

        re.addFlashAttribute("passwordChange", true);

        return "redirect:/login";
    }



}
