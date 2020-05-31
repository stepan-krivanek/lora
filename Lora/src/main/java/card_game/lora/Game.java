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
import card_game.net.Server;
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
    private final int MAX_NUM_OF_GAMES = 40;
    private final Deck mainDeck = new Deck(DECK_SIZE, true);
    private final Player[] players = new Player[NUM_OF_PLAYERS];
    private final int[] score = new int[NUM_OF_PLAYERS];
    private final List<Suit> suits = GameUtils.getOrderedSuits();
    private final List<Rank> ranks = GameUtils.getOrderedRanks();
    private final List<GameModes> gameModes = GameUtils.getOrderedGamemodes();
    private final Server server;
    
    private GameMode gameMode;
    private Player forehand;
    private int round = 0;
    private int gamesPlayedInRound = 0;
    private int graduationAttempt = 0;
    private int gamesToPlay = MAX_NUM_OF_GAMES;
    
    public Game(Server server){
        this.server = server;
        
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            players[i] = new Player(this, i);
            score[i] = 0;
        }
        forehand = players[0];
    }
    
    public void start(int gameModeId){
        if ((gameModeId < 0) || (gameModeId >= GameModes.values().length)){
            gamesToPlay = MAX_NUM_OF_GAMES;
            gameModeId = 0;
        } else {
            gamesToPlay = 1;
        }
        
        cardDealing();
        setGameMode(gameModeId);
        gameMode.start();
    }
    
    public synchronized void playCard(Card card, int playerId){
        Player player = players[playerId];

        if (
            player.isPlaying() &&
            (card == null || player.getHand().contains(card)) &&
            checkRules(card))
        {
            server.response(card, true, playerId);
            server.cardPlayed(card, playerId);
            player.playCard(card);
            gameMode.playCard(card, playerId);
        } else {
            server.response(card, false, playerId);
        }
    }
    
    public void nextGameMode(Deck deck, int[] penalties){  
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            players[i].stopPlaying();
        }
        
        mainDeck.addAll(deck);
        if (mainDeck.size() != DECK_SIZE){
            GameModes current = gameModes.get(gameMode.getId());
            String msg = "Main deck was corrupted by " + current + ", creating new one.";
            Logger.getLogger(Game.class.getName()).log(Level.WARNING, msg);
            
            mainDeck.clear();
            Deck tmp = new Deck(DECK_SIZE, true);
            for (int i = 0; i < DECK_SIZE; i++){
                mainDeck.addTopCard(tmp.get(i));
            }
        }
        
        gamesPlayedInRound += 1;
        if (gamesPlayedInRound >= gamesToPlay){
            addScore(penalties);
            exit();
        } else if (gamesPlayedInRound >= gameModes.size()){
            graduation(penalties);
        } else {
            addScore(penalties);
            cardDealing();
            setGameMode(gamesPlayedInRound);
            gameMode.start();
        }
    }
    
    private void addScore(int[] penalties){
        String msg = "NEW SCORE:";
        for (int i = 0; i < score.length; i++){
            score[i] += penalties[i];
            msg += " Player " + i + ": " + score[i];
        }
        
        Logger.getLogger(Game.class.getName()).log(Level.INFO, msg);
        server.score(penalties);
    }
    
    private boolean checkRules(Card card){
        return gameMode.checkRules(card);
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
        
        server.gameMode(index);
    }
    
    private void nextRound(){
        if (++round >= NUM_OF_ROUNDS){
            round = 0;
            exit();
        } else {
            forehand = getNextPlayer(forehand);
            cardDealing();
            setGameMode(0);
            gameMode.start();
        }
    }
    
    private void graduation(int[] penalties){
        if (graduationAttempt > 0 && penalties[forehand.getId()] == 0){
            gamesPlayedInRound = 0;
            graduationAttempt = 0;
            addScore(penalties);
            nextRound();
        } else {
            if (graduationAttempt > 0){
                for (int i = 0; i < score.length; i++){
                    penalties[i] = i == forehand.getId() ? 8 : 0;
                }
            }
            addScore(penalties);
            
            if (++graduationAttempt > 3){
                graduationAttempt = 0;
                nextRound();
            } else {
                cardDealing();
                server.graduation(forehand.getId());
            }
        }
    }
    
    public void startMode(int id){
        setGameMode(id);
        gameMode.start();
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
        
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            server.hand(players[i].getHand(), i);
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
    
    public Server getServer(){
        return server;
    }
    
    public Player getNextPlayer(Player player){
        return  players[(player.getId() + 1) % NUM_OF_PLAYERS];
    }
    
    private void exit(){
        server.exit();
    }
}
