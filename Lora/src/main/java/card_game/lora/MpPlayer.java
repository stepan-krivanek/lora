/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.net.ServerMessage;
import javafx.application.Platform;
import javafx.scene.paint.Color;

/**
 * Communication node between a human player and a server.
 * 
 * @author Štěpán Křivánek
 */
public class MpPlayer extends MpBot{
    
    private final GameView gameView;
    private final int NUM_OF_PLAYERS = 4;
    private final int[] score = new int[NUM_OF_PLAYERS];
    
    /**
     * Creates a new MpPlayer with specified nickname.
     * 
     * @param nickname Nickname of the player
     * @param program Application to run at
     */
    public MpPlayer(String nickname, Main program){
        super(nickname);
        gameView = new GameView(program, this);
    }

    /**
     * Sets the names of the players in the game view.
     *  
     * @param names Names of the players
     */
    @Override
    public void setNames(String[] names){
        Platform.runLater(() -> {
            gameView.showNames(names);
        });
    }
    
    /**
     * Sets the initial score of the game in the game view.
     * 
     * @param score The initial score
     */
    @Override
    public void setScore(int[] score){
        System.arraycopy(score, 0, this.score, 0, score.length);
        Platform.runLater(() -> {
            gameView.updateScore(score);
        });
    }
    
    /**
     * Calls a specific action according to the data received.
     * 
     * @param data The data received,
     * must contain ServerMessage id at index 0
     * @see card_game.net.ServerMessage
     */
    @Override
    public void action(byte[] data){
        ServerMessage msg = ServerMessage.values()[data[0]];
        
        switch(msg){
            case START:
                Platform.runLater(() -> {
                    gameView.show();
                });
                break;
                
            case EXIT:
                disconnect();
                Platform.runLater(() -> {
                    gameView.exit();
                });
                break;
                
            case CONNECTION_LOST:
                int pId = data[1];
                if (pId != getId()){
                    disconnect();
                    Platform.runLater(() -> {
                        gameView.connectionLost(pId);
                    });
                }
                break;
                
            case SCORE:
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    score[i] += data[i+1];
                }
                
                Platform.runLater(() -> {
                    gameView.updateScore(score);
                });
                break;
                
            case PLAY:
                play();
                break;
                
            case STOP_PLAYING:
                stopPlaying();
                break;
                
            case CARD_PLAYED:
                Card card1 = new Card(data[1]);
                int playerId = data[2];
                        
                Platform.runLater(() -> {
                    gameView.showCard(card1, playerId);
                });
                break;
               
            case GAME_MODE:
                Platform.runLater(() -> {
                    gameView.setGameMode(data[1]);
                });
                break;
                
            case GRADUATION:
                Platform.runLater(() -> {
                    gameView.showGameModeSelection();
                });
                break;
                
            case HAND:
                getHand().clear();
                
                for (int i = 1; i <= HAND_SIZE; i++){
                    Card card2 = new Card(data[i]);
                    getHand().addTopCard(card2);
                }
                
                Platform.runLater(() -> {
                    gameView.showHands();
                });
                break;
                
            case PLAY_RESPONSE:
                Card card3 = new Card(data[1]);
                int index = getHand().indexOf(card3);
                
                if (index == -1){
                    break;
                }
                
                if (data[2] == 1){
                    getHand().remove(index);
                    Platform.runLater(() -> {
                        gameView.getHandView().removeCard(index);
                    });
                } else {
                    Platform.runLater(() -> {
                        gameView.getHandView().glowCard(index, Color.RED);
                    });
                }
                
                break;
                
            case ROUND:
                Platform.runLater(() -> {
                    gameView.newRound(data[1]);
                });
                break;
        }
    }
}
