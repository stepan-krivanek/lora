/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 *
 * @author stepan
 */
public class Menu {
    
    private Button startButton =  new Button("Start");
    private Group buttons = new Group(startButton);
    private VBox layout = new VBox(buttons);

    public Parent getLayout(){
        return layout;
    }
    
    public void setStartButtonAction(Game game){
        startButton.setOnAction(e -> game.playLora(this));
    }
}
