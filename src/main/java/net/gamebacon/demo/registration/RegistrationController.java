package net.gamebacon.demo.registration;

import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.registration.exception.*;
import net.gamebacon.demo.registration.token.ConfirmationToken;
import net.gamebacon.demo.registration.token.ConfirmationTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final String REGISTER_PATH = "/account/register";
    private final String LOGIN_PATH = "/account/login";

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/register") //The url request (antMatcher input in security-config)
    public String showRegister(Model model) {

        //dont override flash fields
        if(!model.containsAttribute("loginUser"))
            model.addAttribute("loginUser", new RegistrationRequest());

        return "account/register"; //the relative file location (Not the case with "redirect:/"
    }
    //redirect is needed to load the page with data
    //cant add data to model attribute? but redirectAttribute.addFlashattribute works=??

    @PostMapping("/register/save")
    public String saveUser(@ModelAttribute  RegistrationRequest loginUser, RedirectAttributes re) {


        try {
            registrationService.register(loginUser);

            //re.addFlashAttribute("confirmEmail", loginUser.getEmail());
            re.addFlashAttribute("success", true);
            return "redirect:/login";
        } catch (NotEligibleException notEligibleException) {
            re.addFlashAttribute("error", "You must confirm that you're 18 years old or older.");
        } catch (NotAgreedToTermsAndConditions notAgreedToTermsAndConditions) {
            re.addFlashAttribute("error", "You must agree with our terms and conditions in order to proceed.");
        } catch (PasswordsNotMatchingException e) {
            re.addFlashAttribute("error", "The passwords do not match.");
        } catch (InvalidUsernameException e) {
            loginUser.setEmail("");
            re.addFlashAttribute("error", "The email is invalid");
        } catch (UsernameTakenException e) {
            loginUser.setEmail("");
            re.addFlashAttribute("error", "The email is already being used.");
        }
        //add present fields before redirect
        re.addFlashAttribute("loginUser", loginUser);

        return "redirect:/register";
    }




    @GetMapping("/login")
    public String login(@RequestParam(name = "error", defaultValue = "false") boolean isError, Model model) {
        model.addAttribute("error", isError);
        return "account/login";
    }

}
