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

/**
 *
 * @author stepa
 */
public class MpPlayer {
    
    private final int MSG_SIZE = 10;
    private int id;
    private boolean isPlaying = false;
    private Connection connection;
    private GameView gameView;
    
    public void play(){
        isPlaying = true;
    }
    
    public void stopPlaying(){
        isPlaying = false;
    }
    
    // Must be reworked!!!
    public boolean playCard(Card card){
        byte[] data = new byte[MSG_SIZE];
        data[0] = card.toByte();
        
        try {
            connection.output.write(data);
            connection.input.read(data);
        } catch (IOException ex) {
            Logger.getLogger(MpPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data[1] == 1;
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
    
    public void setGameView(Main program){
        gameView = new GameView(program, this);
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
    
    private void action(byte[] data){
        Message msg = Message.values()[data[0]];
        
        switch(msg){
            case START:
                gameView.show();
                break;
                
            case EXIT:
                gameView.exit();
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
                gameView.showCard(card1);
                break;
               
            case GAME_MODE:
                gameView.setGameMode(data[1]);
                break;
                
            case END_OF_ROUND:
                gameView.showWinner(id);
                break;
                
            case HAND:
                Deck hand = new Deck(8);
                
                for (int i = 1; i <= 8; i++){
                    Card card2 = new Card(data[i]);
                    hand.add(card2);
                }
                
                gameView.showHand(hand);
                break;
                
            case PLAY_RESPONSE:
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
