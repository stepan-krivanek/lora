/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;

/**
 * Player to play in the Game
 * 
 * @author Štěpán Křivánek
 * @see card_game.lora.Game
 */
public class Player {

    private final Deck hand = new Deck(8);
    private final Deck table = new Deck(32);
    private final Game game;
    private final int id;
    private boolean isPlaying = false;
    
    /**
     * Creates a new player for a game with specified id.
     * 
     * @param game Game to play in
     * @param id Id of the player
     */
    public Player(Game game, int id){
        this.id = id;
        this.game = game;
    }
    
    /**
     * Calls server to send a message to an MpPlayer,
     * that the MpPlayer can play.
     */
    public void play(){
        isPlaying = true;
        game.getServer().play(id);
    }
    
    /**
     * Calls server to send a message to an MpPlayer,
     * that the MpPlayer must stop playing.
     */
    public void stopPlaying(){
        isPlaying = false;
        game.getServer().stopPlaying(id);
    }
    
    /**
     * Removes a card from player's hand
     * 
     * @param card
     */
    public void playCard(Card card){
        if (card != null){
            hand.remove(card);
        }
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
