/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;

/**
 *
 * @author stepa
 */
public class Player {

    private final Deck hand = new Deck(8);
    private final int id;
    private final Game game;
    private HandView handView;
    private boolean isPlaying = false;
    
    public Player(Game game, int id) {
        this.game = game;
        this.id = id;      
    }
    
    public void play(){
        isPlaying = true;
    }
    
    public void stopPlaying(){
        isPlaying = false;
    }
    
    public boolean playCard(Card card){
        if (game.checkRules(card)){
            hand.remove(card);
            game.playCard(card);
            return true;
        }
        
        return false;
    }
    
    public Deck getHand(){
        return hand;
    }
    
    public HandView getHandView(){
        return handView;
    }
    
    public void setHandView(HandView handView){
        this.handView = handView;
    }
    
    public int getId(){
        return id;
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
}
