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
    
    public Bot(Game game, int id) {
        super(game, id);
    }
    
    @Override
    public void play(){
        int cardsToPlay = new Random().nextInt(2) + 1;
        
        for (int i = 0; i < cardsToPlay; i++){
            if (playCard() == false){
                break;
            }
        }
        
        playCard(null);
    }
    
    public boolean playCard(){
        boolean correct = false;
                
        for (int i = 0; i < getHand().size(); i++){
            if (playCard(getHand().get(i)) == true){
                correct = true;
                break;
            }
        }
        
        return correct;
    }
    
}
