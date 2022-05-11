package net.gamebacon.luckywilliams.registration;

import net.gamebacon.luckywilliams.registration.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String saveUser(@ModelAttribute  RegistrationRequest userRequest, RedirectAttributes re) {


        try {
            registrationService.register(userRequest);
            re.addFlashAttribute("new_member", userRequest);
            return "redirect:/login";
        } catch (NotEligibleException notEligibleException) {
            re.addFlashAttribute("error", "You must confirm that you're 18 years old or older.");
        } catch (NotAgreedToTermsAndConditions notAgreedToTermsAndConditions) {
            re.addFlashAttribute("error", "You must agree with our terms and conditions in order to proceed.");
        } catch (PasswordsNotMatchingException e) {
            re.addFlashAttribute("error", "The passwords do not match.");
        } catch (InvalidUsernameException e) {
            userRequest.setEmail("");
            re.addFlashAttribute("error", "The email is invalid");
        } catch (UsernameTakenException e) {
            userRequest.setEmail("");
            re.addFlashAttribute("error", "The email is already being used.");
        }

        //add present fields before redirect
        re.addFlashAttribute("loginUser", userRequest);

        return "redirect:/register";
    }




}
