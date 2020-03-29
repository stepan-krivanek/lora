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
import card_game.lora.GameView;
import card_game.lora.Player;
import java.util.List;

/**
 *
 * @author stepa
 */
public class Tens implements GameMode{
    
    private int MAX_CARDS = 32;
    private int id = GameModes.TENS.ordinal();
    private Deck cardsPlayed = new Deck(MAX_CARDS);
    private Game game;
    private Player player;
    
    public Tens(Game game){
        this.game = game;
        player = game.getFirstPlayer();
    }

    @Override
    public void play(){
        game.getGameView().showTens();
        player.play();
    }
    
    @Override
    public boolean playCard(Card card){
        if (card == null){
            player.stopPlaying();
            player = game.getNextPlayer(player);
            player.play();
            return false;
        }
            
        if (checkRules(card)){
            cardsPlayed.add(card);
            game.getGameView().showCard(card);
            checkWinner();
            return true;
        }
        
        return false;
    }
    
    private void checkWinner(){
        if (player.getHand().size() == 1){
            game.getGameView().showWinner(player);
        }
    }
    
    private boolean checkRules(Card card){
        if (card.getRank() == Rank.TEN){
            return true;
        }

        Rank tmp;
        List<Rank> ranks = game.getOrderedRanks();
        int cardValue = ranks.indexOf(card.getRank());

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
    
    @Override
    public int getId(){
        return id;
    }
    
}
