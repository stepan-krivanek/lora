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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

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
    private GridPane cards;
    
    public Tens(Game game){
        this.game = game;
    }

    @Override
    public void start(){
        show();
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
                showCard(card);
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
        game.getGameView().getTable().getChildren().remove(cards);
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
    
    private void show(){  
        GameView gameView = game.getGameView();
        double width = gameView.getWidth();
        double height = gameView.getHeight();
        double cardWidth = gameView.getCardWidth();
        double cardHeight = gameView.getCardHeight();
        
        cards = new GridPane();
        cards.setAlignment(Pos.CENTER);
        cards.setPrefWidth(width);
        cards.setPrefHeight(height);
        cards.setHgap(NUM_OF_COLS);
        cards.setVgap(NUM_OF_ROWS);

        for (int col = 0; col < NUM_OF_COLS; col++){
            for (int row = 0; row < NUM_OF_ROWS; row++){
                Card card = new Card(
                        game.getSuits().get(row),
                        game.getRanks().get(col)
                );
                
                ImageView cardView = new ImageView(card.getFront());
                cardView.setOpacity(0.2);
                
                cardView.setFitWidth(cardWidth);
                cardView.setFitHeight(cardHeight);
                cardView.setPreserveRatio(true);
                
                GridPane.setConstraints(cardView, col, row);
                GridPane.setMargin(cardView, new Insets(5, 10, 5 ,10));
                
                cards.getChildren().add(cardView);
            }
        }
        
        game.getGameView().getTable().getChildren().add(cards);
    }
    
    private void showCard(Card card){
        int suitIndex = game.getSuits().indexOf(card.getSuit());
        int rankIndex = game.getRanks().indexOf(card.getRank());
        int cardIndex = suitIndex + rankIndex * game.getSuits().size();
        cards.getChildren().get(cardIndex).setOpacity(1);
    }
    
    @Override
    public int getId(){
        return id;
    }
}
