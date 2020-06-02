/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.lora.Game;

/**
 * Game mode All in Lóra game.
 * Player gets one penalty point for each trick taken.
 * 
 * @author Štěpán Křivánek
 */
public class All extends Minigame implements GameModeInterface{
    
    private final int id = GameMode.ALL.ordinal();
    
    /**
     * Creates the game mode All.
     * 
     * @param game Frame game of this mode
     */
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
    protected void end(){
        for (int playerId : getTrickTakers()){
            penalties[playerId] += 1;
        }
        
        super.end();
    }


    @Override
    public int getId() {
        return id;
    }
}
