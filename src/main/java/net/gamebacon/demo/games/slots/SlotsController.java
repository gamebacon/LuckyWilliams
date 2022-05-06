package net.gamebacon.demo.games.slots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
public class SlotsController {

    @Autowired
    SlotsService slotsService;

    @GetMapping("/games/slots")
    public String showSlots() {
        return "games/slots";
    }


    @GetMapping("/games/slots/spin")
    public String spin(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("wheel", Arrays.toString(slotsService.spin()));
        return "redirect:/games/slots";
    }






}
