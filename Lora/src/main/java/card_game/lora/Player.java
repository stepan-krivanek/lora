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

    private final int id;
    private final Deck hand;
    private boolean isPlaying = false;
    private HandView handView;
    private Game game;
    
    public Player(Game game, int id, int handSize) {
        this.game = game;
        this.id = id;
        hand = new Deck(handSize);
    }
    
    public void play(){
        isPlaying = true;
    }
    
    public void playCard(Card card){
        isPlaying = false;
        hand.remove(card);
        game.playNext(card);
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
