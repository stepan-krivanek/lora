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
public class All extends Minigame implements GameMode{
    
    private final int id = GameModes.ALL.ordinal();
    
    public All(Game game){
        super(game);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void playCard(Card card, int playerId) {
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
    public void end(){
        for (int id : getTrickTakers()){
            penalties[id] += 1;
        }
        
        super.end();
    }

    @Override
    public int getId() {
        return id;
    }
}
