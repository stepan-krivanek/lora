/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Improved ArrayList for the card storage needs.
 * 
 * @author Štěpán Křivánek
 */
public final class Deck extends ArrayList<Card> {
    
    private final int MAX_SIZE;
    
    /**
     * Creates the Deck for the maximum size of cards.
     * 
     * @param maxSize Max cards to store
     */
    public Deck(int maxSize){
        MAX_SIZE = maxSize;
    }
    
    /**
     * Creates the Deck for 32 cards and
     * fills it by unique cards.
     * 
     * @param fill If the deck should be filled by 32 unique cards
     */
    public Deck(boolean fill){
        MAX_SIZE = 32;
        
        if (fill == true){
            this.fill();
        }
    }
    
    /**
     * Merges two decks together.
     * 
     * @param deck2 Deck to be merged into this Deck
     * @return False if the two of Decks are too large together to be merged, 
     * true on success.
     */
    public boolean addAll(Deck deck2){
        if (deck2.size() > MAX_SIZE - this.size()){
            return false;
        }
        
        for (int i = 0; i < deck2.size(); i++){
            this.addTopCard(deck2.get(i));
        }
        return true;
    }
    
    /**
     * Merges two decks together and cleares the second one.
     * 
     * @param clear If deck2 should be cleared
     * @param deck2 Deck to be merged into this Deck
     * @return False if the two of Decks are too large together to be merged, 
     * true on success.
     */
    public boolean addAll(Deck deck2, boolean clear){
        boolean ret = false;
        if (this.addAll(deck2) == true){
            if (clear == true){
                deck2.clear();
            }
            ret = true;
        }
        return ret;
    }
    
    /**
     * Fills the deck with 32 unique cards.
     * 
     * @return False if the Deck is not empty or the max size is not 32,
     * otherwise true on success.
     */
    public boolean fill(){
        boolean ret = false;
        if (isEmpty() && MAX_SIZE == 32){
            for (Suit suit : Suit.values()){
                    for (Rank rank : Rank.values()){
                        add(new Card(suit, rank));
                    }
                }
            Collections.shuffle(this);
            ret = true;
        }
        return ret;
    }
    
    /**
     * Removes the top card of the Deck
     * 
     * @return The removed top card
     */
    public Card removeTopCard(){
        if (!isEmpty()){
            return remove(this.size() - 1);
        }
        return null;
    }
    
    /**
     * Adds a card to the top of the Deck.
     * 
     * @param card Cards to be added
     * @return False if the deck is full, otherwise true on success
     */
    public boolean addTopCard(Card card){
        if (!this.isFull()){
            add(card);
            return true;
        }
        return false;
    }
    
    /**
     * Adds a card to the bottom of the Deck.
     * 
     * @param card Card to be added
     * @return False if the Deck is full, otherwise true on success
     */
    public boolean addBotCard(Card card){
        if (!this.isFull()){
            add(0, card);
            return true;
        }
        return false;
    }
    
    /**
     * Simulates the real shuffle of a deck.
     */
    public void shuffle(){
        Collections.shuffle(this);
        int averageShuffle = 4;
        int numOfRounds = (new Random().nextInt(averageShuffle)) + 3;
        
        while (numOfRounds-- > 0){
            int numOfOverlays = (new Random().nextInt(averageShuffle)) + 3;
            int splits[] = new int[numOfOverlays];

            splits[0] = 0;
            for (int i = 1; i < numOfOverlays; i++){
                splits[i] = new Random().nextInt(this.size());
            }
            Arrays.sort(splits);

            Deck shuffledDeck = new Deck(this.size());
            int deckTop = this.size();
            for (int i = numOfOverlays; i > 0; i--){       
                for (int j = splits[i-1]; j < deckTop; j++){
                    shuffledDeck.add(get(j));
                }
                deckTop = splits[i-1];
            }
            this.addAll(shuffledDeck, true);
        }
        
        int halfSwap = new Random().nextInt(this.size());
        for (int i = 0; i < halfSwap; i++){
            this.addBotCard(remove(this.size() - 1));
        }
    }
    
    /**
     * Gets the index of a card in the Deck.
     * 
     * @param card Card to get index of
     * @return -1 if the card is not present in the Deck,
     * otherwise the index of the card in the Deck
     */
    public int indexOf(Card card){
        int ret = -1;
        
        for (int i = 0; i < size(); i++){
            Card tmp = get(i);
            if (tmp.getRank() == card.getRank()){
                if (tmp.getSuit() == card.getSuit()){
                    ret = i;
                    break;
                }
            }
        }
        
        return ret;
    }
    
    /**
     * Checks if the Deck contains the specified card.
     * 
     * @param card Card to check
     * @return True if the card is present in the Deck, false otherwise
     */
    public boolean contains(Card card){
        boolean contains = false;
        
        for (int i = 0; i < size(); i++){
            Card tmp = get(i);
            if (tmp.getRank() == card.getRank()){
                if (tmp.getSuit() == card.getSuit()){
                    contains = true;
                    break;
                }
            }
        }
        
        return contains;
    }
    
    /**
     * Checks if the Deck contains the suit specified.
     * 
     * @param suit Suit to check
     * @return True if the suit is present in the Deck, false otherwise
     */
    public boolean contains(Suit suit){
        boolean contains = false;
        
        for (int i = 0; i < size(); i++){
            if (get(i).getSuit() == suit){
                contains = true;
                break;
            }
        }
        
        return contains;
    }
    
    /**
     * Checks if the Deck is full.
     * 
     * @return True if Deck is full, false otherwise
     */
    public boolean isFull(){
        return this.size() == MAX_SIZE;
    }
    
    @Override
    public boolean isEmpty(){
        return this.size() == 0;
    }
    
    /**
     * Removes the specified card from the Deck.
     * 
     * @param card Card to remove from the Deck
     */
    public void remove(Card card){
        remove(indexOf(card));
    }
}
