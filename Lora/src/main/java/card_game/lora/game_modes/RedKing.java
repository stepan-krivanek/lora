/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Rank;
import card_game.card.Suit;
import card_game.lora.Game;

/**
 * Game mode Red King in Lóra game.
 * Player who takes the trick with the red king
 * gets eight penalty points.
 * 
 * @author Štěpán Křivánek
 */
public class RedKing extends Reds implements GameModeInterface{
    
    private final int id = GameMode.RED_KING.ordinal();
    private final Card RED_KING = new Card(Suit.HEART, Rank.KING);
    private boolean redKingPlayed = false;
    private int redKingPlayerId;
    
    /**
     * Creates a new Red King game mode.
     * 
     * @param game Frame game of this mode
     */
    public RedKing(Game game){
        super(game);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void playCard(Card card, int playerId) {
        if (checkRules(card)){
            if (card.equals(RED_KING)){
                redKingPlayed = true;
                redKingPlayerId = playerId;
            }
            
            if (redKingPlayed && (cardsPlayed() % 4 == 0)){
                end();
            } else {
                super.playCard(card);
            }
        }
    }

    @Override
    public boolean checkRules(Card card) {
        if (card == null){
            return false;
        }
        
        if (cardsPlayed() < 4 && card.equals(RED_KING)){ 
            return false;
        }
        
        return super.checkRules(card);
    }
    
    @Override
    protected void end(){
        penalties[redKingPlayerId] = 8;
        super.end();
    }

    @Override
    public int getId() {
        return id;
    }
}
