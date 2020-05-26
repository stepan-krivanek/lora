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
import card_game.lora.Player;
import java.util.List;

/**
 *
 * @author stepa
 */
public class Minigame {
    
    private final int MAX_CARDS = 32;
    private final int NUM_OF_PLAYERS;
    private final int[] trickTakers;
    private final Deck discardDeck = new Deck(4);
    protected final Game game;
    protected final int[] penalties;
    
    private int round = 0;
    private int cardsPlayed = 0;
    private Player first;
    private Player player;
    private Suit leadSuit;
    
    public Minigame(Game game){
        this.game = game;
        NUM_OF_PLAYERS = game.getNumOfPlayers();
        trickTakers = new int[MAX_CARDS / NUM_OF_PLAYERS];
        penalties = new int[NUM_OF_PLAYERS];
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

        if (playersPlayed == 0){
            newRound();
        } else {
            player.play();
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
    
    public void end(){
        Deck mainDeck = new Deck(MAX_CARDS);
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            player = game.getNextPlayer(player);
            mainDeck.addAll(player.getTable(), true);
            mainDeck.addAll(player.getHand(), true);
        }
        
        game.nextGameMode(mainDeck, penalties);
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
        
        trickTakers[round] = first.getId();
        round += 1;
        
        if (cardsPlayed >= MAX_CARDS){
            end();
        } else {
            player.play();
        }
    }
    
    public Player getCurrentPlayer(){
        return player;
    }
    
    public int cardsPlayed(){
        return cardsPlayed;
    }
    
    public int[] getTrickTakers(){
        return trickTakers;
    }
}
