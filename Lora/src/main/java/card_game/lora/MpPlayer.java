/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.net.Client;
import java.io.IOException;
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
    private Client client;
    
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
        client = new Client("localhost");
        
        try {
            id = client.getInput().readInt();
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
}
