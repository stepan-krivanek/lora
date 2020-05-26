/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.net.Server;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author stepan
 */
public class GameMenu extends StackPane{
    
    private final int SPACING = 20;
    private final String bcgrPath = "/images/the_card_players.jpg";
    private final Rectangle2D bounds = Screen.getPrimary().getBounds();
    private final double width = bounds.getWidth();
    private final double height = bounds.getHeight();
    private final Button serverButton, clientButton, exitButton;
    private final VBox centerVBox;
    private final Main program;

    private int numOfPlayers = -1;
    
    public GameMenu(Main program, Stage stage){
        this.program = program;
        
        Background bcgr = GameUtils.loadBackground(bcgrPath, width, height);
        setBackground(bcgr);
        setPrefHeight(height);
        setPrefWidth(width);
        
        centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setSpacing(SPACING);
        
        //Create game
        serverButton = new Button("Create new game");
        serverButton.setOnAction(e -> {
            createGame();
        });
        
        //Join game
        clientButton = new Button("Join existing game");
        clientButton.setOnAction(e -> {
            joinServer();
        });
        
        //Exit Button
        exitButton = new Button("Exit");
        exitButton.setOnAction(e -> program.closeProgram(stage));
        
        getChildren().add(centerVBox);
        setAlignment(centerVBox, Pos.CENTER);
        
        setMainButtons();
    }
    
    private void setMainButtons(){
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(serverButton, clientButton, exitButton);
    }
    
    private void unselectAll(ToggleButton a, ToggleButton b, ToggleButton c, ToggleButton d){
        a.setSelected(false);
        b.setSelected(false);
        c.setSelected(false);
        d.setSelected(false);
    }    
    
    private void createGame(){
        numOfPlayers = -1;
        
        Text text = new Text("Choose number of players");
        text.setFont(Font.font ("Verdana", 30));
        text.setFill(Color.WHITE);
        
        ToggleButton one = new ToggleButton("1");
        ToggleButton two = new ToggleButton("2");
        ToggleButton three = new ToggleButton("3");
        ToggleButton four = new ToggleButton("4");
        
        one.setStyle("-fx-focus-color: transparent;");
        one.setOnAction(e -> {
            unselectAll(one, two, three, four);
            one.setSelected(true);
            numOfPlayers = 1;
        });
        
        two.setStyle("-fx-focus-color: transparent;");
        two.setOnAction(e -> {
            unselectAll(one, two, three, four);
            two.setSelected(true);
            numOfPlayers = 2;
        });
        
        three.setStyle("-fx-focus-color: transparent;");
        three.setOnAction(e -> {
            unselectAll(one, two, three, four);
            three.setSelected(true);
            numOfPlayers = 3;
        });
        
        four.setStyle("-fx-focus-color: transparent;");
        four.setOnAction(e -> {
            unselectAll(one, two, three, four);
            four.setSelected(true);
            numOfPlayers = 4;
        });
        
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-focus-color: transparent;");
        playButton.setOnAction(e -> {
            if (numOfPlayers > 0 && numOfPlayers <= 4){
                startServer();
            }
        });
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-focus-color: transparent;");
        cancelButton.setOnAction(e -> {
            setMainButtons();
        });
        
        HBox numbers = new HBox(one, two, three, four);
        numbers.setAlignment(Pos.CENTER);
        numbers.setSpacing(SPACING);
        
        HBox buttons = new HBox(playButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(SPACING);
        
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(text, numbers, buttons);
    }
    
    private void startServer(){
        Server server = new Server();
        Thread t = new Thread(server);
        t.start();
        
        addBots(numOfPlayers);
        joinServer();
    }
    
    private void addBots(int numOfPlayers){
        for (int i = 4; i > numOfPlayers;  --i){
            MpBot bot = new MpBot(program);
            bot.connectToServer();
        }
    }
    
    private void joinServer(){
        MpPlayer player = new MpPlayer(program);
        
        if (player.connectToServer()){
            this.setVisible(false);
        }
    }
}
