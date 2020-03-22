/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Deck;

/**
 *
 * @author stepa
 */
public class Game {
    
    private final int MAX_DECK_SIZE;
    private final Deck mainDeck;
    //private NotePad
    private final Player[] players;
    private int firstPlayer = 0;
    
    public Game(int numOfPlayers, int maxDeckSize){
        if ((numOfPlayers == 0) || (maxDeckSize % numOfPlayers != 0)){
            throw new ArithmeticException(
                    "Deck size must be divisible by the number of players!"
            );
        }
        
        MAX_DECK_SIZE = maxDeckSize;
        mainDeck = new Deck(MAX_DECK_SIZE, true);
        
        players = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++){
            players[i] = new Player(i, MAX_DECK_SIZE / numOfPlayers);
        }
    }
    
    public Player getPlayer(int index){
        return players[index];
    }
    
    public Deck getMainDeck(){
        return mainDeck;
    }
    
    public Player nextPlayer(Player player){
        int index = player.getId() + 1;
        
        if (index >= players.length){
            return players[0];
        }
        return  players[index];
    }
    
    public Player getFirstPlayer(){
        return players[firstPlayer];
    }
    
}
