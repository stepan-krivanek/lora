/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.tactics;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.lora.GameUtils;
import card_game.lora.MpBot;
import java.util.Random;

/**
 * Tactic for a Lóra bot
 * to play random cards.
 * 
 * @author Štěpán Křivánek
 */
public class RandomTactic implements TacticInterface{
    
    private final Random r = new Random();
    private final MpBot bot;    
    
    /**
     * Creates a new RandomTactic.
     * 
     * @param bot Bot which is about to use this tactic
     */
    public RandomTactic(MpBot bot){
        this.bot = bot;
    }

    @Override
    public String getName(){
        return "Random";
    }

    @Override
    public Card playReds(){
        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }

    @Override
    public Card playSuperiors() {
        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }

    @Override
    public Card playFrla() {
        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }

    @Override
    public Card playAll() {
        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }

    @Override
    public Card playRedking() {
        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }

    private int cardsPlayed = 0;
    
    @Override
    public Card playTens() {
        Deck playableCards = new Deck(bot.getHand().size());
        for (Card card : bot.getHand()){
            int value = GameUtils.getRankValue(card.getRank());
            int tenValue = GameUtils.getRankValue(Rank.TEN);
            
            if (value == tenValue){
                playableCards.add(card);
            } else if (value > tenValue){
                Card tmp = new Card(card.getSuit(), Rank.values()[value - 1]);
                
                if (cardWasPlayed(tmp)){
                    playableCards.add(card);
                }
            } else {
                Card tmp = new Card(card.getSuit(), Rank.values()[value + 1]);
                
                if (cardWasPlayed(tmp)){
                    playableCards.add(card);
                }
            }
        }
        
        if (playableCards.isEmpty()){
            return null;
        }
        
        if (cardsPlayed < 1){
            return playableCards.get(r.nextInt(playableCards.size()));
        }

        if (r.nextInt(3) == 0){
            return null;
        }
        
        return playableCards.get(r.nextInt(playableCards.size()));
    }

    @Override
    public Card playQuarts() {
        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }
    
    private boolean cardWasPlayed(Card card){
        boolean ret = false;
        
        for (Deck deck : bot.getMemory()){
            if (deck.contains(card)){
                ret = true;
                break;
            }
        }
        
        return ret;
    }
    
}
