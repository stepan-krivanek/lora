/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import java.io.InputStream;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Class contaning the core design elements for the game.
 * 
 * @author Štěpán Křivánek
 */
public class Design {
    
    /**
     * Enum of core colors used in the game.
     */
    public static enum Colour {

        BLUE("#7997A0"),GREY("#AAAFA1"),YELLOW("#B07437"),
        BROWN("#46362F"),ORANGE("#965629");
        
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
    public static final Paint YELLOW = Color.web(Colour.YELLOW.toString());
    public static final Paint ORANGE = Color.web(Colour.ORANGE.toString());
    public static final Paint BROWN = Color.web(Colour.BROWN.toString());
    
    private static final AudioClip click = new AudioClip(
        Button.class.getResource("/sounds/button_on_click.wav").toExternalForm()
    );
    
    private static final AudioClip card_play = new AudioClip(
        Button.class.getResource("/sounds/card_drop.wav").toExternalForm()
    );
    
    /**
     * Plays a sound of a click.
     */
    public static void playClick(){
        click.play();
    }
    
    public static void playCardPlay(){
        card_play.play();
    }
    
    /**
     * Gets the font used in the game.
     * 
     * @param size Size of the font
     * @return Font
     */
    public static Font Font(double size){
        InputStream file = Design.class.getResourceAsStream("/fonts/IMFellEnglish-Regular.ttf");
        return Font.loadFont(file, size);
    }
    
    /**
     * Predefined buttons for the game.
     */
    public static class Button extends ToggleButton { 
        
        
        
        /**
         * Creates the predefined button.
         * 
         * @param width Width of the button
         * @param height Height of the button
         */
        public Button(double width, double height) {
            this.setBackground(GameUtils.loadBackground(
                    "/images/button4.png", width, height)
            );
            
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
            
            this.setFont(Font(height / 2));
   
            this.setStyle("-fx-text-fill: " + Colour.GREY.toString());
            this.setOnAction(e -> {
                click.play();
            });
            this.setOnMouseEntered(e -> {
                this.setStyle("-fx-text-fill: " + Colour.YELLOW.toString());
            });
            this.setOnMouseExited(e -> {
                this.setStyle("-fx-text-fill: " + Colour.GREY.toString());
            });
        }
        
        
    }
    
    private Design(){}
}
