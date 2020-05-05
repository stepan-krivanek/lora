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
import card_game.lora.game_modes.All;
import card_game.lora.game_modes.FrLa;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.input.KeyCode;
import card_game.lora.game_modes.GameMode;
import card_game.lora.game_modes.GameModes;
import card_game.lora.game_modes.Quarts;
import card_game.lora.game_modes.RedKing;
import card_game.lora.game_modes.Reds;
import card_game.lora.game_modes.Superiors;
import card_game.lora.game_modes.Tens;
import card_game.net.ClientConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stepa
 */
public class Game {
     
    private final int DECK_SIZE = 32;
    private final int NUM_OF_PLAYERS = 4;
    private final int NUM_OF_ROUNDS = 4;
    private final Deck mainDeck = new Deck(DECK_SIZE, true);
    private final Player[] players = new Player[NUM_OF_PLAYERS];
    private final GameView gameView = new GameView(this);
    private final List<Suit> suits;
    private final List<Rank> ranks;
    private final List<GameModes> gameModes;
    private final Main program;
    private GameMode gameMode;
    private Player forehand;
    private int round;
    private ClientConnection connection;
    
    public Game(Main program){
        this.program = program;
        
        Rank[] rankArr = new Rank[Rank.values().length];
        for (Rank rank : Rank.values()){
            rankArr[GameUtils.getRankValue(rank)] = rank;
        }
        ranks = Collections.unmodifiableList(Arrays.asList(rankArr));
        suits = Collections.unmodifiableList(Arrays.asList(Suit.values()));
        gameModes = Collections.unmodifiableList(
                Arrays.asList(GameModes.values())
        );
        
        program.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                exit();
            }
        });
        
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            players[i] = new Player(this);
        }
        
        int id = connectToServer();
        if (id == -1){
            exit();
        }
    }
    
    public void start(){
        cardDealing();
        setGameMode(0);
        gameMode.start();
    }
    
    public void playCard(Card card){
        gameMode.playCard(card);
    }
    
    public boolean checkRules(Card card){
        return gameMode.checkRules(card);
    }
    
    public void nextGameMode(Deck deck){
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            players[i].stopPlaying();
        }
        
        mainDeck.addAll(deck);
        int index = gameMode.getId() + 1;
        
        if (index >= gameModes.size()){
            nextRound();
            System.out.println("Next round not implemented yet.");
        } else {
            cardDealing();
            setGameMode(index);
            gameMode.start();
        }
    }
    
    private void setGameMode(int index){
        switch(gameModes.get(index)){
            case REDS:
                gameMode = new Reds(this);
                break;
                
            case SUPERIORS:
                gameMode = new Superiors(this);
                break;
                
            case FRLA:
                gameMode = new FrLa(this);
                break;
                
            case ALL:
                gameMode = new All(this);
                break;
                
            case RED_KING:
                gameMode = new RedKing(this);
                break;
                
            case TENS:
                gameMode = new Tens(this);
                break;
                
            case QUARTS:
                gameMode = new Quarts(this);
                break;
        } 
    }
    
    private void nextRound(){
        if (++round > NUM_OF_ROUNDS){
            round = 0;
            endgame();
        } else {
            graduation(forehand);
        }
    }
    
    private void graduation(Player player){
        //gameMode = gameView.chooseGameMode(player);
    }
    
    private void endgame(){
        gameView.showWinner(forehand);
    }
    
    private void cardDealing(){
        mainDeck.shuffle();
        gameView.cardDealing();
        
        final int cardsToDeal = 2;
        Player player = forehand;
        
        while (!mainDeck.isEmpty()){
            for (int i = 0; i < cardsToDeal; i++){
                Card card = mainDeck.removeTopCard();
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
    
    public List<Suit> getSuits(){
        return suits;
    }
    
    public List<Rank> getRanks(){
        return ranks;
    }
    
    public GameView getGameView(){
        return gameView;
    }
    
    public Player getForehand(){
        return forehand;
    }
    
    public int getNumOfPlayers(){
        return NUM_OF_PLAYERS;
    }
    
    public Player getPlayer(int index){
        index = index % NUM_OF_PLAYERS;
        return players[index];
    }
    
    public Player getNextPlayer(Player player){
        int index = player.getId() + 1;
        
        if (index >= players.length){
            return players[0];
        }
        return  players[index];
    }
    
    /**
     returns player's id or -1 if connection fails;
     */
    private int connectToServer(){
        int ret = -1;
        
        connection = new ClientConnection("localhost");
        try {
            ret = connection.getInput().readInt();
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }
    
    private void exit(){
        program.getMainMenu().setVisible(true);
        gameView.setVisible(false);
        program.getRoot().getChildren().remove(gameView);
    }
}
