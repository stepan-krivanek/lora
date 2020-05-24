/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.net.ClientConnection;
import card_game.net.Message;
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
    
    private final int MSG_SIZE = 10;
    protected final int HAND_SIZE = 8;
    private int id;
    private boolean isPlaying = false;
    private final Deck hand = new Deck(HAND_SIZE);
    private ClientConnection connection;
    private GameView gameView;
    
    public MpPlayer(Main program){
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
        data[0] = card.toByte();
        sendToServer(data);
    }
    
    public void pass(){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)-1;
        sendToServer(data);
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
    
    public boolean connectToServer(){   
        connection = new ClientConnection(this, "localhost");
        
        try {
            id = connection.getInput().readInt();
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        Thread t = new Thread(connection);
        t.start();
        return true;
    }
    
    public Deck getHand(){
        return hand;
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
        Message msg = Message.values()[data[0]];
        
        switch(msg){
            case START:
                Platform.runLater(() -> {
                    gameView.show();
                });
                break;
                
            case EXIT:
                Platform.runLater(() -> {
                    gameView.exit();
                });
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
                        
                Platform.runLater(() -> {
                    gameView.showCard(card1, playerId);
                });
                break;
               
            case GAME_MODE:
                Platform.runLater(() -> {
                    gameView.setGameMode(data[1]);
                });
                break;
                
            case END_OF_ROUND:
                Platform.runLater(() -> {
                    gameView.showWinner(id);
                });
                break;
                
            case HAND:
                hand.clear();
                
                for (int i = 1; i <= HAND_SIZE; i++){
                    Card card2 = new Card(data[i]);
                    hand.addTopCard(card2);
                }
                
                Platform.runLater(() -> {
                    gameView.showHand();
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
