package net.gamebacon.demo.games.videopoker;

import net.gamebacon.demo.games.slots.SlotsSessionResults;
import net.gamebacon.demo.games.videopoker.util.VideopokerSessionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VideopokerController {

    @Autowired
    private VideopokerService videopokerService;


    @GetMapping("/games/videopoker")
    public String showVideoPoker() {
        return "/games/videopoker";
    }



    @PostMapping(value = "/games/videopoker/start", produces = "application/json")
    @ResponseBody
    public VideopokerSessionResults spin(RedirectAttributes re, @RequestParam int bet) {

        VideopokerSessionResults result = videopokerService.newGame(bet);
        re.addFlashAttribute("result", result.getWithdrawResult());

        return result;

    }



}
