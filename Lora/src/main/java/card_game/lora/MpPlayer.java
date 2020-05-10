/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.net.Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
    private int id;
    private boolean isPlaying = false;
    private Deck hand;
    private Connection connection;
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
        connection = new Connection("localhost");
        
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
    
    private void sendToServer(byte[] data){
        try {
            connection.output.write(data);
        } catch (IOException ex) {
            Logger.getLogger(MpPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action(byte[] data){
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
                Platform.runLater(() -> {
                    gameView.showCard(card1);
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
                hand = new Deck(8);
                
                for (int i = 1; i <= 8; i++){
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
    
    private class Connection implements Runnable {
    
        private InetAddress ipAddress;
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;

        public Connection(String ipAddress){
            try {
                this.ipAddress = InetAddress.getByName(ipAddress);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }       

            try {
                socket = new Socket(ipAddress, 1341);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public DataInputStream getInput(){
            return input;
        }

        public DataOutputStream getOutput(){
            return output;
        }

        @Override
        public void run() {
            while (true){
                try {
                    byte[] data = new byte[MSG_SIZE];
                    input.read(data);
                    action(data);
                } catch (IOException ex) {
                    Logger.getLogger(MpPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
