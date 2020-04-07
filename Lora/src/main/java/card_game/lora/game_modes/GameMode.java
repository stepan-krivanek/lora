/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;

/**
 *
 * @author stepa
 */
public interface GameMode {
    
    public void start();
    
    public void playCard(Card card);
    
    public boolean checkRules(Card card);
    
    public int getId();
}
