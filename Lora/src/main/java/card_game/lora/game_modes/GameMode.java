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
    
    public void play();
    
    public boolean playCard(Card card);
    
    public int getId();
}
