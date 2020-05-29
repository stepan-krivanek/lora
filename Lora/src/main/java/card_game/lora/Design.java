/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 *
 * @author stepa
 */
public class Design {
    
    private enum Colour {
        BLUE("#7997A0"), GREY("#AAAFA1"), ORANGE("#B07437"), BROWN("#46362F");
        
        private final String name;
        private Colour(String name){
            this.name = name;
        }
        
        @Override
        public String toString(){
            return name;
        }
    }
    
    public static final Paint BLUE = Color.web(Colour.BLUE.toString());
    public static final Paint GREY = Color.web(Colour.GREY.toString());
    public static final Paint ORANGE = Color.web(Colour.ORANGE.toString());
    public static final Paint BROWN = Color.web(Colour.BROWN.toString());
    
    public static Font Font(double size){
        try {
            File file = new File("src/main/resources/fonts/IMFellEnglish-Regular.ttf");
            return Font.loadFont(new FileInputStream(file), size);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Design.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Font.font("Verdana", size);
    }
    
    public static class Button extends ToggleButton {
         
        
        public Button(double width, double height) {
            this.setBackground(GameUtils.loadBackground(
                    "/images/button4.png", width, height)
            );
            
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
            
            this.setFont(Font(height / 2));
   
            this.setStyle("-fx-text-fill: " + Colour.GREY.toString());
            this.setOnMouseEntered(e -> {
                this.setStyle("-fx-text-fill: " + Colour.ORANGE.toString());
            });
            this.setOnMouseExited(e -> {
                this.setStyle("-fx-text-fill: " + Colour.GREY.toString());
            });
        }
        
        
    }
    
    private Design(){}
    
}