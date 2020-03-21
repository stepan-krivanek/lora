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
public class Deck {
    
    private final int MAX_SIZE;
    private ArrayList<Card> deck = new ArrayList();
    private int numOfCards = 0;
    
    public Deck(int maxSize){
        MAX_SIZE = maxSize;
    }
    
    public Deck(int maxSize, boolean fill){
        MAX_SIZE = maxSize;
        
        if (fill == true){
            fillDeck();
        }
    }
    
    public boolean fillDeck(){
        boolean ret = false;
        if (isEmpty() && MAX_SIZE == 32){
            for (Suit suit : Suit.values()){
                    for (Rank rank : Rank.values()){
                        deck.add(new Card(suit, rank));
                        numOfCards++;
                    }
                }
            Collections.shuffle(deck);
            ret = true;
        }
        return ret;
    }
    
    public Card getTopCard(){
        if (!isEmpty()){
            return deck.remove(--numOfCards);
        }
        return null;
    }
    
    public boolean addTopCard(Card card){
        if (!isFull()){
            deck.add(card);
            numOfCards++;
            return true;
        }
        return false;
    }
    
    public void shuffle(){
        int averageShuffle = numOfCards / 8;
        int numOfRounds = (new Random().nextInt(averageShuffle)) + 3;
        
        while (numOfRounds-- > 0){
            int numOfOverlays = (new Random().nextInt(averageShuffle)) + 3;
            int splits[] = new int[numOfOverlays];

            splits[0] = 0;
            for (int i = 1; i < numOfOverlays; i++){
                splits[i] = new Random().nextInt(numOfCards);
            }
            Arrays.sort(splits);

            ArrayList<Card> shuffledDeck = new ArrayList();
            int deckTop = deck.size();
            for (int i = numOfOverlays; i > 0; i--){       
                for (int j = splits[i-1]; j < deckTop; j++){
                    shuffledDeck.add(deck.get(j));
                }
                deckTop = splits[i-1];
            }
            deck = shuffledDeck;
        }
    }
    
    public int getSize(){
        return deck.size();
    }
    
    public boolean isEmpty(){
        return deck.isEmpty();
    }
    
    public boolean isFull(){
        return deck.size() == MAX_SIZE;
    }
}
