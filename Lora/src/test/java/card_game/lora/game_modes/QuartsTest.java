/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.net.Server;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the game mode Quarts.
 * Tests all cards that can not be played
 * and then all that can be played.
 * 
 * @author Štěpán Křivánek
 */
public class QuartsTest {
    
    private final int NUM_OF_PLAYERS = 4;
    private final int[] score = {0,0,0,0};
    private final int gameMode = GameMode.QUARTS.ordinal();
    private final int round = 0;
    private final boolean singleGame = true;
    private final TestBot[] testBots = new TestBot[NUM_OF_PLAYERS];
    private final Deck allCards = new Deck(true);
    
    /**
     * Creates a new Quarts test.
     */
    public QuartsTest() {
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
     * Test of checkRules method, of class Quarts.
     *//*
    @Test
    public void testCheckRules() {
        System.out.println("Testing check rules of Quarts");
        
        Card card;
        boolean result;
        
        for (int i = 0; i < 5; i++){
            setUp();
            
            card = allCards.get(i % allCards.size());
            result = testBots[0].getHand().contains(card);
            assertEquals(result, playCard(card), "Card sample " + i);
            if (result){
                testFollowingCards(card);
            }
            
            tearDown();
        }
        
    }
    */
    private void testFollowingCards(Card card){
        Deck cardsToPlay = getCardsToPlay(card);
        
        // Tests all cards that can not be played
        for (int i = 0; i < allCards.size(); i++){
            Card tmp = allCards.get(i);
            if (!cardsToPlay.contains(tmp)){
                assertEquals(false, playCard(tmp), "Cards not to play");
            }
        }
        
        // Tests cards supposed to play
        for (Card tmp : cardsToPlay){
            assertEquals(true, playCard(tmp), "Cards to play");
        }
    }
    
    private Deck getCardsToPlay(Card card){
        Deck cardsToPlay = new Deck(3);
        for (int i = 1; i < 4; i++){
            int rankIndex = card.getRank().ordinal() + i;
            if (rankIndex >= Rank.values().length){
                break;
            }
            
            Card tmp = new Card(card.getSuit(), Rank.values()[rankIndex]);
            if (testBots[0].getCardsPlayed().contains(tmp)){
                break;
            }
            
            cardsToPlay.addTopCard(tmp);
        }
        
        return cardsToPlay;
    }
    
    private boolean playCard(Card card){
        int botId = -1;
        for (TestBot bot : testBots){
            if (bot.getHand().contains(card)){
                bot.playCard(card);
                botId = bot.getId();
                break;
            }
        }
        
        if (botId == -1){
            return  false;
        }
        
        while (testBots[botId].isAwaitingResponse()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(QuartsTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return testBots[botId].getResponse() == 1;
    }
    
}
