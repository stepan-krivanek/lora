/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;

/**
 * Interface that must be implemented
 * in order to be included in Lóra game modes.
 * 
 * @author Štěpán Křivánek
 */
public interface GameModeInterface {
    
    /**
     * Starts the game mode.
     */
    public void start();
    
    /**
     * Plays a card specified by a player specified.
     * 
     * @param card Card to be played
     * @param playerId Id of the player, who played the card
     */
    public void playCard(Card card, int playerId);
 
    /**
     * Checks if a card specified can be played.
     * 
     * @param card Card to check rules for
     * @return True if card can be played, false otherwise
     */
    public boolean checkRules(Card card);
    
    /**
     * @return Id of the game mode
     */
    public int getId();
}
