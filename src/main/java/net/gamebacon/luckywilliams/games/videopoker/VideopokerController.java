package net.gamebacon.luckywilliams.games.videopoker;

import net.gamebacon.luckywilliams.games.util.WithdrawResult;
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

        if(!model.containsAttribute("result"))
            model.addAttribute("result", new WithdrawResult(false, videopokerService.getBalance(user.getId())));

        return "/games/videopoker";
    }



    @PostMapping(value = "/games/videopoker/draw", produces = "application/json")
    @ResponseBody
    public VideoPokerSession draw(RedirectAttributes re, @RequestBody() VideoPokerSession session) {


        VideoPokerSession result = videopokerService.validateSession(session);

        if(result.getWithdrawResult() == null)
            result.setWithdrawResult(new WithdrawResult(true, -1));

        re.addFlashAttribute("result", result.getWithdrawResult());

        return result;

    }



}
