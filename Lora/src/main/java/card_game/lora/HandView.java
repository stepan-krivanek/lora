/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author stepa
 */
public class HandView extends GridPane{
    
    private final double CARD_WIDTH;
    private final double CARD_HEIGHT;
    
    public HandView(Deck deck, double width, double height){
        CARD_WIDTH = width / 10;
        CARD_HEIGHT = height / 3;
        
        setAlignment(Pos.BOTTOM_CENTER);
        setPrefWidth(width);
        setPrefHeight(height);
        setHgap(deck.size());
        setVgap(1);
        
        double overlay = CARD_WIDTH / 2;
        for (int i = 0; i < deck.size(); i++){
            Card card = deck.get(i);
            card.setFitWidth(CARD_WIDTH);
            card.setFitHeight(CARD_HEIGHT);
            card.setPreserveRatio(true);
            
            card.setOnMouseEntered(e -> {
                card.setTranslateY(-100);
            });
            card.setOnMouseExited(e -> {
                card.setTranslateY(0);
            });
            
            GridPane.setConstraints(card, i, 0);
            GridPane.setMargin(card, new Insets(0, -overlay, 0, 0));
            this.getChildren().add(card);
        }
    }
    
    protected double getCardWidth(){
        return CARD_WIDTH;
    }
    
    protected double getCardHeight(){
        return CARD_HEIGHT;
    }
    
}
