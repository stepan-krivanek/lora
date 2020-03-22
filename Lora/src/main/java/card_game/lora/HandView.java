/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 *
 * @author stepa
 */
public class HandView extends GridPane{
    
    public HandView(Deck deck, double width, double height){
        setAlignment(Pos.BOTTOM_CENTER);
        //setTranslateY();
        setPrefWidth(width);
        setPrefHeight(height);
        setHgap(deck.size());
        setVgap(1);
        
        for (int i = 0; i < deck.size(); i++){
            Card card = deck.get(i);
            card.setFitWidth(width / 10);
            card.setFitHeight(height / 3);
            card.setPreserveRatio(true);
            GridPane.setConstraints(card, i, 0);
            getChildren().add(card);
        }
    }
    
    public void removeCard(int index){
        
    }
    
}
