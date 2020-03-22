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
public class Player {

    private final int ID;
    private Deck hand;
    
    public Player(int id, int handSize) {
        ID = id;
        hand = new Deck(handSize);
    }
    
    public void play(){
        
    }
    
    public Deck getHand(){
        return hand;
    }
    
    public int getId(){
        return ID;
    }
    
}
