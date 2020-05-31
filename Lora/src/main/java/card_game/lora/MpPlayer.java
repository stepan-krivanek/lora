/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.net.ClientConnection;
import card_game.net.ClientMessage;
import card_game.net.ServerMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.paint.Color;

/**
 *
 * @author stepa
 */
public class MpPlayer {
    
    private final GameView gameView;
    private final int MSG_SIZE = 10;
    private final int NUM_OF_PLAYERS = 4;
    private final int[] score = new int[NUM_OF_PLAYERS];
    protected final int HAND_SIZE = 8;
    private final String nickname;
    
    private int id = -1;
    private boolean isPlaying = false;
    private final Deck hand = new Deck(HAND_SIZE);
    private ClientConnection connection;
    
    public MpPlayer(String nickname, Main program){
        this.nickname = nickname;
        gameView = new GameView(program, this);
    }
    
    public void play(){
        isPlaying = true;
    }
    
    public void stopPlaying(){
        isPlaying = false;
    }
    
    public void playCard(Card card){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.PLAY_CARD.ordinal();
        data[1] = card.toByte();
        sendToServer(data);
    }
    
    public void pass(){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.PASS.ordinal();
        sendToServer(data);
    }
    
    public void chooseGameMode(int id){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.GAME_MODE.ordinal();
        data[1] = (byte)id;
        sendToServer(data);
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
    
    public void disconnect(){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)ClientMessage.DISCONNECT.ordinal();
        sendToServer(data);
    }
    
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
    
    public void setNames(String[] names){
        Platform.runLater(() -> {
            gameView.showNames(names);
        });
    }
    
    public Deck getHand(){
        return hand;
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
    
    public void action(byte[] data){
        ServerMessage msg = ServerMessage.values()[data[0]];
        
        switch(msg){
            case START:
                Platform.runLater(() -> {
                    gameView.show();
                });
                break;
                
            case EXIT:
                disconnect();
                Platform.runLater(() -> {
                    gameView.exit();
                });
                break;
                
            case CONNECTION_LOST:
                int pId = data[1];
                if (pId != id){
                    disconnect();
                    Platform.runLater(() -> {
                        gameView.connectionLost(pId);
                    });
                }
                break;
                
            case SCORE:
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    score[i] += data[i+1];
                }
                
                Platform.runLater(() -> {
                    gameView.updateScore(score);
                });
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
                        
                Platform.runLater(() -> {
                    gameView.showCard(card1, playerId);
                });
                break;
               
            case GAME_MODE:
                Platform.runLater(() -> {
                    gameView.setGameMode(data[1]);
                });
                break;
                
            case GRADUATION:
                Platform.runLater(() -> {
                    gameView.showGameModeSelection(this);
                });
                break;
                
            case HAND:
                hand.clear();
                
                for (int i = 1; i <= HAND_SIZE; i++){
                    Card card2 = new Card(data[i]);
                    hand.addTopCard(card2);
                }
                
                Platform.runLater(() -> {
                    gameView.showHands();
                });
                break;
                
            case PLAY_RESPONSE:
                Card card3 = new Card(data[1]);
                int index = hand.indexOf(card3);
                
                if (index == -1){
                    break;
                }
                
                if (data[2] == 1){
                    hand.remove(index);
                    Platform.runLater(() -> {
                        gameView.getHandView().removeCard(index);
                    });
                } else {
                    Platform.runLater(() -> {
                        gameView.getHandView().glowCard(index, Color.RED);
                    });
                }
                
                break;
        }
    }
}
