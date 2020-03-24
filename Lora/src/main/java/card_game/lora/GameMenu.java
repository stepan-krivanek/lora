/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
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
public class GameMenu extends Parent{
    
    private final int SPACING = 20;
    private final String bcgrPath = "/images/the_card_players.jpg";
    private final Rectangle2D bounds = Screen.getPrimary().getBounds();
    private final double width = bounds.getWidth();
    private final double height = bounds.getHeight();
    private Button startButton;
    private Button exitButton;

    public GameMenu(Main program, Stage stage){
        StackPane menu = new StackPane();
        Background bcgr = GameUtils.loadBackground(bcgrPath, width, height);
        menu.setBackground(bcgr);
        menu.setPrefHeight(height);
        menu.setPrefWidth(width);
        
        VBox centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setSpacing(SPACING);
        
        //Start Button
        startButton = new Button("START");
        startButton.setOnAction(e -> {
            Game game = new Game(false, program);
            program.getRoot().getChildren().add(game.getGameView());
            this.setVisible(false);
            game.start();
        });
        
        //Exit Button
        exitButton = new Button("EXIT");
        exitButton.setOnAction(e -> program.closeProgram(stage));
        
        centerVBox.getChildren().addAll(startButton, exitButton);
        menu.getChildren().add(centerVBox);
        menu.setAlignment(centerVBox, Pos.CENTER);
        
        getChildren().add(menu);
    }
}
