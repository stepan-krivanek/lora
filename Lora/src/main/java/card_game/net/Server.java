/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.net;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.Game;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stepa
 */
public class Server implements Runnable{
    
    private final Game game = new Game(this);
    private final int NUM_OF_PLAYERS = 4;
    private final int MSG_SIZE = 10;
    private final int port = 1341;
    private final Connection[] players = new Connection[NUM_OF_PLAYERS];
    
    private ServerSocket socket;
    
    public Server(){
        try {
            socket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void acceptConnections(){
        int connectedPlayers = 0;
        
        while (connectedPlayers < NUM_OF_PLAYERS){
            try {
                Socket s = socket.accept();
                Connection connection = new Connection(s, connectedPlayers);
                players[connectedPlayers] = connection;
                
                Thread t = new Thread(connection);
                t.start();
                
                System.out.println("Player " + connectedPlayers + " has joint the game.");
                connectedPlayers += 1;
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("Game is ready to start.");
    }

    @Override
    public void run() {
        acceptConnections();
        start();
    }
    
    private void broadcast(byte[] data) {
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            send(data, i);
        }
    }
    
    private void send(byte[] data, int id){
        if (id >= NUM_OF_PLAYERS){
            return;
        }
        
        try {
            players[id].output.write(data);
            players[id].output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private byte[] initMessage(Message id){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)id.ordinal();
        return data;
    }
    
    private void start(){
        broadcast(initMessage(Message.START));
        game.start();
    }
    
    public void exit(){
        broadcast(initMessage(Message.EXIT));
    }
    
    public void endOfRound(){
        broadcast(initMessage(Message.END_OF_ROUND));
    }
    
    public void cardPlayed(Card card, int playerId){
        if (card == null) return;
        
        byte[] data = initMessage(Message.CARD_PLAYED);
        data[1] = card.toByte();
        data[2] = (byte)playerId;
        broadcast(data);
    }
    
    public void response(Card card, boolean correct, int id){
        if (card == null) return;
        
        byte[] data = initMessage(Message.PLAY_RESPONSE);
        data[1] = card.toByte();
        data[2] = correct == true ? (byte)1 : (byte)0;
        send(data, id);
    }
    
    public void hand(Deck deck, int id){
        byte[] data = initMessage(Message.HAND);
        
        for (int i = 0; i < deck.size(); i++){
            data[i+1] = deck.get(i).toByte();
        }
        
        send(data, id);
    }
    
    public void score(){
        //TBA
    }
    
    public void gameMode(int id){
        byte[] data = initMessage(Message.GAME_MODE);
        data[1] = (new Integer(id)).byteValue();
        broadcast(data);
    }
    
    public void play(int id){
        send(initMessage(Message.PLAY), id);
    }
    
    public void stopPlaying(int id){
        send(initMessage(Message.STOP_PLAYING), id);
    }
    
    private class Connection implements Runnable {
        
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;
        private int playerId;
        
        public Connection(Socket s, int id){
            socket = s;
            playerId = id;
            
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void run(){
            try {
                output.writeInt(playerId);
                output.flush();
                
                while(true){
                    byte data[] = new byte[MSG_SIZE];
                    input.read(data);
                    Card card = data[0] == -1 ? null : new Card(data[0]);
                    game.playCard(card, playerId);
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
