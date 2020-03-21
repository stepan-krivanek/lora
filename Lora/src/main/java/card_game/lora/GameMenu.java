/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author stepan
 */
public class GameMenu extends Parent{
    
    private Button startButton;
    private Button exitButton;

    public GameMenu(GameView game, Main program, Stage stage){
        StackPane menu = new StackPane();
        VBox centerVBox = new VBox();
        centerVBox.setSpacing(20);
        
        //Start Button
        startButton = new Button("START");
        startButton.setOnAction(e -> game.playLora());
        
        //Exit Button
        exitButton = new Button("EXIT");
        exitButton.setOnAction(e -> program.closeProgram(stage));
        
        centerVBox.getChildren().addAll(startButton, exitButton);
        menu.getChildren().add(centerVBox);
        menu.setAlignment(centerVBox, Pos.CENTER);
        
        getChildren().addAll(menu);
    }
}
