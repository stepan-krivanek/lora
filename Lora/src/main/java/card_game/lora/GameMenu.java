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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
            Server server = new Server(4);
            Thread t = new Thread(server);
            t.start();
            joinServer();
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
    
    private void joinServer(){
        MpPlayer player = new MpPlayer(program);
        
        if (player.connectToServer()){
            this.setVisible(false);
        }
    }
}
