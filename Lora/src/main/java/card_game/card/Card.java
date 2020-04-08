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
public class Card {
    
    private final Suit suit;
    private final Rank rank;
    private final Image frontImage;
    private final Image backImage;
    
    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
        frontImage = CardUtils.getFrontImage(this);
        backImage = CardUtils.getBackImage();
    }
    
    public Image getFront(){
        return frontImage;
    }
    
    public Image getBack(){
        return backImage;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
}
