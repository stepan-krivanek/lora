/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
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
    //private NotePad
    private Player firstPlayer;
    private Player playingOne;
    
    public Game(boolean multiplayer, Main program){
        gameView = new GameView();
        
        program.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                program.getMainMenu().setVisible(true);
                gameView.setVisible(false);
                program.getRoot().getChildren().remove(gameView);
            }
        });
        
        players[0] = new Player(this, 0, DECK_SIZE / NUM_OF_PLAYERS);
        for (int i = 1; i < NUM_OF_PLAYERS; i++){
            players[i] = new Bot(this, i, DECK_SIZE / NUM_OF_PLAYERS);
        }
        
        firstPlayer = players[0];
    }
    
    public void start(){
        cardDealing();
        playTens();
    }
    
    private void playTens(){
        gameView.showTens(players[0].getHandView());
        playingOne = firstPlayer;
        playingOne.play();
    }
    
    public void playNext(Card card){
        gameView.showCard(card);
        playingOne = getNextPlayer(playingOne);
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
