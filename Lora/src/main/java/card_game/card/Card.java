/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.card;

import javafx.scene.image.Image;

/**
 *
 * @author stepan
 */
public class Card implements Comparable<Card>{
    
    private final Suit suit;
    private final Rank rank;
    private final Image frontImage;
    private final Image backImage;
    
    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
        this.frontImage = CardUtils.getFrontImage(this);
        this.backImage = CardUtils.getBackImage();
    }

    /**
     * Compares Card A to Card B according to their Lora values.<br>
     * If value of A <b>equals</b> value of B, returns <b>0</b>.<br>
     * If value of A is <b>lower</b> than value of B, returns <b>-1</b>.<br>
     * If value of A is <b>greater</b> than value of B, returns <b>+1</b>.<br>
     * 
     * @param card
     * @return 
     */
    @Override
    public int compareTo(Card card) {
        int thisValue = CardUtils.getLoraCardValue(this);
        int otherValue = CardUtils.getLoraCardValue(card);
        
        if (thisValue == otherValue){
            return 0;
        }
        return thisValue < otherValue ? -1 : 1;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Image getFrontImage() {
        return frontImage;
    }

    public Image getBackImage() {
        return backImage;
    }
    
}
