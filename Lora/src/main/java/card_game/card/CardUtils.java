/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.card;

import java.util.HashMap;
import javafx.scene.image.Image;

/**
 *
 * @author stepa
 */
public class CardUtils {
    
    private static final HashMap<Rank, Integer> loraValues = new HashMap();
    private static final HashMap<Suit, HashMap<Rank, String>> frontImages = new HashMap();
    
    public static int getLoraCardValue(Card card){
        return loraValues.get(card.getRank());
    }
    
    //TO BE REWORKED
    public static Image getFrontImage(Card card){
        return  new Image("/images/green_king.png");
    }
    
    public static Image getBackImage(){
        return new Image("/images/card_bcgr.png");
    }
    
    private CardUtils(){
        initLoraValues();
        initFrontImages();
    }

    private void initLoraValues() {
        loraValues.put(Rank.SEVEN, 0);
        loraValues.put(Rank.EIGHT, 1);
        loraValues.put(Rank.NINE, 2);
        loraValues.put(Rank.TEN, 3);
        loraValues.put(Rank.INFERIOR, 4);
        loraValues.put(Rank.SUPERIOR, 5);
        loraValues.put(Rank.KING, 6);
        loraValues.put(Rank.ACE, 7);
    }

    //TO BE REWORKED
    private void initFrontImages() {
        HashMap<Rank, String> hashMap = new HashMap();
        hashMap.put(Rank.KING, "/images/green_king.png");
        frontImages.put(Suit.LEAF, hashMap);
    }
}
