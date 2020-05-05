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
import java.util.List;
import card_game.lora.game_modes.GameMode;
import card_game.lora.game_modes.GameModes;
import card_game.lora.game_modes.Quarts;
import card_game.lora.game_modes.RedKing;
import card_game.lora.game_modes.Reds;
import card_game.lora.game_modes.Superiors;
import card_game.lora.game_modes.Tens;
import card_game.net.Client;

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
    private final List<Suit> suits = GameUtils.getOrderedSuits();
    private final List<Rank> ranks = GameUtils.getOrderedRanks();
    private final List<GameModes> gameModes = GameUtils.getOrderedGamemodes();
    private GameMode gameMode;
    private Player forehand;
    private int round;
    
    public Game(){
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            players[i] = new Player(this);
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
            exit();
        } else {
            graduation(forehand);
        }
    }
    
    private void graduation(Player player){
        //gameMode = gameView.chooseGameMode(player);
    }
    
    private void cardDealing(){
        mainDeck.shuffle();
        
        final int cardsToDeal = 2;
        Player player = forehand;
        
        while (!mainDeck.isEmpty()){
            for (int i = 0; i < cardsToDeal; i++){
                Card card = mainDeck.removeTopCard();
                player.getHand().add(card);
            }
            
            player = getNextPlayer(player);
        }
    }
    
    public List<Suit> getSuits(){
        return suits;
    }
    
    public List<Rank> getRanks(){
        return ranks;
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
    
    private void exit(){
        
    }
}
