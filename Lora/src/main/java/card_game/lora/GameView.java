/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import java.io.FileInputStream;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author stepan
 */
public class GameView {
    
    private Button returnButton =  new Button("Back");
    private Group buttons = new Group(returnButton);
    private StackPane layout = new StackPane(buttons);
    private Stage stage;
    
    public GameView(Stage stage){
        this.stage = stage;
    }
    
    public void playLora(){
        
    }
    
    public Parent getLayout(){
        return layout;
    }
    
    private void setReturnButtonAction(Scene scene){
        returnButton.setOnAction(e -> {
            stage.getScene().setRoot(scene.getRoot());
         });
    }
    
    private StackPane setLayout() {
        /*try {
            Image table = new Image(new FileInputStream(imagesFolder + "\\table.png"));
        } catch (Exception e){
            
        }*/
        
        
        return null;
    }
}