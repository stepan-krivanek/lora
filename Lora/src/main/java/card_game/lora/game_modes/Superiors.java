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
public class Superiors extends Minigame implements GameMode{
    
    private final int id = GameModes.SUPERIORS.ordinal();
    
    public Superiors(Game game){
        super(game);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void playCard(Card card, int playerId) {
        if (checkRules(card)){
            super.playCard(card);
        }
    }

    @Override
    public boolean checkRules(Card card) {
        if (card == null){
            return false;
        }
        
        return super.checkRules(card.getSuit());
    }

    @Override
    public void end(){
        for (int i = 0; i < penalties.length; i++){
            int superiors = 0;
            for (Card card : game.getPlayer(i).getTable()){
                if (card.getRank().equals(Rank.SUPERIOR)){
                    superiors += 1;
                }
            }
            
            penalties[i] = superiors;
        }
        
        super.end();
    }
    
    @Override
    public int getId() {
        return id;
    }
}
