/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.game_modes.GameModes;
import card_game.net.Message;
import java.util.List;
import java.util.Random;

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
    
    public MpBot(Main program) {
        super(program);
        memory = new Deck[NUM_OF_PLAYERS];
        setMemory();
    }
    
    private void setMemory(){
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            memory[i] = new Deck(8);
        }
    }
    
    @Override
    public void action(byte[] data){
        Message msg = Message.values()[data[0]];
        
        switch(msg){
            case START:
                brain = new Brain();
                Thread t = new Thread(brain);
                t.start();
                break;
                
            case EXIT:
                
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
                
            case END_OF_ROUND:
                //TBA
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
                break;
        }
    }
    
    private interface Tactic {
        
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
            
            while(true){
                if (getHand().size() == 0 || !isPlaying()){
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
                        playCard(card);
                    } else {
                        pass();
                    }
                }
            }
        }
    }
}