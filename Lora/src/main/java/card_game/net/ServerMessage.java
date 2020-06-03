/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.net;

/**
 * Enum of messages to be possibly
 * send from server to client.
 * 
 * @author Štěpán Křivánek
 */
public enum ServerMessage {

    /**
     * Signals start of a new game.
     */
    START,

    /**
     * Signals end of the game.
     */
    EXIT,

    /**
     * Signals score update.
     * The score update follows.
     */
    SCORE,

    /**
     * Signals card that was played.
     * The card and playerId, who played the card follow.
     */
    CARD_PLAYED,

    /**
     * Signals new game mode to be started.
     * The game mode id follows.
     */
    GAME_MODE,

    /**
     * Signals which player can play.
     * Followed by the id of the player to play.
     */
    PLAY,

    /**
     * Signals a new round of the game.
     * The number of the round follows.
     */
    ROUND,

    /**
     * Signals which player can not play anymore.
     * Followed by the id of the player, who can
     * not play anymore.
     */
    STOP_PLAYING,

    /**
     * Signals that player is about to graduate.
     */
    GRADUATION,

    /**
     * Signals new cards handed out.
     * The cards follow.
     */
    HAND,

    /**
     * Signals response to the card that was requested to play.
     * The card and response follow.
     */
    PLAY_RESPONSE,

    /**
     * Signals that a player disconnected from the game.
     * Id of the player follows.
     */
    CONNECTION_LOST;
}
