/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

/**
 * Enum of game modes in game Lóra.
 *  
 * @author Štěpán Křivánek
 */
public enum GameMode {

    REDS("Reds"), SUPERIORS("Superiors"), FRLA("First-Last"), ALL("All"),
    RED_KING("Red King"), TENS("Tens"), QUARTS("Quarts");
    
    private final String name;
    private GameMode(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
