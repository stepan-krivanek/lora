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
    private final Button spButton, mpButton, exitButton;
    private final VBox centerVBox;
    private final Main program;

    public GameMenu(Main program, Stage stage){
        this.program = program;
        
        Background bcgr = GameUtils.loadBackground(bcgrPath, width, height);
        setBackground(bcgr);
        setPrefHeight(height);
        setPrefWidth(width);
        
        centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setSpacing(SPACING);
        
        //SP Button
        spButton = new Button("Singleplayer");
        spButton.setOnAction(e -> {
            //TBA
        });
        
        //MP Button
        mpButton = new Button("Multiplayer");
        mpButton.setOnAction(e -> {
            gameCreation();
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
        centerVBox.getChildren().addAll(spButton, mpButton, exitButton);
    }
    
    private void gameCreation(){
        Button serverButton = new Button("Create new game");
        serverButton.setOnAction(e -> {
            chooseNumOfPlayers();
        });
        
        Button clientButton = new Button("Join existing game");
        clientButton.setOnAction(e -> {
            joinServer();
        });
        
        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            setMainButtons();
        });
        
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(serverButton, clientButton, returnButton);
    }
    
    private void chooseNumOfPlayers(){
        Text text = new Text("Choose number of players");
        text.setFont(Font.font ("Verdana", 30));
        text.setFill(Color.WHITE);
        
        Button one = new Button("1");
        one.setOnAction(e -> {
            startServer(1);
        });
        
        Button two = new Button("2");
        two.setOnAction(e -> {
            startServer(2);
        });
        
        Button three = new Button("3");
        three.setOnAction(e -> {
            startServer(3);
        });
        
        Button four = new Button("4");
        four.setOnAction(e -> {
            startServer(4);
        });
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            gameCreation();
        });
        
        HBox hBox = new HBox(one, two, three, four);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(SPACING);
        
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(text, hBox, cancel);
    }
    
    private void startServer(int numOfPlayers){
        Server server = new Server();
        Thread t = new Thread(server);
        t.start();
        
        addBots(server, numOfPlayers);
        joinServer();
    }
    
    private void addBots(Server server, int numOfPlayers){
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
