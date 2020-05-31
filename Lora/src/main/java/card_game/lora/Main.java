/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.CardUtils;
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

    private StackPane root;
    private Scene mainScene;
    private Stage stage;
    private String nickname = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        CardUtils.preloadImages();
        root = new StackPane();
        
        setStage(stage, "Lora");
        setMainMenu();
        
        mainScene = new Scene(root);
        stage.setScene(mainScene);
        stage.show();
    }
    
    public void setMainMenu(){
        GameMenu mainMenu = new GameMenu(this, nickname);
        stage.setOnCloseRequest(e -> {});
        root.getChildren().add(mainMenu);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getNickname(){
        return nickname;
    }
    
    public void close(){
        stage.close();
    }
    
    public Stage getStage(){
        return stage;
    }
    
    public Scene getScene(){
        return mainScene;
    }
    
    public StackPane getRoot(){
        return root;
    }
    
    private void setStage(Stage stage, String title){
        stage.setTitle(title);
        stage.setWidth(750);
        stage.setHeight(450);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.initStyle(StageStyle.UTILITY);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
