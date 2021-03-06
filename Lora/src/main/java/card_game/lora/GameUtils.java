/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Rank;
import card_game.card.Suit;
import card_game.lora.game_modes.GameMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Screen;

/**
 * Game utils to support the core needs of the game.
 * 
 * @author Štěpán Křivánek
 */
public class GameUtils {
    
    private static final HashMap<GameMode, Image> MODES_IMAGES = new HashMap();
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
    
    /**
     * Loads a background with prespecified parameters.
     * 
     * @param path Path to load the background from
     * @param width Width of the background
     * @param height Height of the background
     * @return Background
     */
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
    
    /**
     * Preloads images when the speed doesn't matter
     * to speed up the actual loading later on.
     */
    public static void preloadImages(){
        initModeImages();
        imagesLoaded = true;
    }
    
    /**
     * Gets an image of a game mode.
     * 
     * @param gameMode Game mode to get image of
     * @return Image of the game mode
     */
    public static Image getModeImage(GameMode gameMode){
        if (!imagesLoaded){
            preloadImages();
        }
        return MODES_IMAGES.get(gameMode);
    }
    
    /**
     * Calls a function after a delay.
     * 
     * @param ms Delay in milliseconds
     * @param function Function to be called after the delay
     */
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
    
    public static List<GameMode> getOrderedGamemodes(){
        return Collections.unmodifiableList(Arrays.asList(GameMode.values()));
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
        return Screen.getPrimary().getBounds().getWidth();
    }
    
    public static double getScreenHeight(){
        return Screen.getPrimary().getBounds().getHeight();
    }
    
    /**
     * Gets the value of a rank.
     * 
     * @param rank Rank to get value of
     * @return Int value of the rank
     */
    public static int getRankValue(Rank rank){
        return rankValues.get(rank);
    }
    
    private static Image loadModeImage(String path){
        double screen_width = Screen.getPrimary().getBounds().getWidth();
        return new Image(path, screen_width / 7, 0, true, true);
    }
    
    private static void initModeImages(){
        final String modes = "/images/modes/";
        MODES_IMAGES.put(GameMode.REDS, loadModeImage(modes + "reds.png"));
        MODES_IMAGES.put(GameMode.SUPERIORS, loadModeImage(modes + "superiors.png"));
        MODES_IMAGES.put(GameMode.FRLA, loadModeImage(modes + "fr-la.png"));
        MODES_IMAGES.put(GameMode.ALL, loadModeImage(modes + "all.png"));
        MODES_IMAGES.put(GameMode.RED_KING, loadModeImage(modes + "red_king.png"));
        MODES_IMAGES.put(GameMode.TENS, loadModeImage(modes + "tens.png"));
        MODES_IMAGES.put(GameMode.QUARTS, loadModeImage(modes + "quarts.png"));
    }
}
