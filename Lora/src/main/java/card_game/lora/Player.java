/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.net.ClientConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stepa
 */
public class Player {

    private final Deck hand = new Deck(8);
    private final Deck table = new Deck(32);
    private Game game;
    private int id;
    private HandView handView;
    private boolean isPlaying = false;
    
    public Player(Game game){
        this.game = game;
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
    
    public Deck getTable(){
        return table;
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
    
    public void setId(int id){
        this.id = id;
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
}
