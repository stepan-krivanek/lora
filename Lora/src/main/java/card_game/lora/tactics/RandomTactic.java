/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.tactics;

import card_game.card.Card;
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

    @Override
    public Card playTens() {
        if (r.nextInt(4) == 0){
            return null;
        }

        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }

    @Override
    public Card playQuarts() {
        return bot.getHand().get(r.nextInt(bot.getHand().size()));
    }
    
}
