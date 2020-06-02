/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.net;

/**
 * Enum of messages to be possibly
 * send from client to server.
 * 
 * @author Štěpán Křivánek
 */
public enum ClientMessage {

    /**
     * Signals card, that player wants to play.
     * The card follows.
     */
    PLAY_CARD,

    /**
     * Signals that the player finished its turn.
     */
    PASS,

    /**
     * Signals game mode chosen for graduation.
     * The game mode id follows.
     */
    GAME_MODE,

    /**
     * Signals that player has exit the game.
     */
    DISCONNECT;
}
