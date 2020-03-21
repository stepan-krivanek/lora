/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
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
        
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        
        StackPane root = new StackPane();
        GameView game = new GameView(stage);
        GameMenu menu = new GameMenu(game, this, stage);

        root.setBackground(loadBackground(width, height));
        root.getChildren().addAll(menu);
        root.setAlignment(menu, Pos.CENTER);
        
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
    
    private Background loadBackground(double width, double height){
        Image image = new Image(
                "/images/the_card_players.jpg",
                width,
                height,
                false,
                true,
                true
        );
        
        BackgroundImage bcgrImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        
        return new Background(bcgrImage);
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
