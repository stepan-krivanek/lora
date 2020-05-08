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
 *
 * @author stepan
 */
public class Deck extends ArrayList<Card> {
    
    private final int MAX_SIZE;
    
    public Deck(int maxSize){
        MAX_SIZE = maxSize;
    }
    
    public Deck(int maxSize, boolean fill){
        MAX_SIZE = maxSize;
        
        if (fill == true){
            this.fill();
        }
    }
    
    public boolean addAll(Deck deck2){
        if (deck2.size() > MAX_SIZE - this.size()){
            return false;
        }
        
        for (int i = 0; i < deck2.size(); i++){
            this.addTopCard(deck2.get(i));
        }
        return true;
    }
    
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
    
    public Card removeTopCard(){
        if (!isEmpty()){
            return remove(this.size() - 1);
        }
        return null;
    }
    
    public boolean addTopCard(Card card){
        if (!this.isFull()){
            add(card);
            return true;
        }
        return false;
    }
    
    public boolean addBotCard(Card card){
        if (!this.isFull()){
            add(0, card);
            return true;
        }
        return false;
    }
    
    public void shuffle(){
        int averageShuffle = this.size() / 8;
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
    
    public boolean isFull(){
        return this.size() == MAX_SIZE;
    }
    
    public void remove(Card card){
        remove(indexOf(card));
    }
}
