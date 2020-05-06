/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

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
    
    private int id;
    private HandView handView;
    private boolean isPlaying = false;
    private Connection connection;
    
    public void play(){
        isPlaying = true;
    }
    
    public void stopPlaying(){
        isPlaying = false;
    }
    
    public HandView getHandView(){
        return handView;
    }
    
    public void setHandView(HandView handView){
        this.handView = handView;
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
    
    public class Connection implements Runnable {
    
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
            
        }
    }
}
