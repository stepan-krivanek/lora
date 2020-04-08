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
import card_game.lora.GameView;
import card_game.lora.Player;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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
    private StackPane discardLayout;
    
    public Minigame(Game game){
        this.game = game;
        NUM_OF_PLAYERS = game.getNumOfPlayers();
    }
    
    public void start() {
        show();
        first = game.getForehand();
        player = first;
        player.play();
    }

    public void playCard(Card card) {
        player.stopPlaying();
        player = game.getNextPlayer(player);

        cardsPlayed += 1;
        discardDeck.addTopCard(card);
        showCard(card);

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
        if (suit != leadSuit){
            if (player.getHand().contains(leadSuit)){
                return false;
            }
        }
        
        return true;
    }
    
    private void end(){
        game.getGameView().getTable().getChildren().remove(discardLayout);
        
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
        
        discardLayout.getChildren().clear();
        
        if (cardsPlayed == MAX_CARDS){
            end();
        }
    }
    
    private void show(){
        GameView gameView = game.getGameView();
        discardLayout = new StackPane();
        discardLayout.setPrefWidth(gameView.getWidth());
        discardLayout.setPrefHeight(gameView.getHeight());
        discardLayout.setAlignment(Pos.CENTER);
        
        gameView.getTable().getChildren().add(discardLayout);
    }
    
    private void showCard(Card card){
        GameView gameView = game.getGameView();
        double cardWidth = gameView.getCardWidth();
        double cardHeight = gameView.getCardHeight();
        
        ImageView cardView = new ImageView(card.getFront());
        cardView.setFitWidth(cardWidth);
        cardView.setFitHeight(cardHeight);
        cardView.setPreserveRatio(true);
        cardView.setRotate(new Random().nextInt(360));
        
        discardLayout.getChildren().add(cardView);
    }
    
    public Player getCurrentPlayer(){
        return player;
    }
}
