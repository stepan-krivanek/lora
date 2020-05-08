/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Deck;

/**
 *
 * @author stepa
 */
public class PrimaryHandView extends HandView{
    
    private final double CARD_WIDTH;
    private final double CARD_HEIGHT;
    
    public PrimaryHandView(MpPlayer player, double width, double height){
        super(player, width, height);
        CARD_WIDTH = super.getCardWidth();
        CARD_HEIGHT = super.getCardHeight();
        
        
    }
    
}
