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
 *<h1>Lóra</h1>
 * Multiplayer card game Lóra for 1-4 players.
 * Includes 7 game modes, which all together
 * define the game.
 * Has bots to substitute missing players.
 * 
 * @author Štěpán Křivánek
 * @version 1.0
 * @since 2020-06-02
 */
public class Main extends Application {

    private StackPane root;
    private Scene mainScene;
    private Stage stage;
    private String nickname = null;
    
    /**
     * Starts the actual javaFX application
     * 
     * @param stage
     * @throws Exception
     * @see javafx.application.Application
     */
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
    
    /**
     * Used to set the main menu from any other GUI method
     */
    public void setMainMenu(){
        GameMenu mainMenu = new GameMenu(this, nickname);
        stage.setOnCloseRequest(e -> {});
        root.getChildren().add(mainMenu);
    }

    /**
     * Used to set a nickname of a player
     * 
     * @param nickname Players new nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * Returns players current nickname
     * 
     * @return
     */
    public String getNickname(){
        return nickname;
    }
    
    /**
     * Closes the main stage of the application.
     * It should always be called as last method
     * before the end.
     * 
     * @see javafx.stage.Stage
     */
    public void close(){
        stage.close();
    }
    
    /**
     * 
     * @return
     */
    public Stage getStage(){
        return stage;
    }
    
    /**
     *
     * @return
     */
    public Scene getScene(){
        return mainScene;
    }
    
    /**
     *
     * @return
     */
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
    
    /**
     * Main method only launches javafx Application
     * 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
