/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Rank;
import card_game.card.Suit;
import card_game.lora.Game;

/**
 *
 * @author stepa
 */
public class RedKing extends Reds implements GameMode{
    
    private final int id = GameModes.RED_KING.ordinal();
    private int cardsPlayed = 0;
    
    public RedKing(Game game){
        super(game);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void playCard(Card card, int playerId) {
        if (checkRules(card)){
            cardsPlayed += 1;
            super.playCard(card);
        }
    }

    @Override
    public boolean checkRules(Card card) {
        if (card == null){
            return false;
        }
        
        if (cardsPlayed < 4 && 
            card.getRank() == Rank.KING &&
            card.getSuit() == Suit.HEART
        ){    
            return false;
        }
        
        return super.checkRules(card);
    }

    @Override
    public int getId() {
        return id;
    }
}
