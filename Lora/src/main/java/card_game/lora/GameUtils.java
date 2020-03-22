/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Screen;

/**
 *
 * @author stepa
 */
public class GameUtils {
    
    private static final Rectangle2D BOUNDS = Screen.getPrimary().getBounds();
    private static final double SCREEN_WIDTH = BOUNDS.getWidth();
    private static final double SCREEN_HEIGHT = BOUNDS.getHeight();
    
    private GameUtils(){}
    
    public static Background loadBackground(
            String path, double width, double height)
    {
        Image image = new Image(
                path,
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
    
    public static double getScreenWidth(){
        return SCREEN_WIDTH;
    }
    
    public static double getScreenHeight(){
        return SCREEN_HEIGHT;
    }
}