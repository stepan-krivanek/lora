/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.net;

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
 *
 * @author stepa
 */
public class ClientConnection implements Runnable {

    private final int NUM_OF_PLAYERS = 4;
    private final int MSG_SIZE = 10;
    private final MpPlayer player;
    private InetAddress ipAddress;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientConnection(MpPlayer player){
        this.player = player;

        try {
            this.ipAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }       

        try {
            socket = new Socket(ipAddress, 7473);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
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
