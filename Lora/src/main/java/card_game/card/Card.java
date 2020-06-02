/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.card;

import javafx.scene.image.Image;

/**
 * Card that holds its suit and rank.
 * 
 * @author Štěpán Křivánek
 */
public class Card {
    
    private final Suit suit;
    private final Rank rank;
    
    /**
     * Creates a new card according to suit and rank.
     * 
     * @param suit Suit (color) of the card
     * @param rank Rank (value) of the card
     * @see card_game.card.Suit
     * @see card_game.card.Rank
     */
    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }
    
    /**
     * Creates a new card from byte.
     * Should <STRONG>only</STRONG> be used 
     * to recover a card compressed
     * by {@link #toByte() toByte} function
     * 
     * @param b Byte to recreate the card from
     * @see card_game.card.Card#toByte() 
     */
    public Card(byte b){
        int id = (new Byte(b)).intValue();
        suit = Suit.values()[(id / 10) % 10];
        rank = Rank.values()[id % 10];
    }
    
    /**
     * Gets the front image of the card.
     * 
     * @return Front image of the card
     * @see javafx.scene.image.Image
     */
    public Image getFront(){
        return CardUtils.getFrontImage(this);
    }
    
    /**
     *Gets the back image of the card.
     * 
     * @return Back image of the card
     * @see javafx.scene.image.Image
     */
    public Image getBack(){
        return CardUtils.getBackImage();
    }

    /**
     *
     * @return
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     *
     * @return
     */
    public Rank getRank() {
        return rank;
    }
    
    /**
     * Compresses the card into a single byte.
     * Card can be then recovered by {@link #Card(byte) Card}.
     * 
     * @return
     */
    public byte toByte(){
        Integer id = suit.ordinal() * 10 + rank.ordinal();
        return id.byteValue();
    }
    
    /**
     * Compares two cards.
     * Returns true if cards are the same,
     * returns false otherwise.
     * 
     * @param card Card to be compared to
     * @return If cards are the same
     */
    public boolean equals(Card card){
        return (card.rank == rank && card.suit == suit);
    }
    
    @Override
    public String toString(){
        return suit.toString() + " " + rank.toString();
    }
}
