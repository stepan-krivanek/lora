/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import java.util.Random;

/**
 *
 * @author stepa
 */
public class Bot extends Player{
    
    private boolean isPlaying = false;
    
    public Bot(Game game, int id) {
        super(game, id);
    }
    
    @Override
    public void play(){
        isPlaying = true;
        playCard();
        isPlaying = false;
    }
    
    @Override
    public void stopPlaying(){
        isPlaying = false;
    }
    
    public boolean playCard(){                
        int size = getHand().size();
        
        for (int i = 0; i < size; i++){
            if (isPlaying == false){
                return false;
            }
            
            if (playCard(getHand().get(i)) == true){
                return true;
            }
        }
        
        return false;
    }
    
}
