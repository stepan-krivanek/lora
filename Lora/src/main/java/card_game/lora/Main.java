/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

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
    
    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage, "Lora");
        
        StackPane root = new StackPane();
        GameView game = new GameView();
        GameMenu menu = new GameMenu(game, this, stage);
        
        root.getChildren().addAll(menu, game);
        game.setVisible(false);
        
        Scene mainScene = new Scene(root);
        
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
    
    private void save(){
        //TO BE DONE
    }
    
    private void setStage(Stage stage, String title){
        stage.setTitle(title);
        stage.setFullScreen(true);
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
