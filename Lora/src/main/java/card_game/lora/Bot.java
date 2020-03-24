/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

/**
 *
 * @author stepa
 */
public class Bot extends Player{
    
    private Game game;
    
    public Bot(Game game, int id, int handSize) {
        super(game, id, handSize);
        
        this.game = game;
    }
    
    @Override
    public void play(){
        game.playNext(getHand().remove(0));
    }
    
}
