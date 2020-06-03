/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.card.Suit;
import card_game.net.Server;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for game mode Tens.
 * Tests all cards that can not be played;
 * all cards that can be played;
 * players that can not play;
 * player that can play;
 * players that can not pass;
 * player that can pass
 * 
 * @author Štěpán Křivánek
 */
public class TensTest {
    
    private final int NUM_OF_PLAYERS = 4;
    private final int[] score = {0,0,0,0};
    private final int gameMode = GameMode.TENS.ordinal();
    private final int round = 0;
    private final boolean singleGame = true;
    private final TestBot[] testBots = new TestBot[NUM_OF_PLAYERS];
    
    /**
     * Creates a new Tens test.
     */
    public TensTest() {
    }
    
    /**
     * Sets up a server with a game and 4 test bots to play the game.
     */
    @BeforeEach
    public void setUp() {
        Server server = new Server(score, gameMode, round, singleGame);
        server.setWaitTime(0);
        
        Thread t = new Thread(server);
        t.setName("Thread: server");
        t.start();
        
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            testBots[i] = new TestBot("Bot" + i);
            testBots[i].connectToServer();
        }
        
        boolean ready = false;
        while(!ready){
            ready = true;
            for (TestBot bot : testBots){
                if (!bot.isReady()){
                    ready = false;
                    break;
                }
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(QuartsTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Diconnects all bots from the game, which ends tha game and server too.
     */
    @AfterEach
    public void tearDown() {
        for (TestBot testBot : testBots){
            testBot.disconnect();
        }
    }

    /**
     * Test of checkRules method, of class Tens.
     *//*
    @Test
    public void testCheckRules() {
        System.out.println("Testing check rules of Tens");
        
        setUp();
        int playerId = 0;
        while(true){
            Deck playableCards = getPlayableCards(playerId);

            for (Card card : testBots[playerId].getHand()){
                if (!playableCards.contains(card)){
                    assertEquals(false, playCard(card),
                        "Played card " + card.toString() + " " + printCardsPlayed()
                    );
                }
            }

            for (Card card : playableCards){
                assertEquals(true, playCard(card), "Testing playable cards");
            }
            
            if (isWinner()){
                break;
            }
            
            testNextPlayer(playerId);
            playerId = (playerId + 1) % NUM_OF_PLAYERS;
        }
        tearDown();
    }
    */
    private String printCardsPlayed(){
        String s = "Cards played: ";
        for (Card card : testBots[0].getCardsPlayed()){
            s += card.toString() + ", ";
        }
        return s;
    }
    
    private void testNextPlayer(int playerId){
        for (TestBot bot : testBots){
            if (bot.getId() != playerId){
                for (Card card : bot.getHand()){
                    assertEquals(false, playCard(card), "Testing players that can not play");
                }
                assertEquals(false, sendCard(null, bot.getId()), "Testing players that can not pass");
            }
        }
        
        assertEquals(true, sendCard(null, playerId), "Testing pass");
    }
    
    private boolean isWinner(){
        boolean ret = false;
        
        for (TestBot bot : testBots){
            if (bot.getHand().isEmpty()){
                ret = true;
                break;
            }
        }
        
        return ret;
    }
    
    private Deck getPlayableCards(int playerId){
        Deck playedCards = testBots[0].getCardsPlayed();
        Deck hand = testBots[playerId].getHand();
        Deck playableCards = new Deck(32);
        
        for (Suit suit : Suit.values()){
            playableCards.add(new Card(suit, Rank.TEN));
        }
        
        for (Card card : playedCards){
            int rank = card.getRank().ordinal();
            
            if (rank > Rank.TEN.ordinal()){
                if (rank != Rank.ACE.ordinal()){
                    playableCards.add(new Card(
                            card.getSuit(), Rank.values()[rank + 1]
                    ));
                }
            } else if (rank < Rank.TEN.ordinal()){
                if (rank != Rank.SEVEN.ordinal()){
                    playableCards.add(new Card(
                            card.getSuit(), Rank.values()[rank - 1]
                    ));
                }
            } else {
                playableCards.add(new Card(card.getSuit(), Rank.NINE));
                playableCards.add(new Card(card.getSuit(), Rank.INFERIOR));
            }
        }
        
        Deck ret = new Deck(8);
        for (Card card : playableCards){
            if (hand.contains(card)){
                ret.add(card);
            }
        }
        
        return ret;
    }
    
    private boolean playCard(Card card){
        int botId = -1;
        for (TestBot bot : testBots){
            if (bot.getHand().contains(card)){
                botId = bot.getId();
                break;
            }
        }
        
        if (botId == -1){
            return  false;
        }
        
        return sendCard(card, botId);
    }
    
    private boolean sendCard(Card card, int playerId){
        if (card == null){
            testBots[playerId].pass();
        } else {
            testBots[playerId].playCard(card);
        }
        
        while (testBots[playerId].isAwaitingResponse()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(QuartsTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return testBots[playerId].getResponse() == 1;
    }
}
