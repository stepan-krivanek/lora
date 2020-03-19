/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import java.io.FileInputStream;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author stepan
 */
public class Game {
    
    String imagesFolder = "C:\\Users\\stepa\\Documents\\school\\PJVS\\pictures";
    private Button returnButton =  new Button("Back");
    private Group buttons = new Group(returnButton);
    private StackPane layout = new StackPane(buttons);
    private Stage stage;
    
    public Game(Stage stage){
        this.stage = stage;
    }
    
    public void playLora(Menu menu){
        setReturnButtonAction(menu.getLayout());
        StackPane layout = setLayout();
        
        stage.getScene().setRoot(this.layout);
    }
    
    public Parent getLayout(){
        return layout;
    }
    
    private void setReturnButtonAction(Parent parent){
        returnButton.setOnAction(e -> stage.getScene().setRoot(parent));
    }
    
    private StackPane setLayout() {
        /*try {
            Image table = new Image(new FileInputStream(imagesFolder + "\\table.png"));
        } catch (Exception e){
            
        }*/
        
        
        return null;
    }
}