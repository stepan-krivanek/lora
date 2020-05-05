/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora.game_modes;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.Game;
import card_game.lora.GameUtils;
import card_game.lora.Player;
import java.util.concurrent.Callable;

/**
 *
 * @author stepa
 */
public class Quarts implements GameMode{

    private final int MAX_CARDS = 32;
    private final int DECK_SIZE = MAX_CARDS / 8;
    private final int id = GameModes.QUARTS.ordinal();
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
    public void playCard(Card card) {
        if (checkRules(card)){
            if (discardDeck.isEmpty()){
                leadCard = card;
                setFirstPlayer();
                playAll();
            }
            
            cardsToPlay -= 1;
            cardsPlayed.add(card);
            discardDeck.add(card);
            
            if (cardsToPlay <= 0){
                stopAll();
                
                GameUtils.wait(3000, new Callable() {
                    @Override
                    public Void call() throws Exception {
                        
                        playNext();
                        return null;
                    }
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
        
        int index = getRankDiff(card);        
        return !(index >= DECK_SIZE || index < 0);
    }
    
    private void end(){
        Deck mainDeck = new Deck(MAX_CARDS);
        for (int i = 0; i < game.getNumOfPlayers(); i++){
            Player p = game.getPlayer(i);
            mainDeck.addAll(p.getTable(), true);
        }
        
        game.nextGameMode(mainDeck);
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

        if (cardsPlayed.size() >= MAX_CARDS){
            end();
        } else {
            first.play();
        }
    }
    
    private void setFirstPlayer(){
        boolean found = false;
        
        for (int i = 0; i < game.getNumOfPlayers(); i++){
            Player p = game.getPlayer(i);
            
            for (int j = 0; j < p.getHand().size(); j++){
                if (p.getHand().get(j) == highestCard){
                    first = p;
                    found = true;
                    break;
                }
                
                if (found){
                    break;
                }
            }
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
