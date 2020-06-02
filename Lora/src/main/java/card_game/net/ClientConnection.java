/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.net;

import card_game.lora.MpBot;
import card_game.lora.MpPlayer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client side conection framework to connect to the server,
 * reveive data from it and send data to it.
 * 
 * @author Štěpán Křivánek
 */
public class ClientConnection implements Runnable {

    private final int NUM_OF_PLAYERS = 4;
    private final int MSG_SIZE = 10;
    private final MpBot player;
    private InetAddress ipAddress;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    /**
     * Creates new connection for the specified MpPlayer.
     * 
     * @param player Player to be connected to the server
     */
    public ClientConnection(MpBot player){
        this.player = player;

        try {
            this.ipAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }       

        try {
            socket = new Socket(ipAddress, 7472);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return Input stream to receive data
     */
    public DataInputStream getInput(){
        return input;
    }

    /**
     *
     * @return Output stream to send data
     */
    public DataOutputStream getOutput(){
        return output;
    }

    @Override
    public void run() {
        String[] names = new String[NUM_OF_PLAYERS];
        int[] score = new int[NUM_OF_PLAYERS];
        byte[] data = new byte[MSG_SIZE];
        
        try{
            input.read(data);
            player.action(data);
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                score[i] = input.readInt();
            }
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                names[i] = input.readUTF();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        player.setScore(score);
        player.setNames(names);
        
        while (true){
            try {
                data = new byte[MSG_SIZE];
                input.read(data);
                if (data[0] >= ServerMessage.values().length || data[0] < 0){
                    continue;
                }
                
                player.action(data);
                
                ServerMessage msg = ServerMessage.values()[data[0]];
                if (msg.equals(ServerMessage.EXIT) || msg.equals(ServerMessage.CONNECTION_LOST)){
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(MpPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
