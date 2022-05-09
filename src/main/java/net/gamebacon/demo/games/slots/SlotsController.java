package net.gamebacon.demo.games.slots;

import net.gamebacon.demo.games.util.WithDrawResponse;
import net.gamebacon.demo.login_user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SlotsController {

    @Autowired
    SlotsService slotsService;

    @GetMapping("/games/slots")
    public String showSlots(Model model) {

        //get from db instead.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = ((LoginUser) auth.getPrincipal());

        if(!model.containsAttribute("result"))
            model.addAttribute("result", new WithDrawResponse(false, user.getBalance()));

        return "games/slots";
    }


    @PostMapping(value = "/games/slots/spin", produces = "application/json")
    @ResponseBody
    public SlotsSessionResults spin(RedirectAttributes re, @RequestParam int bet) {

        SlotsSessionResults result = slotsService.spin(bet);

        re.addFlashAttribute("result", result.getWithdrawResult());

        return result;

    }






}
