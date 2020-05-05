/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.card;

import card_game.lora.GameUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import javafx.scene.image.Image;

/**
 *
 * @author stepa
 */
public class CardUtils {
    
    private static final double CARD_WIDTH = GameUtils.getScreenWidth() / 6;
    private static boolean imagesLoaded = false;
    private static final Image BACK_IMAGE = loadCardImage(
            "/images/card_bcgr.png"
    );
    private static final HashMap<Suit, HashMap<Rank, Image>> FRONT_IMAGES =
            new HashMap();
    
    private CardUtils(){}
    
    public static Image getFrontImage(Card card){
        if (!imagesLoaded){
            preloadImages();
        }
        return  FRONT_IMAGES.get(card.getSuit()).get(card.getRank());
    }
    
    public static Image getBackImage(){
        return BACK_IMAGE;
    }
    
    private static void preloadImages(){
        initFrontImages();
        imagesLoaded = true;
    }
    
    private static Image loadCardImage(String path){
        return new Image(path, CARD_WIDTH, 0, true, true);
    }

    //TO BE REWORKED
    private static void initFrontImages() {
        final HashMap<Rank, Image> leaves = new HashMap();
        leaves.put(Rank.SEVEN, loadCardImage("/images/green7.png"));
        leaves.put(Rank.EIGHT, loadCardImage("/images/green8.png"));
        leaves.put(Rank.NINE, loadCardImage("/images/green9.png"));
        leaves.put(Rank.TEN, loadCardImage("/images/green10.png"));
        leaves.put(Rank.INFERIOR, loadCardImage("/images/green_inferior.png"));
        leaves.put(Rank.SUPERIOR, loadCardImage("/images/green_superior.png"));
        leaves.put(Rank.KING, loadCardImage("/images/green_king.png"));
        leaves.put(Rank.ACE, loadCardImage("/images/green_ace.png"));
        FRONT_IMAGES.put(Suit.LEAF, leaves);
        
        final HashMap<Rank, Image> hearts = new HashMap();
        hearts.put(Rank.SEVEN, loadCardImage("/images/red7.png"));
        hearts.put(Rank.EIGHT, loadCardImage("/images/red8.png"));
        hearts.put(Rank.NINE, loadCardImage("/images/red9.png"));
        hearts.put(Rank.TEN, loadCardImage("/images/red10.png"));
        hearts.put(Rank.INFERIOR, loadCardImage("/images/red_inferior.png"));
        hearts.put(Rank.SUPERIOR, loadCardImage("/images/red_superior.png"));
        hearts.put(Rank.KING, loadCardImage("/images/red_king.png"));
        hearts.put(Rank.ACE, loadCardImage("/images/red_ace.png"));
        FRONT_IMAGES.put(Suit.HEART, hearts);
        
        final HashMap<Rank, Image> bells = new HashMap();
        bells.put(Rank.SEVEN, loadCardImage("/images/bell7.png"));
        bells.put(Rank.EIGHT, loadCardImage("/images/bell8.png"));
        bells.put(Rank.NINE, loadCardImage("/images/bell9.png"));
        bells.put(Rank.TEN, loadCardImage("/images/bell10.png"));
        bells.put(Rank.INFERIOR, loadCardImage("/images/bell_inferior.png"));
        bells.put(Rank.SUPERIOR, loadCardImage("/images/bell_superior.png"));
        bells.put(Rank.KING, loadCardImage("/images/bell_king.png"));
        bells.put(Rank.ACE, loadCardImage("/images/bell_ace.png"));
        FRONT_IMAGES.put(Suit.BELL, bells);
        
        final HashMap<Rank, Image> accorns = new HashMap();
        accorns.put(Rank.SEVEN, loadCardImage("/images/accorn7.png"));
        accorns.put(Rank.EIGHT, loadCardImage("/images/accorn8.png"));
        accorns.put(Rank.NINE, loadCardImage("/images/accorn9.png"));
        accorns.put(Rank.TEN, loadCardImage("/images/accorn10.png"));
        accorns.put(Rank.INFERIOR, loadCardImage("/images/accorn_inferior.png"));
        accorns.put(Rank.SUPERIOR, loadCardImage("/images/accorn_superior.png"));
        accorns.put(Rank.KING, loadCardImage("/images/accorn_king.png"));
        accorns.put(Rank.ACE, loadCardImage("/images/accorn_ace.png"));
        FRONT_IMAGES.put(Suit.ACCORN, accorns);
    }
}
