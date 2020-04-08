/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.lora.Game;

/**
 *
 * @author stepa
 */
public class FrLa extends Minigame implements GameMode{
    
    private final int id = GameModes.FRLA.ordinal();
    
    public FrLa(Game game){
        super(game);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void playCard(Card card) {
        if (checkRules(card)){
            super.playCard(card);
        }
    }

    @Override
    public boolean checkRules(Card card) {
        if (card == null){
            return false;
        }
        
        return super.checkRules(card.getSuit());
    }

    @Override
    public int getId() {
        return id;
    }
}
