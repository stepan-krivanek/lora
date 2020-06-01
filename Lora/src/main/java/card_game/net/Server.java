/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.net;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.lora.Game;
import card_game.lora.GameUtils;
import card_game.lora.game_modes.GameModes;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stepa
 */
public class Server implements Runnable{
    
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final Game game;
    private final int NUM_OF_PLAYERS = 4;
    private final int MSG_SIZE = 10;
    private final int port = 7472;
    private final Connection[] players = new Connection[NUM_OF_PLAYERS];
    private final String[] names = new String[NUM_OF_PLAYERS];
    private final int[] score = new int[NUM_OF_PLAYERS];
    private final int gameModeId;
    private final int round;
    private final boolean singleGame;
    
    private boolean exit = false;
    private ServerSocket serverSocket;
    
    public Server(int[] score, int gameModeId, int round, boolean singleGame){
        this.gameModeId = gameModeId;
        this.round = round;
        this.singleGame = singleGame;
        System.arraycopy(score, 0, this.score, 0, NUM_OF_PLAYERS);
        
        game = new Game(this, score, round);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } 
    }
    
    public void acceptConnections(){
        int connectedPlayers = 0;
        
        while (connectedPlayers < NUM_OF_PLAYERS){
            try {
                Socket s = serverSocket.accept();
                
                Connection connection = new Connection(s, connectedPlayers);
                connection.output.writeInt(connectedPlayers);
                connection.output.flush();
                names[connectedPlayers] = connection.input.readUTF();
                players[connectedPlayers] = connection;
                
                Thread t = new Thread(connection);
                t.setName("Thread: connection to player " + connectedPlayers);
                t.start();
                
                System.out.println("Player " + connectedPlayers + " has joint the game.");
                connectedPlayers += 1;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

        newRound(round);
        for (Connection player : players){
            try {
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    player.output.writeInt(score[i]);
                    System.out.println("Server" + i + " " + score[i]);
                    player.output.flush();
                }
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    player.output.writeUTF(names[i]);
                    player.output.flush();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Failed to send game info", ex);
            }
        }
        
        try {
            serverSocket.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to close server socket", ex);
        }
        
        System.out.println("Game is ready to start.");
    }

    @Override
    public void run() {
        acceptConnections();
        start();
    }
    
    private void broadcast(byte[] data) {
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            send(data, i);
        }
    }
    
    private void send(byte[] data, int playerId){
        if (playerId >= NUM_OF_PLAYERS){
            return;
        }

        try {
            players[playerId].output.write(data);
            players[playerId].output.flush();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "player: " + Integer.toString(playerId), ex);
        }
    }
    
    private byte[] initMessage(ServerMessage msg){
        byte[] data = new byte[MSG_SIZE];
        data[0] = (byte)msg.ordinal();
        return data;
    }
    
    private void start(){
        broadcast(initMessage(ServerMessage.START));
        game.start(gameModeId, singleGame);
    }
    
    public void exit(){
        exit = true;
        broadcast(initMessage(ServerMessage.EXIT));
    }
    
    public void newRound(int round){
        byte[] data = initMessage(ServerMessage.ROUND);
        data[1] = (byte)round;
        broadcast(data);
    }
    
    public void graduation(int playerId){
        send(initMessage(ServerMessage.GRADUATION), playerId);
    }
    
    public void cardPlayed(Card card, int playerId){
        if (card == null) return;
        logger.log(Level.INFO, "Card {0} played by {1}",
                new Object[]{card.toString(), playerId}
        );
        
        byte[] data = initMessage(ServerMessage.CARD_PLAYED);
        data[1] = card.toByte();
        data[2] = (byte)playerId;
        broadcast(data);
    }
    
    public void response(Card card, boolean correct, int id){
        if (card == null) return;
        
        byte[] data = initMessage(ServerMessage.PLAY_RESPONSE);
        data[1] = card.toByte();
        data[2] = correct == true ? (byte)1 : (byte)0;
        send(data, id);
    }
    
    public void hand(Deck deck, int playerId){
        byte[] data = initMessage(ServerMessage.HAND);
        
        String msg = "Player " + playerId + " has got cards [";
        for (int i = 0; i < deck.size(); i++){
            Card card = deck.get(i);
            data[i+1] = card.toByte();
            msg += card.toString() + ", ";
        }
        msg += "]";
        
        logger.log(Level.INFO, msg);
        send(data, playerId);
    }
    
    public boolean score(int[] penalties){
        int length = penalties.length;
        if (penalties.length != 4){
            logger.log(Level.SEVERE, "Error: Wrong score format!");
            return false;
        }
        
        byte[] data = initMessage(ServerMessage.SCORE);
        for (int i = 0; i < length; i++){
            data[i+1] = (byte)penalties[i];
        }
        
        broadcast(data);
        return true;
    }
    
    public void gameMode(int id){
        byte[] data = initMessage(ServerMessage.GAME_MODE);
        data[1] = (new Integer(id)).byteValue();
        
        String msg = "Starting game mode " + GameModes.values()[id].toString();
        logger.log(Level.INFO, msg);
        broadcast(data);
    }
    
    public void play(int id){
        GameUtils.wait(2000, (Callable) () -> {
            send(initMessage(ServerMessage.PLAY), id);
            return null;
        });
    }
    
    public void stopPlaying(int id){
        send(initMessage(ServerMessage.STOP_PLAYING), id);
    }
    
    private void connectionLost(int playerId){
        byte[] data = initMessage(ServerMessage.CONNECTION_LOST);
        data[1] = (byte)playerId;
        broadcast(data);
    }
    
    private class Connection implements Runnable {
        
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;
        private int playerId;
        
        public Connection(Socket s, int id){
            socket = s;
            playerId = id;
            
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        
        public void run(){
            try {
                while(!exit){
                    byte data[] = new byte[MSG_SIZE];
                    input.read(data);
                    if (data[0] >= ServerMessage.values().length || data[0] < 0){
                        continue;
                    }
                    
                    ClientMessage msg = ClientMessage.values()[data[0]];
                    if (msg.equals(ClientMessage.DISCONNECT)){
                        if (!exit){
                            exit = true;
                            connectionLost(playerId);
                        }
                        
                        break;
                    }
                    
                    if(msg.equals(ClientMessage.GAME_MODE)){
                        game.startMode(data[1]);
                        continue;
                    }
                    
                    Card card = msg.equals(ClientMessage.PASS) ? null : new Card(data[1]);
                    game.playCard(card, playerId);
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
