/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.Game;
import card_game.lora.Player;

/**
 *
 * @author stepa
 */
public class Reds implements GameMode{

    private final int DECK_SIZE = 32;
    private final int id = GameModes.REDS.ordinal();
    private final Deck cardsPlayed = new Deck(DECK_SIZE);
    private final Game game;
    private Player player;
    
    public Reds(Game game){
        this.game = game;
    }
    
    @Override
    public void start() {
        
    }

    @Override
    public void playCard(Card card) {
        
    }

    @Override
    public boolean checkRules(Card card) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getId() {
        return id;
    }
    
}
