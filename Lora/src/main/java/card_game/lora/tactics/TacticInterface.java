/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.tactics;

import card_game.card.Card;

/**
 * Interface which must be implemented to be
 * a tactic for a bot player in Lóra.
 * 
 * @author Štěpán Křivánek
 */
public interface TacticInterface {
    
    /**
     * @return Name of the tactic
     */
    public String getName();
        
    /**
     * Method to play the game mode Reds. 
     * 
     * @return Card to play next
     */
    public Card playReds();

    /**
     * Method to play the game mode Superiors. 
     * 
     * @return Card to play next
     */
    public Card playSuperiors();

    /**
     * Method to play the game mode First-Last. 
     * 
     * @return Card to play next
     */
    public Card playFrla();

    /**
     * Method to play the game mode All. 
     * 
     * @return Card to play next
     */
    public Card playAll();

    /**
     * Method to play the game mode Red King. 
     * 
     * @return Card to play next
     */
    public Card playRedking();

    /**
     * Method to play the game mode Tens. 
     * 
     * @return Card to play next
     */
    public Card playTens();

    /**
     * Method to play the game mode Quarts. 
     * 
     * @return Card to play next
     */
    public Card playQuarts();
    
}
