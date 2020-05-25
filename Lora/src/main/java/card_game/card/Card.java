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
    
    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }
    
    public Card(byte b){
        int id = (new Byte(b)).intValue();
        suit = Suit.values()[(id / 10) % 10];
        rank = Rank.values()[id % 10];
    }
    
    public Image getFront(){
        return CardUtils.getFrontImage(this);
    }
    
    public Image getBack(){
        return CardUtils.getBackImage();
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
    
    public byte toByte(){
        Integer id = suit.ordinal() * 10 + rank.ordinal();
        return id.byteValue();
    }
    
    public boolean equals(Card card){
        return (card.rank == rank && card.suit == suit);
    }
    
    public String toString(){
        return suit.toString() + " " + rank.toString();
    }
}
