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
    private final Deck table = new Deck(32);
    private final Game game;
    private final int id;
    private boolean isPlaying = false;
    
    public Player(Game game, int id){
        this.id = id;
        this.game = game;
    }
    
    public void play(){
        isPlaying = true;
        game.getServer().play(id);
    }
    
    public void stopPlaying(){
        isPlaying = false;
        game.getServer().stopPlaying(id);
    }
    
    public void playCard(Card card){
        hand.remove(card);
        if (hand.contains(card)){
            System.out.println("Remove failed");
        }
        game.playCard(card);
    }
    
    public Deck getTable(){
        return table;
    }
    
    public Deck getHand(){
        return hand;
    }
    
    public int getId(){
        return id;
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
}
