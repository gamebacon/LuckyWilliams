package net.gamebacon.luckywilliams.games.roulette;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RouletteController {

    @Autowired
    private RouletteService rouletteService;

    @GetMapping("/games/roulette")
    public String roulette() {
        return "games/roulette";
    }


    @PostMapping(value = "/games/roulette/start", produces = "application/json")
    @ResponseBody
    public RouletteSessionResult startRoulette(RedirectAttributes re, @RequestBody RouletteSessionResult ingoingResult) {
        RouletteSessionResult outgoingResult = rouletteService.startRoulette(ingoingResult.getBets());
        re.addFlashAttribute("result", outgoingResult.getWithdrawResult());
        return outgoingResult;

    }

}
