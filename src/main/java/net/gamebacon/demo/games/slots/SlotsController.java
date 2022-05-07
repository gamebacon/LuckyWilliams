package net.gamebacon.demo.games.slots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
public class SlotsController {

    @Autowired
    SlotsService slotsService;

    @GetMapping("/games/slots")
    public String showSlots(Model model) {

        if(!model.containsAttribute("result"))
             model.addAttribute("result", new SlotsSessionResults(0, new int[]{-1, -1, -1}));

        return "games/slots";
    }


    @GetMapping("/games/slots/spin")
    public String spin(RedirectAttributes redirectAttributes) {

        //can afford?

        SlotsSessionResults result = slotsService.spin();

        redirectAttributes.addFlashAttribute("result", result);

        return "redirect:/games/slots";
    }






}
