/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author stepan
 */
public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage, "Lora");
        
        Menu menu = new Menu();
        Game game = new Game(stage);
        menu.setStartButtonAction(game);
        
        Scene mainScene = new Scene(menu.getLayout());
        
        
        stage.setScene(mainScene);
        stage.show();
    }
    
    private void setStage(Stage stage, String title){
        stage.setTitle(title);
        stage.setMaximized(true);
        stage.initStyle(StageStyle.UTILITY);
    }
}
