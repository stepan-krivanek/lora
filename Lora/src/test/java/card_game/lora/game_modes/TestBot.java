/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.MpBot;
import card_game.net.ServerMessage;

/**
 *
 * @author stepa
 */
public class TestBot extends MpBot{
    
    private final int NUM_OF_PLAYERS = 4;
    private final int MAX_CARDS = 32;
    private final String[] names = new String[NUM_OF_PLAYERS];
    private final int[] score = new int[NUM_OF_PLAYERS];
    private final Deck cardsPlayed = new Deck(MAX_CARDS);
    
    private GameMode gameMode = GameMode.REDS;
    private boolean awaitingResponse = false;
    private int round = 0;
    private int response = -1;
    private boolean isReady = false;
    
    /**
     *
     * @param nickname
     */
    public TestBot(String nickname) {
        super(nickname);
    }
    
    /**
     *
     * @return
     */
    public Deck getCardsPlayed(){
        return cardsPlayed;
    }
    
    /**
     *
     * @return
     */
    public boolean isReady(){
        return isReady;
    }
    
    /**
     *
     * @return
     */
    public GameMode getGameMode(){
        return gameMode;
    }
    
    /**
     *
     * @return
     */
    @Override
    public int getRound(){
        return round;
    }
    
    /**
     *
     * @return
     */
    public boolean isAwaitingResponse(){
        return awaitingResponse;
    }
    
    /**
     *
     * @return
     */
    public int getResponse(){
        return response;
    }
    
    /**
     *
     * @param card
     */
    @Override
    public void playCard(Card card){
        awaitingResponse = true;
        super.playCard(card);
    }
    
    /**
     *
     * @param names
     */
    @Override
    public void setNames(String[] names){
        System.arraycopy(names, 0, this.names, 0, names.length);
    }
    
    /**
     *
     * @param score
     */
    @Override
    public void setScore(int[] score){
        System.arraycopy(score, 0, this.score, 0, score.length);
    }
    
    /**
     *
     * @param data
     */
    @Override
    public void action(byte[] data){
        ServerMessage msg = ServerMessage.values()[data[0]];
        
        switch(msg){
            case START:
                isReady = true;
                break;
                
            case EXIT:
                disconnect();
                break;
                
            case CONNECTION_LOST:
                int pId = data[1];
                if (pId != getId()){
                    disconnect();
                }
                break;
                
            case SCORE:
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    score[i] += data[i+1];
                }
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
                cardsPlayed.addTopCard(card1);
                break;
               
            case GAME_MODE:
                gameMode = GameMode.values()[data[1]];
                break;
                
            case GRADUATION:
                chooseGameMode(GameMode.QUARTS.ordinal());
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
                
                response = data[2];
                awaitingResponse = false;
                break;
                
            case ROUND:
                round = data[1];
                break;
        }
    }
}
