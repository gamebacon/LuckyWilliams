package net.gamebacon.demo.games.slots;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SlotsSessionResults {
    private float winAmount;
    private int[] wheels;
}