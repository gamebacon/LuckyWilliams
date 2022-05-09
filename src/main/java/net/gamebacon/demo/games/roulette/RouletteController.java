package net.gamebacon.demo.games.roulette;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouletteController {


    @GetMapping("/games/roulette")
    public String roulette() {
        return "games/roulette";
    }

}
