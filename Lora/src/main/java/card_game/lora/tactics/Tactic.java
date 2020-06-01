/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.tactics;

import card_game.card.Card;

/**
 *
 * @author stepa
 */
public interface Tactic {
    
    public String getName();
        
    public Card playReds();

    public Card playSuperiors();

    public Card playFrla();

    public Card playAll();

    public Card playRedking();

    public Card playTens();

    public Card playQuarts();
    
}
