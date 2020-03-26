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
    private boolean isPlaying = false;
    private HandView handView;
    protected Game game;
    
    public Player(Game game, int id) {
        this.game = game;
        this.id = id;
    }
    
    public void play(){
        isPlaying = true;
    }
    
    public boolean playCard(Card card){
        if (card == null){
            isPlaying = false;
            game.playNext(card);
            return false;
        }
        
        if (game.checkRules(card) == true){
            hand.remove(card);
            game.playNext(card);
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
