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
import card_game.lora.game_modes.GameMode;
import card_game.lora.game_modes.GameModes;
import card_game.lora.game_modes.Tens;

/**
 *
 * @author stepa
 */
public class Game {
     
    private final int DECK_SIZE = 32;
    private final int NUM_OF_PLAYERS = 4;
    private final int NUM_OF_WAISTLINES = 4;
    private final Deck mainDeck = new Deck(DECK_SIZE, true);
    private final Player[] players = new Player[NUM_OF_PLAYERS];
    private final GameView gameView;
    private final List<Suit> suits;
    private final List<Rank> ranks;
    private final List<GameModes> gameModes;
    private GameMode gameMode;
    private Player firstPlayer;
    private int waistline;
    
    public Game(boolean multiplayer, Main program){
        Rank[] rankArr = new Rank[Rank.values().length];
        for (Rank rank : Rank.values()){
            rankArr[GameUtils.getRankValue(rank)] = rank;
        }
        ranks = Collections.unmodifiableList(Arrays.asList(rankArr));
        suits = Collections.unmodifiableList(Arrays.asList(Suit.values()));
        gameModes = Collections.unmodifiableList(
                Arrays.asList(GameModes.values())
        );
        
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
        gameMode = new Tens(this);
        gameMode.play();
    }
    
    public boolean playCard(Card card){
        return gameMode.playCard(card);
    }
    
    private void nextGameMode(){
        int index = gameMode.getId() + 1;
        
        if (index >= gameModes.size()){
            nextWaistline();
        }
        
        switch(gameModes.get(index)){
            case REDS:
                gameMode = new Tens(this);
                break;
                
            case SUPERIORS:
                gameMode = new Tens(this);
                break;
                
            case FRLA:
                gameMode = new Tens(this);
                break;
                
            case ALL:
                gameMode = new Tens(this);
                break;
                
            case RED_KING:
                gameMode = new Tens(this);
                break;
                
            case TENS:
                gameMode = new Tens(this);
                break;
                
            case QUARTS:
                gameMode = new Tens(this);
                break;
        } 
    }
    
    private void nextWaistline(){
        if (++waistline > NUM_OF_WAISTLINES){
            waistline = 0;
            endgame();
        } else {
            graduation(firstPlayer);
        }
    }
    
    private void graduation(Player player){
        //gameMode = gameView.chooseGameMode(player);
    }
    
    private void endgame(){
        
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
        gameView.showHand(players[0]);
        gameView.showPassButton(players[0]);
    }
    
    public List<Suit> getOrderedSuits(){
        return suits;
    }
    
    public List<Rank> getOrderedRanks(){
        return ranks;
    }
    
    public GameView getGameView(){
        return gameView;
    }
    
    public Player getFirstPlayer(){
        return firstPlayer;
    }
    
    public Player getNextPlayer(Player player){
        int index = player.getId() + 1;
        
        if (index >= players.length){
            return players[0];
        }
        return  players[index];
    }
}
