/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.lora.Game;

/**
 * Game mode First-Last in Lóra game.
 * Player who takes the first trick gets
 * four penalty points. The same goes for
 * the player who takes the last trick.
 * 
 * @author Štěpán Křivánek
 */
public class FrLa extends Minigame implements GameModeInterface{
    
    private final int id = GameMode.FRLA.ordinal();
    
    /**
     * Creates a new First-Last game mode.
     * 
     * @param game Frame game of this mode
     */
    public FrLa(Game game){
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
        int[] trickTakers = getTrickTakers();
        penalties[trickTakers[0]] += 4;
        penalties[trickTakers[trickTakers.length - 1]] += 4;
        super.end();
    }
    
    @Override
    public int getId() {
        return id;
    }
}
