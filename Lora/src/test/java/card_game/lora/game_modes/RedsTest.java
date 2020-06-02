/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Suit;
import card_game.net.Server;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for game mode Reds.
 * Tests all cards that can not be played,
 * including reds and different suits;
 * cards that can be played;
 * players who can not play;
 * next player to play
 * 
 * @author Štěpán Křivánek
 */
public class RedsTest {
    
    private final int NUM_OF_PLAYERS = 4;
    private final int[] score = {0,0,0,0};
    private final int gameMode = GameMode.REDS.ordinal();
    private final int round = 0;
    private final boolean singleGame = true;
    private final TestBot[] testBots = new TestBot[NUM_OF_PLAYERS];
    private final Deck allCards = new Deck(true);
    private final Card[] lastPlayedCards = new Card[NUM_OF_PLAYERS];
    
    private Suit leadSuit;
    
    /**
     * Creates a new Reds test.
     */
    public RedsTest() {
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
     * Test of checkRules method, of class Reds.
     */
    @Test
    public void testCheckRules() {
        System.out.println("Testing check rules of Reds");
        
        setUp();
        int playerId = 0;
        while(!testBots[0].getHand().isEmpty()){
            Deck hand = testBots[playerId].getHand();

            for (Card card : allCards){
                if (!hand.contains(card)){
                    assertEquals(false, playCard(card), "Tests correct player to play");
                }
            }

            if (!hasOnlyReds(hand)){
                for (Card card : hand){
                    if (card.getSuit().equals(Suit.HEART)){
                        assertEquals(false, playCard(card),
                                "Reds must be played last. " + printCardsPlayed()
                        );
                    }
                }
            }

            Card card = playAnyCard(playerId);
            leadSuit = card.getSuit();
            testSuit(playerId, leadSuit);
            playerId = getNextPlayer();
        }
        
        tearDown();
    }
    
    private int getNextPlayer(){
        int max = -1;
        int playerId = -1;
        
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            Card card = lastPlayedCards[i];
            if(card.getSuit() == leadSuit && card.getRank().ordinal() > max){
                max = card.getRank().ordinal();
                playerId = i;
            }
        }
        
        return playerId;
    }
    
    private void testSuit(int playerId, Suit suit){
        for (int i = 1; i < NUM_OF_PLAYERS; i++){
            int index = (playerId + i) % NUM_OF_PLAYERS;
            Deck hand = testBots[index].getHand();
            
            if (hand.contains(suit)){
                for (Card card : hand){
                    if (!card.getSuit().equals(suit)){
                        assertEquals(false, playCard(card), "Testing suit");
                    }
                }
            }
            
            playAnyCard(index);
        }
    }
    
    private Card playAnyCard(int playerId){
        Deck hand = testBots[playerId].getHand();
        
        int idx = 0; 
        Card card = hand.get(idx++);
        while(!playCard(card)){
            card = hand.get(idx++);
        }

        lastPlayedCards[playerId] = card;
        return card;
    }
    
    private String printCardsPlayed(){
        String s = "Cards played: ";
        for (Card card : testBots[0].getCardsPlayed()){
            s += card.toString() + ", ";
        }
        return s;
    }
    
    private boolean hasOnlyReds(Deck deck){
        boolean ret = true;
        
        for (Card card : deck){
            if (!card.getSuit().equals(Suit.HEART)){
                ret = false;
                break;
            }
        }
        
        return ret;
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
