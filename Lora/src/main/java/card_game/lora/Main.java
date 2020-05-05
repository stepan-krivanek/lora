/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.net.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author stepan
 */
public class Main extends Application {

    private boolean toBeSaved = true;
    private StackPane root;
    private GameMenu mainMenu;
    private Scene mainScene;
    
    @Override
    public void start(Stage stage) throws Exception {
        root = new StackPane();
        setStage(stage, "Lora");
        
        mainMenu = new GameMenu(this, stage);
        
        root.getChildren().add(mainMenu);
        
        mainScene = new Scene(root);
        stage.setScene(mainScene);
        stage.show();
    }
    
    public void closeProgram(Stage stage){
        if (ExitAlertBox.display(this, "Do you want to save the game?")){
            if (toBeSaved == true){
                save();
            }
            stage.close();
        }
    }
    
    public Scene getScene(){
        return mainScene;
    }
    
    public GameMenu getMainMenu(){
        return mainMenu;
    }
    
    public StackPane getRoot(){
        return root;
    }
    
    private void save(){
        //TO BE DONE
    }
    
    private void setStage(Stage stage, String title){
        stage.setTitle(title);
        stage.setWidth(500);
        stage.setHeight(300);
        //stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.initStyle(StageStyle.UTILITY);
        stage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(stage);
        });
    }
    
    public void setToBeSaved(boolean toBeSaved){
        this.toBeSaved = toBeSaved;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
