/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author stepan
 */
public class GameView extends Parent{
    
    private final Rectangle2D bounds = Screen.getPrimary().getBounds();
    private final double width = bounds.getWidth();
    private final double height = bounds.getHeight();
    private Button returnButton = new Button("Back");
    
    public GameView(){
        StackPane gameView = new StackPane();
        //Background bcgr = GameUtils.loadBackground(path, width, height);
        Background bcgr = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
        gameView.setBackground(bcgr);
        gameView.setPrefHeight(height);
        gameView.setPrefWidth(width);
        
        Image table = new Image("/images/table.png", width, height, false, true);
        ImageView imageView = new ImageView(table);
        imageView.setEffect(perspection(width, height));
        
        gameView.getChildren().addAll(imageView, returnButton);
        gameView.setAlignment(returnButton, Pos.CENTER);
        
        getChildren().add(gameView);
        
    }
    
    public void playLora(GameMenu menu){
        setReturnButtonAction(menu);
        this.setVisible(true);
        menu.setVisible(false);
        cardDealing();
        
        
    }
    
    private void setReturnButtonAction(GameMenu menu){
        returnButton.setOnAction(e -> {
            menu.setVisible(true);
            this.setVisible(false);
         });
    }
    
    private void cardDealing(){
        //Animation
        
    }
    
    private PerspectiveTransform perspection(double width, double height){
        final double NUM_OF_V_SEGMENTS = 3;
        final double NUM_OF_H_SEGMENTS = (NUM_OF_V_SEGMENTS / 3) * 5;
        double hSegment = width / NUM_OF_H_SEGMENTS;
        double vSegment = height / NUM_OF_V_SEGMENTS;
        double zoom = NUM_OF_V_SEGMENTS / 3;
        
        PerspectiveTransform perspection = new PerspectiveTransform(
                zoom * hSegment, vSegment,
                width - zoom * hSegment, vSegment,
                width + zoom * hSegment, height,
                0 - zoom * hSegment, height
        );
        
        return perspection;
    }

}