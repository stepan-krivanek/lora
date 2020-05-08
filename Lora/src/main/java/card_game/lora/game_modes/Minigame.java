/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.card.Suit;
import card_game.lora.Game;
import card_game.lora.GameUtils;
import card_game.lora.Player;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author stepa
 */
public class Minigame {
    
    private final int MAX_CARDS = 32;
    private final int NUM_OF_PLAYERS;
    private final Deck discardDeck = new Deck(MAX_CARDS / 8);
    private final Game game;
    private int cardsPlayed = 0;
    private Player first;
    private Player player;
    private Suit leadSuit;
    
    public Minigame(Game game){
        this.game = game;
        NUM_OF_PLAYERS = game.getNumOfPlayers();
    }
    
    public void start() {
        first = game.getForehand();
        player = first;
        player.play();
    }

    public void playCard(Card card) {
        player.stopPlaying();
        player = game.getNextPlayer(player);

        cardsPlayed += 1;
        discardDeck.addTopCard(card);
        int playersPlayed = cardsPlayed % NUM_OF_PLAYERS;

        if (playersPlayed == 1){
            leadSuit = card.getSuit();
        }

        int timeToWait = 1000;
        if (playersPlayed == 0){
            GameUtils.wait(3 * timeToWait, new Callable() {
                @Override
                public Void call() throws Exception {
                    newRound();

                    if (cardsPlayed < MAX_CARDS){
                        player.play();
                    }
                    return null;
                }
            });
        } else {
            GameUtils.wait(timeToWait, new Callable() {
                @Override
                public Void call() throws Exception {
                    player.play();
                    return null;
                }
            });
        }
    }
    
    public boolean checkRules(Suit suit){
        if (cardsPlayed % 4 == 0){
            return true;
        }
        
        if (suit != leadSuit){
            if (player.getHand().contains(leadSuit)){
                return false;
            }
        }
        
        return true;
    }
    
    private void end(){
        Deck mainDeck = new Deck(MAX_CARDS);
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            player = game.getNextPlayer(player);
            mainDeck.addAll(player.getTable(), true);
        }
        
        game.nextGameMode(mainDeck);
    }
    
    private void newRound(){
        List<Rank> ranks = game.getRanks();
        int highestValue = ranks.indexOf(discardDeck.get(0).getRank());
        int index = 0;
        
        for (int i = 1; i < discardDeck.size(); i++){
            Card card = discardDeck.get(i);
            
            if (card.getSuit() == leadSuit){
                int cardValue = ranks.indexOf(card.getRank());
                if (cardValue > highestValue){
                    highestValue = cardValue;
                    index = i;
                }
            }
        }
        
        int id = (first.getId() + index) % NUM_OF_PLAYERS;
        first = game.getPlayer(id);
        first.getTable().addAll(discardDeck, true);
        player = first;
        
        if (cardsPlayed == MAX_CARDS){
            end();
        }
    }
    
    public Player getCurrentPlayer(){
        return player;
    }
}
