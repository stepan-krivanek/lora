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
 * Card utils to support the basic methods
 * for cards.
 * 
 * @author Štěpán Křivánek
 */
public class CardUtils {
    
    private static final String cards = "/images/cards/";
    private static final double CARD_WIDTH = GameUtils.getScreenWidth() / 6;
    private static boolean imagesLoaded = false;
    private static final Image BACK_IMAGE = loadCardImage(
            cards + "card_back3.png"
    );
    private static final HashMap<Suit, HashMap<Rank, Image>> FRONT_IMAGES =
            new HashMap();
    
    private CardUtils(){}
    
    /**
     * Gets front image for a card.
     * 
     * @param card Card to get the front image of
     * @return Front image of the card
     */
    public static Image getFrontImage(Card card){
        if (!imagesLoaded){
            preloadImages();
        }
        return  FRONT_IMAGES.get(card.getSuit()).get(card.getRank());
    }
    
    /**
     * Gets the back image of a card.
     * 
     * @return Back image of a card
     */
    public static Image getBackImage(){
        return BACK_IMAGE;
    }
    
    /**
     * Preloads images of all cards.
     * Used to speed up the actual image
     * loading when the speed doesn't matter.
     */
    public static void preloadImages(){
        initFrontImages();
        imagesLoaded = true;
    }
    
    private static Image loadCardImage(String path){
        return new Image(path, CARD_WIDTH, 0, true, true);
    }

    private static void initFrontImages() {
        final HashMap<Rank, Image> leaves = new HashMap();
        leaves.put(Rank.SEVEN, loadCardImage(cards + "green7.png"));
        leaves.put(Rank.EIGHT, loadCardImage(cards + "green8.png"));
        leaves.put(Rank.NINE, loadCardImage(cards + "green9.png"));
        leaves.put(Rank.TEN, loadCardImage(cards + "green10.png"));
        leaves.put(Rank.INFERIOR, loadCardImage(cards + "green_inferior.png"));
        leaves.put(Rank.SUPERIOR, loadCardImage(cards + "green_superior.png"));
        leaves.put(Rank.KING, loadCardImage(cards + "green_king.png"));
        leaves.put(Rank.ACE, loadCardImage(cards + "green_ace.png"));
        FRONT_IMAGES.put(Suit.LEAF, leaves);
        
        final HashMap<Rank, Image> hearts = new HashMap();
        hearts.put(Rank.SEVEN, loadCardImage(cards + "red7.png"));
        hearts.put(Rank.EIGHT, loadCardImage(cards + "red8.png"));
        hearts.put(Rank.NINE, loadCardImage(cards + "red9.png"));
        hearts.put(Rank.TEN, loadCardImage(cards + "red10.png"));
        hearts.put(Rank.INFERIOR, loadCardImage(cards + "red_inferior.png"));
        hearts.put(Rank.SUPERIOR, loadCardImage(cards + "red_superior.png"));
        hearts.put(Rank.KING, loadCardImage(cards + "red_king.png"));
        hearts.put(Rank.ACE, loadCardImage(cards + "red_ace.png"));
        FRONT_IMAGES.put(Suit.HEART, hearts);
        
        final HashMap<Rank, Image> bells = new HashMap();
        bells.put(Rank.SEVEN, loadCardImage(cards + "bell7.png"));
        bells.put(Rank.EIGHT, loadCardImage(cards + "bell8.png"));
        bells.put(Rank.NINE, loadCardImage(cards + "bell9.png"));
        bells.put(Rank.TEN, loadCardImage(cards + "bell10.png"));
        bells.put(Rank.INFERIOR, loadCardImage(cards + "bell_inferior.png"));
        bells.put(Rank.SUPERIOR, loadCardImage(cards + "bell_superior.png"));
        bells.put(Rank.KING, loadCardImage(cards + "bell_king.png"));
        bells.put(Rank.ACE, loadCardImage(cards + "bell_ace.png"));
        FRONT_IMAGES.put(Suit.BELL, bells);
        
        final HashMap<Rank, Image> accorns = new HashMap();
        accorns.put(Rank.SEVEN, loadCardImage(cards + "accorn7.png"));
        accorns.put(Rank.EIGHT, loadCardImage(cards + "accorn8.png"));
        accorns.put(Rank.NINE, loadCardImage(cards + "accorn9.png"));
        accorns.put(Rank.TEN, loadCardImage(cards + "accorn10.png"));
        accorns.put(Rank.INFERIOR, loadCardImage(cards + "accorn_inferior.png"));
        accorns.put(Rank.SUPERIOR, loadCardImage(cards + "accorn_superior.png"));
        accorns.put(Rank.KING, loadCardImage(cards + "accorn_king.png"));
        accorns.put(Rank.ACE, loadCardImage(cards + "accorn_ace.png"));
        FRONT_IMAGES.put(Suit.ACCORN, accorns);
    }
}
