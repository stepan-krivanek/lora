/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.game_modes.GameModes;
import card_game.net.ServerMessage;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stepa
 */
public class MpBot extends MpPlayer{
    
    private final int NUM_OF_PLAYERS = 4;
    private final List<GameModes> gameModes = GameUtils.getOrderedGamemodes();
    private final Deck[] memory;
    
    private GameModes gameMode = GameModes.REDS;
    private Brain brain;
    private boolean awaitingResponse = false;
    private boolean exit = false;
    
    public MpBot(String nickname, Main program) {
        super(nickname, program);
        memory = new Deck[NUM_OF_PLAYERS];
        setMemory();
    }
    
    private void setMemory(){
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            memory[i] = new Deck(8);
        }
    }
    
    @Override
    public void setNames(String[] names){
        // No use for it yet
    }
    
    @Override
    public void action(byte[] data){
        ServerMessage msg = ServerMessage.values()[data[0]];
        
        switch(msg){
            case START:
                brain = new Brain();
                Thread t = new Thread(brain);
                t.setName("Thread: Bot " + getId());
                t.start();
                break;
                
            case EXIT:
                exit = true;
                disconnect();
                break;
                
            case CONNECTION_LOST:
                int pId = data[1];
                if (pId != getId()){
                    exit = true;
                    disconnect();
                }
                break;
                
            case SCORE:
                //TBA
                break;
                
            case PLAY:
                play();
                break;
                
            case STOP_PLAYING:
                stopPlaying();
                break;
                
            case CARD_PLAYED:
                Card card1 = new Card(data[1]);
                int playerId = data[2];
                
                memory[playerId].addTopCard(card1);
                break;
               
            case GAME_MODE:
                setMemory();
                gameMode = gameModes.get(data[1]);
                break;
                
            case GRADUATION:
                chooseGameMode(GameModes.QUARTS.ordinal());
                break;
                
            case HAND:
                getHand().clear();
                
                for (int i = 1; i <= HAND_SIZE; i++){
                    Card card2 = new Card(data[i]);
                    getHand().addTopCard(card2);
                }
                break;
                
            case PLAY_RESPONSE:
                Card card3 = new Card(data[1]);
                int index = getHand().indexOf(card3);
                
                if (index != -1 && data[2] == 1){
                    getHand().remove(index);
                }
                
                awaitingResponse = false;
                break;
        }
    }
    
    private interface Tactic {
        
        public String getName();
        
        public Card playReds();
        
        public Card playSuperiors();
        
        public Card playFrla();
        
        public Card playAll();
        
        public Card playRedking();
        
        public Card playTens();
        
        public Card playQuarts();
    }
    
    private class RandomTactic implements Tactic {
        
        private final Random r = new Random();
        
        public String getName(){
            return "Random";
        }
        
        public Card playReds(){
            return getHand().get(r.nextInt(getHand().size()));
        }

        @Override
        public Card playSuperiors() {
            return getHand().get(r.nextInt(getHand().size()));
        }

        @Override
        public Card playFrla() {
            return getHand().get(r.nextInt(getHand().size()));
        }

        @Override
        public Card playAll() {
            return getHand().get(r.nextInt(getHand().size()));
        }

        @Override
        public Card playRedking() {
            return getHand().get(r.nextInt(getHand().size()));
        }

        @Override
        public Card playTens() {
            if (r.nextInt(4) == 0){
                return null;
            }
            
            return getHand().get(r.nextInt(getHand().size()));
        }

        @Override
        public Card playQuarts() {
            return getHand().get(r.nextInt(getHand().size()));
        }
        
    }
    
    private class Brain implements Runnable{

        @Override
        public void run() {
            Tactic tactic = new RandomTactic();
            
            String msg = "Bot " + getId() + " joins the game.";
            Logger.getLogger(MpBot.class.getName()).log(Level.INFO, msg);
            
            while(!exit){
                if (!isPlaying() || awaitingResponse || getHand().isEmpty()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MpBot.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    continue;
                }
                
                Card card = null;
                switch(gameMode){
                    case REDS:
                        card = tactic.playReds();
                        break;
                        
                    case SUPERIORS:
                        card = tactic.playSuperiors();
                        break;
                    
                    case FRLA:
                        card = tactic.playFrla();
                        break;
                    
                    case ALL:
                        card = tactic.playAll();
                        break;
                    
                    case RED_KING:
                        card = tactic.playRedking();
                        break;
                    
                    case TENS:
                        card = tactic.playTens();
                        break;
                    
                    case QUARTS:
                        card = tactic.playQuarts();
                        break;
                }
                
                if(isPlaying()){
                    if (card != null){
                        awaitingResponse = true;
                        playCard(card);
                    } else {
                        pass();
                    }
                }
            }
            
            msg = "Bot " + getId() + " finished the game with " + tactic.getName() + " tactic.";
            Logger.getLogger(MpBot.class.getName()).log(Level.INFO, msg);
        }
    }
}