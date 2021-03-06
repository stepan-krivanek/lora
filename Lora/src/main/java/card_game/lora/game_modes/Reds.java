/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Suit;
import card_game.lora.Game;

/**
 * Game mode Reds in Lóra game.
 * Player gets a penalty point for each
 * card of suit heart in a taken trick.
 * 
 * @author Štěpán Křivánek
 */
public class Reds extends Minigame implements GameModeInterface{

    private final int NUM_OF_PLAYERS;
    private final int id = GameMode.REDS.ordinal();
    
    /**
     * Creates a new Reds game mode.
     * 
     * @param game Frame game of this mode
     */
    public Reds(Game game){
        super(game);
        NUM_OF_PLAYERS = game.getNumOfPlayers();
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
        
        if (cardsPlayed() % NUM_OF_PLAYERS == 0){
            if (card.getSuit() == Suit.HEART){
                if (!hasOnlyReds()){
                    return false;
                }
            }
            return true;
        }
        
        return super.checkRules(card.getSuit());
    }
    
    @Override
    protected void end(){
        for (int i = 0; i < penalties.length; i++){
            int reds = 0;
            for (Card card : game.getPlayer(i).getTable()){
                if (card.getSuit().equals(Suit.HEART)){
                    reds += 1;
                }
            }
            
            penalties[i] = reds;
        }
        
        super.end();
    }
    
    private boolean hasOnlyReds(){
        boolean onlyReds = true;
        
        Deck hand = super.getCurrentPlayer().getHand();
        for (int i = 0; i < hand.size(); i++){
            if (hand.get(i).getSuit() != Suit.HEART){
                onlyReds = false;
                break;
            }
        }
        
        return onlyReds;
    }

    @Override
    public int getId() {
        return id;
    }  
}
