/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.lora.Game;
import card_game.lora.Player;
import java.util.List;

/**
 *
 * @author stepa
 */
public class Tens implements GameMode{
    
    private final int NUM_OF_ROWS = 4;
    private final int NUM_OF_COLS = 8;
    private final int DECK_SIZE = 32;
    private final int id = GameModes.TENS.ordinal();
    private final Deck cardsPlayed = new Deck(DECK_SIZE);
    private final Game game;
    private Player player;
    
    public Tens(Game game){
        this.game = game;
    }

    @Override
    public void start(){
        player = game.getForehand();
        player.play();
    }
    
    @Override
    public void playCard(Card card){
        if (checkRules(card)){
            if (card == null){
                player.stopPlaying();
                player = game.getNextPlayer(player);
                player.play();
            } else if (checkRules(card)){
                cardsPlayed.add(card);
                checkWinner();
            }
        }
    }
    
    @Override
    public boolean checkRules(Card card){
        if (card == null){
            return true;
        }
        
        if (card.getRank() == Rank.TEN){
            return true;
        }

        List<Rank> ranks = game.getRanks();
        int cardValue = ranks.indexOf(card.getRank());

        Rank tmp;
        if (cardValue > ranks.indexOf(Rank.TEN)){
            tmp = ranks.get(cardValue - 1);
        } else {
            tmp = ranks.get(cardValue + 1);
        }

        if (cardsPlayed.contains(new Card(card.getSuit(), tmp))){
            return true;
        }
        
        return false;
    }
    
    private void end(){
        Deck mainDeck = new Deck(DECK_SIZE);
        
        for (int row = 0; row < NUM_OF_ROWS; row++){
            for (int col = 0; col < NUM_OF_COLS; col++){
                Card card = new Card(
                        game.getSuits().get(row),
                        game.getRanks().get(col)
                );
                
                if (cardsPlayed.contains(card)){
                    mainDeck.addTopCard(card);
                }
            }
        }
        
        for (int i = 0; i < game.getNumOfPlayers(); i++){
            player = game.getNextPlayer(player);
            
            while(!player.getHand().isEmpty()){
                mainDeck.addTopCard(player.getHand().removeTopCard());
                //add points
            }
        }
        
        game.nextGameMode(mainDeck);
    }
    
    private void checkWinner(){
        if (player.getHand().isEmpty()){
            end();
        }
    }
    
    @Override
    public int getId(){
        return id;
    }
}
