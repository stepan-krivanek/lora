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
import card_game.lora.GameUtils;
import card_game.lora.Player;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author stepa
 */
public class Quarts implements GameMode{

    private final int MAX_CARDS = 32;
    private final int DECK_SIZE = 4;
    private final int id = GameModes.QUARTS.ordinal();
    private final List<Rank> ranks = GameUtils.getOrderedRanks();
    private final Deck cardsPlayed = new Deck(MAX_CARDS);
    private final Deck discardDeck = new Deck(DECK_SIZE);
    private final Game game;
    private Card leadCard;
    private Card highestCard;
    private Player first;
    private int cardsToPlay;
    
    public Quarts(Game game){
        this.game = game;
    }
    
    @Override
    public void start() {
        first = game.getForehand();
        first.play();
    }

    @Override
    public void playCard(Card card, int playerId) {
        if (checkRules(card)){
            if (discardDeck.isEmpty()){
                leadCard = card;
                setCardsToPlay(card);
                playAll();
            }
            
            if (card.equals(highestCard)){
                first = game.getPlayer(playerId);
            }
            
            cardsPlayed.add(card);
            discardDeck.add(card);
            if (discardDeck.size() >= cardsToPlay){
                stopAll();
                
                GameUtils.wait(3000, (Callable) () -> {
                    playNext();
                    return null;
                });
            }
        }
    }

    @Override
    public boolean checkRules(Card card) {
        if (card == null){
            return false;
        }
        
        if (discardDeck.isEmpty()){
            return true;
        }
        
        if (card.getSuit() != leadCard.getSuit()){
            return false;
        }
        
        int index = getRankDiff(card); 
        return !(index >= cardsToPlay || index < 0);
    }
    
    private void end(){
        Deck mainDeck = new Deck(MAX_CARDS);
        int[] penalties = new int[game.getNumOfPlayers()];
        
        for (int i = 0; i < game.getNumOfPlayers(); i++){
            Player p = game.getPlayer(i);
            penalties[i] = p.getHand().size();
            mainDeck.addAll(p.getTable(), true);
            mainDeck.addAll(p.getHand(), true);
        }
        
        game.nextGameMode(mainDeck, penalties);
    }
    
    private void stopAll(){
        for (int i = 0; i < game.getNumOfPlayers(); i++){
            game.getPlayer(i).stopPlaying();
        }
    }
    
    private void playAll(){
        for (int i = 0; i < game.getNumOfPlayers(); i++){
            game.getPlayer(i).play();
        }
    }
    
    private void playNext(){    
        first.getTable().addAll(discardDeck, true);

        boolean end = false;
        for (int i = 0; i < game.getNumOfPlayers(); i++){
            if (game.getPlayer(i).getHand().isEmpty()){
                end = true;
                break;
            }
        }
        
        if (end){
            end();
        } else {
            first.play();
        }
    }
    
    private void setCardsToPlay(Card card){
        int value = ranks.indexOf(card.getRank());
        
        cardsToPlay = 1;
        highestCard = card;
        for (int i = 1; i < DECK_SIZE; i++){
            int rankIndex = value + i;
            if (rankIndex >= ranks.size()){
                break;
            }
            
            Card tmp = new Card(card.getSuit(), ranks.get(rankIndex));
            if (cardsPlayed.contains(tmp)){
                break;
            }
            
            cardsToPlay += 1;
            highestCard = tmp;
        }
    }

    private int getRankDiff(Card card){
        int cardIndex = game.getRanks().indexOf(card.getRank());
        int leadCardIndex = game.getRanks().indexOf(leadCard.getRank());
        return cardIndex - leadCardIndex;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
}
