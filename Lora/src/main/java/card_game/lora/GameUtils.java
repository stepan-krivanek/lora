/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Rank;
import card_game.card.Suit;
import card_game.lora.game_modes.GameModes;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
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
    private static final HashMap<GameModes, Image> MODES_IMAGES = new HashMap();
    private static final HashMap<Rank, Integer> rankValues = new HashMap(){
        {
            put(Rank.SEVEN, 0);
            put(Rank.EIGHT, 1);
            put(Rank.NINE, 2);
            put(Rank.TEN, 3);
            put(Rank.INFERIOR, 4);
            put(Rank.SUPERIOR, 5);
            put(Rank.KING, 6);
            put(Rank.ACE, 7);
        }
    };
    
    private static boolean imagesLoaded = false;
    
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
    
    public static void preloadImages(){
        initModeImages();
        imagesLoaded = true;
    }
    
    public static Image getModeImage(GameModes gameMode){
        if (!imagesLoaded){
            preloadImages();
        }
        return MODES_IMAGES.get(gameMode);
    }
    
    public static void wait(int ms, Callable function){
        Task<Void> wait = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(ms);
                return null;
            }
        };
        
        wait.setOnSucceeded(e -> {
            try {
                function.call();
            } catch (Exception exception){
                Logger.getLogger(GameUtils.class.getName()).log(
                        Level.SEVERE, "Function call failed", exception
                );
            }
        });
        
        Thread t = new Thread(wait);
        t.setName("Thread: wait");
        t.start();
    }
    
    public static List<GameModes> getOrderedGamemodes(){
        return Collections.unmodifiableList(Arrays.asList(GameModes.values()));
    }
    
    public static List<Suit> getOrderedSuits(){
        return Collections.unmodifiableList(Arrays.asList(Suit.values()));
    }
    
    public static List<Rank> getOrderedRanks(){
        Rank[] rankArr = new Rank[Rank.values().length];
        for (Rank rank : Rank.values()){
            rankArr[getRankValue(rank)] = rank;
        }
        return Collections.unmodifiableList(Arrays.asList(rankArr));
    }
    
    public static double getScreenWidth(){
        return SCREEN_WIDTH;
    }
    
    public static double getScreenHeight(){
        return SCREEN_HEIGHT;
    }
    
    public static int getRankValue(Rank rank){
        return rankValues.get(rank);
    }
    
    private static Image loadModeImage(String path){
        return new Image(path, SCREEN_WIDTH / 7, 0, true, true);
    }
    
    private static void initModeImages(){
        final String modes = "/images/modes/";
        MODES_IMAGES.put(GameModes.REDS, loadModeImage(modes + "reds.png"));
        MODES_IMAGES.put(GameModes.SUPERIORS, loadModeImage(modes + "superiors.png"));
        MODES_IMAGES.put(GameModes.FRLA, loadModeImage(modes + "fr-la.png"));
        MODES_IMAGES.put(GameModes.ALL, loadModeImage(modes + "all.png"));
        MODES_IMAGES.put(GameModes.RED_KING, loadModeImage(modes + "red_king.png"));
        MODES_IMAGES.put(GameModes.TENS, loadModeImage(modes + "tens.png"));
        MODES_IMAGES.put(GameModes.QUARTS, loadModeImage(modes + "quarts.png"));
    }
}
