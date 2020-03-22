/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.card;

import card_game.lora.GameUtils;
import java.util.HashMap;
import javafx.scene.image.Image;

/**
 *
 * @author stepa
 */
public class CardUtils {
    
    private static final double CARD_WIDTH = GameUtils.getScreenWidth() / 6;
    private static final Suit[] SUITS = new Suit[]{
        Suit.HEART, Suit.LEAF, Suit.BELL, Suit.ACCORN
    };
    private static final Rank[] RANKS = new Rank[]{
        Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN,
        Rank.INFERIOR, Rank.SUPERIOR, Rank.KING, Rank.ACE
    };
    private static boolean imagesLoaded = false;
    private static boolean valuesSet = false;
    private static final Image BACK_IMAGE = loadCardImage(
            "/images/card_bcgr.png"
    );
    private static final HashMap<Rank, Integer> LORA_VALUES = new HashMap();
    private static final HashMap<Suit, HashMap<Rank, Image>> FRONT_IMAGES =
            new HashMap();
    
    private CardUtils(){}
    
    public static Suit[] getOrderedSuits(){
        return SUITS;
    }
    
    public static Rank[] getOrderedRanks(){
        return RANKS;
    }
    
    public static int getLoraCardValue(Card card){
        if (!valuesSet){
            setValues();
        }
        return LORA_VALUES.get(card.getRank());
    }
    
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
    
    private static void setValues(){
        initLoraValues();
        valuesSet = true;
    }
    
    private static Image loadCardImage(String path){
        return new Image(path, CARD_WIDTH, 0, true, true);
    }

    private static void initLoraValues() {
        LORA_VALUES.put(Rank.SEVEN, 0);
        LORA_VALUES.put(Rank.EIGHT, 1);
        LORA_VALUES.put(Rank.NINE, 2);
        LORA_VALUES.put(Rank.TEN, 3);
        LORA_VALUES.put(Rank.INFERIOR, 4);
        LORA_VALUES.put(Rank.SUPERIOR, 5);
        LORA_VALUES.put(Rank.KING, 6);
        LORA_VALUES.put(Rank.ACE, 7);
    }

    //TO BE REWORKED
    private static void initFrontImages() {
        final HashMap<Rank, Image> leaves = new HashMap();
        leaves.put(Rank.SEVEN, loadCardImage("/images/green_king3.png"));
        leaves.put(Rank.EIGHT, loadCardImage("/images/green_king3.png"));
        leaves.put(Rank.NINE, loadCardImage("/images/green_king3.png"));
        leaves.put(Rank.TEN, loadCardImage("/images/green_king3.png"));
        leaves.put(Rank.INFERIOR, loadCardImage("/images/green_inferior.png"));
        leaves.put(Rank.SUPERIOR, loadCardImage("/images/green_superior.png"));
        leaves.put(Rank.KING, loadCardImage("/images/green_king3.png"));
        leaves.put(Rank.ACE, loadCardImage("/images/green_king3.png"));
        FRONT_IMAGES.put(Suit.LEAF, leaves);
        
        final HashMap<Rank, Image> hearts = new HashMap();
        hearts.put(Rank.SEVEN, loadCardImage("/images/green_king3.png"));
        hearts.put(Rank.EIGHT, loadCardImage("/images/green_king3.png"));
        hearts.put(Rank.NINE, loadCardImage("/images/green_king3.png"));
        hearts.put(Rank.TEN, loadCardImage("/images/green_king3.png"));
        hearts.put(Rank.INFERIOR, loadCardImage("/images/red_inferior.png"));
        hearts.put(Rank.SUPERIOR, loadCardImage("/images/green_king3.png"));
        hearts.put(Rank.KING, loadCardImage("/images/green_king3.png"));
        hearts.put(Rank.ACE, loadCardImage("/images/green_king3.png"));
        FRONT_IMAGES.put(Suit.HEART, hearts);
        
        final HashMap<Rank, Image> bells = new HashMap();
        bells.put(Rank.SEVEN, loadCardImage("/images/green_king3.png"));
        bells.put(Rank.EIGHT, loadCardImage("/images/green_king3.png"));
        bells.put(Rank.NINE, loadCardImage("/images/green_king3.png"));
        bells.put(Rank.TEN, loadCardImage("/images/green_king3.png"));
        bells.put(Rank.INFERIOR, loadCardImage("/images/green_king3.png"));
        bells.put(Rank.SUPERIOR, loadCardImage("/images/green_king3.png"));
        bells.put(Rank.KING, loadCardImage("/images/green_king3.png"));
        bells.put(Rank.ACE, loadCardImage("/images/green_king3.png"));
        FRONT_IMAGES.put(Suit.BELL, bells);
        
        final HashMap<Rank, Image> accorns = new HashMap();
        accorns.put(Rank.SEVEN, loadCardImage("/images/green_king3.png"));
        accorns.put(Rank.EIGHT, loadCardImage("/images/green_king3.png"));
        accorns.put(Rank.NINE, loadCardImage("/images/green_king3.png"));
        accorns.put(Rank.TEN, loadCardImage("/images/green_king3.png"));
        accorns.put(Rank.INFERIOR, loadCardImage("/images/green_king3.png"));
        accorns.put(Rank.SUPERIOR, loadCardImage("/images/green_king3.png"));
        accorns.put(Rank.KING, loadCardImage("/images/green_king3.png"));
        accorns.put(Rank.ACE, loadCardImage("/images/green_king3.png"));
        FRONT_IMAGES.put(Suit.ACCORN, accorns);
    }
}
