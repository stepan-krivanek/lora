/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author stepan
 */
public class Card extends ImageView{
    
    private final Suit suit;
    private final Rank rank;
    private final Image frontImage;
    private final Image backImage;
    
    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
        frontImage = CardUtils.getFrontImage(this);
        backImage = CardUtils.getBackImage();
        showFront();
    }
    
    public void showFront(){
        setImage(frontImage);
    }
    
    public void showBack(){
        setImage(backImage);
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
}
