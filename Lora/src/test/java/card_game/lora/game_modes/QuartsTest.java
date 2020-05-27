/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.lora.Game;
import card_game.net.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author stepa
 */
public class QuartsTest {
    
    private Game game;
    
    public QuartsTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        Server server = new Server();
        game = new Game(server);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of playCard method, of class Quarts.
     */
    @Test
    public void testPlayCard() {
        System.out.println("playCard");
        Card card = null;
        int playerId = 0;
        Quarts quarts = new Quarts(game);
        
        quarts.playCard(card, playerId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkRules method, of class Quarts.
     */
    @Test
    public void testCheckRules() {
        System.out.println("checkRules");
        Card card = null;
        Quarts instance = null;
        boolean expResult = false;
        boolean result = instance.checkRules(card);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
