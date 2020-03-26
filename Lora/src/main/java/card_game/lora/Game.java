/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.card.Suit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.input.KeyCode;

/**
 *
 * @author stepa
 */
public class Game {
     
    private final int DECK_SIZE = 32;
    private final int NUM_OF_PLAYERS = 4;
    private final Deck mainDeck = new Deck(DECK_SIZE, true);
    private final Player[] players = new Player[NUM_OF_PLAYERS];
    private final GameView gameView;
    private final List<Suit> suits;
    private final List<Rank> ranks;
    //private NotePad
    private GameMode gameMode;
    private Player firstPlayer;
    private Player playingOne;
    private Deck cardsPlayed;
    
    public Game(boolean multiplayer, Main program){
        Rank[] rankArr = new Rank[Rank.values().length];
        for (Rank rank : Rank.values()){
            rankArr[GameUtils.getRankValue(rank)] = rank;
        }
        ranks = Collections.unmodifiableList(Arrays.asList(rankArr));
        
        Suit[] suitArr = new Suit[Suit.values().length];
        for (Suit suit : Suit.values()){
            suitArr[GameUtils.getSuitValue(suit)] = suit;
        }
        suits = Collections.unmodifiableList(Arrays.asList(suitArr));
        
        gameView = new GameView(this);
        
        program.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                program.getMainMenu().setVisible(true);
                gameView.setVisible(false);
                program.getRoot().getChildren().remove(gameView);
            }
        });
        
        players[0] = new Player(this, 0);
        for (int i = 1; i < NUM_OF_PLAYERS; i++){
            players[i] = new Bot(this, i);
        }
        
        firstPlayer = players[0];
    }
    
    public void start(){
        cardDealing();
        playTens();
    }
    
    public void playNext(Card card){
        if (card != null){
            cardsPlayed.add(card);
            gameView.showCard(card);
            checkWinner();
        } else {
            playingOne = getNextPlayer(playingOne);
            playingOne.play();
        }
    }
    
    public boolean checkRules(Card card){
        boolean ret = false;
        
        switch(gameMode){
            case TENS:
                if (card.getRank() == Rank.TEN){
                    ret = true;
                    break;
                }
                
                Rank tmp;
                int cardValue = ranks.indexOf(card.getRank());
                
                if (cardValue > ranks.indexOf(Rank.TEN)){
                    tmp = ranks.get(cardValue - 1);
                } else {
                    tmp = ranks.get(cardValue + 1);
                }
                
                if (cardsPlayed.contains(new Card(card.getSuit(), tmp))){
                    ret = true;
                }
                break;
        }
        
        return ret;
    }
    
    private boolean checkWinner(){
        boolean ret = false;
        
        for (int i = 0; i < players.length; i++){
            if (players[i].getHand().size() == 0){
                gameView.showWinner(players[i]);
                ret = true;
            }
        }
        
        return ret;
    }
    
    private void playTens(){
        cardsPlayed = new Deck(DECK_SIZE);
        gameMode = GameMode.TENS;
        gameView.showTens(players[0]);
        playingOne = firstPlayer;
        playingOne.play();
    }
    
    private void cardDealing(){
        gameView.cardDealing();
        mainDeck.shuffle();
        
        final int cardsToDeal = 2;
        Player player = firstPlayer;
        Card card;
        
        while (mainDeck.isEmpty() == false){
            for (int i = 0; i < cardsToDeal; i++){
                card = mainDeck.removeTopCard();
                player.getHand().add(card);
            }
            
            player = getNextPlayer(player);
        }
        
        players[0].setHandView(
            new HandView(players[0], gameView.getWidth(), gameView.getHeight())
        );
    }
    
    public List<Suit> getOrderedSuits(){
        return suits;
    }
    
    public List<Rank> getOrderedRanks(){
        return ranks;
    }
    
    public GameMode getGameMode(){
        return gameMode;
    }
    
    public GameView getGameView(){
        return gameView;
    }
    
    public Player getPlayer(int index){
        return players[index];
    }
    
    public Deck getMainDeck(){
        return mainDeck;
    }
    
    public Player getNextPlayer(Player player){
        int index = player.getId() + 1;
        
        if (index >= players.length){
            return players[0];
        }
        return  players[index];
    }
}
