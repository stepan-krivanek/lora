/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.tactics.RandomTactic;
import card_game.lora.game_modes.GameMode;
import card_game.net.ClientConnection;
import card_game.net.ClientMessage;
import card_game.net.ServerMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import card_game.lora.tactics.TacticInterface;

/**
 * Bot that can play the game Lóra with a tactic
 * from {@link card_game.lora.tactics} chosen at random.
 * 
 * @author Štěpán Křivánek
 */
public class MpBot {
    
    private final int MSG_SIZE = 10;
    private final int NUM_OF_PLAYERS = 4;
    protected final int HAND_SIZE = 8;
    protected final Deck[] memory;
    private final String nickname;
    private final Deck hand = new Deck(HAND_SIZE);
    
    private int id = -1;
    private boolean isPlaying = false;
    private GameMode gameMode = GameMode.REDS;
    private boolean awaitingResponse = false;
    private boolean exit = false;
    private ClientConnection connection;
    private int round = 0;
    
    /**
     * Creates a new bot with nickname.
     * 
     * @param nickname Nickname of the bot
     */
    public MpBot(String nickname) {
        this.nickname = nickname;
        memory = new Deck[NUM_OF_PLAYERS];
        setMemory();
    }
    
    /**
     * Called when the player can play.
     */
    public void play(){
        isPlaying = true;
    }
    
    /**
     * Called when the player can not play anymore.
     */
    public void stopPlaying(){
        isPlaying = false;
    }
    
    /**
     * Sends a card to play to the server.
     * 
     * @param card Card to play
     */
    public void playCard(Card card){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.PLAY_CARD.ordinal();
        data[1] = card.toByte();
        sendToServer(data);
    }
    
    /**
     * Sends to the server that the player is finished playing.
     */
    public void pass(){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.PASS.ordinal();
        sendToServer(data);
    }
    
    /**
     * Sends a game mode chosen to graduate from to the server.
     * 
     * @param id Game mode id
     */
    public void chooseGameMode(int id){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.GAME_MODE.ordinal();
        data[1] = (byte)id;
        sendToServer(data);
    }
    
    /**
     * @return True if player is playing, false otherwise
     */
    public boolean isPlaying(){
        return isPlaying;
    }
    
    /**
     * Disonnects player from the server.
     */
    public void disconnect(){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.DISCONNECT.ordinal();
        sendToServer(data);
    }
    
    /**
     * Connects player to the server.
     * 
     * @return True if player was connected successfully, false otherwise
     */
    public boolean connectToServer(){
        connection = new ClientConnection(this);
        
        try {
            id = connection.getInput().readInt();
            connection.getOutput().writeUTF(nickname);
            connection.getOutput().flush();
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        Thread t = new Thread(connection);
        t.setName("Thread: connection to server " + id);
        t.start();
        return true;
    }
    
    public int getRound(){
        return round;
    }
    
    public Deck getHand(){
        return hand;
    }
    
    public Deck[] getMemory(){
        return memory;
    }

    public String getNickname(){
        return nickname;
    }
    
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
    
    private void sendToServer(byte[] data){
        try {
            connection.getOutput().write(data);
        } catch (IOException ex) {
            Logger.getLogger(MpPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setMemory(){
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            memory[i] = new Deck(8);
        }
    }
    
    /**
     * Sets the names of the players in the game.  
     *
     * @param names Players' names
     */
    public void setNames(String[] names){
        // No use for it yet
    }
    
    /**
     * Sets the initial score of the game.
     * 
     * @param score The initial score
     */
    public void setScore(int[] score){
        // No use for it yet
    }
    
    /**
     * Calls an action according to the received message.
     * 
     * @param data Message with the data received,
     * must contain the ServerMessage id at index 0
     * @see card_game.net.ServerMessage
     */
    public void action(byte[] data){
        ServerMessage msg = ServerMessage.values()[data[0]];
        
        switch(msg){
            case START:
                Brain brain = new Brain();
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
                // No use for it yet
                break;
                
            case PLAY:
                if (data[1] == getId()){
                    play();
                }
                break;
                
            case STOP_PLAYING:
                if (data[1] == getId()){
                    stopPlaying();
                }
                break;
                
            case CARD_PLAYED:
                Card card1 = new Card(data[1]);
                int playerId = data[2];
                
                memory[playerId].addTopCard(card1);
                break;
               
            case GAME_MODE:
                setMemory();
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
                
                awaitingResponse = false;
                break;
                
            case ROUND:
                round = data[1];
                break;
        }
    }
    
    private class Brain implements Runnable{

        @Override
        public void run() {
            TacticInterface tactic = new RandomTactic(MpBot.this);
            
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