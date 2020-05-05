/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.net;

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
    
    private final int port = 1341;
    private ServerSocket socket;
    private int numOfPlayers;
    private Connection[] players;
    
    public Server(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
        players = new Connection[numOfPlayers];
            
        try {
            socket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void acceptConnections(){
        int connectedPlayers = 0;
        
        while (connectedPlayers < numOfPlayers){
            try {
                Socket s = socket.accept();
                Connection connection = new Connection(s, connectedPlayers);
                players[connectedPlayers] = connection;
                
                System.out.println("Player number " + connectedPlayers + " has joint the game.");
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
        
        for (int i = 0; i < numOfPlayers; i++){
            Thread t = new Thread(players[i]);
            t.start();
        }
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
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
