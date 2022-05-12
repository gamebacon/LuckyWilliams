package net.gamebacon.luckywilliams.games.videopoker;

import net.gamebacon.luckywilliams.games.util.WithdrawResult;
import net.gamebacon.luckywilliams.games.videopoker.util.Card;
import net.gamebacon.luckywilliams.login_user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VideopokerController {

    @Autowired
    private VideopokerService videopokerService;


    @GetMapping("/games/videopoker")
    public String showVideoPoker(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = ((LoginUser) auth.getPrincipal());

        if(!model.containsAttribute("balanceLeft"))
            model.addAttribute("balanceLeft", videopokerService.getBalance(user.getId()));

        return "/games/videopoker";
    }



    @PostMapping(value = "/games/videopoker/draw", produces = "application/json")
    @ResponseBody
    public VideoPokerSession draw(RedirectAttributes re, @RequestBody() VideoPokerSession inputSession) {


        VideoPokerSession returnSession = videopokerService.validateSession(inputSession);


        //System.out.println(returnSession);

        //re.addFlashAttribute("balanceLeft", returnSession.getWithdrawResult().getBalanceLeft());
        //re.addFlashAttribute("session", returnSession);

        return returnSession;

    }



}
