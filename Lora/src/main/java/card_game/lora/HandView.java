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
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 *
 * @author stepa
 */
public class HandView extends GridPane{
    
    private final double CARD_WIDTH;
    private final double CARD_HEIGHT;
    private final double toRadians = (Math.PI / 180);
    
    public HandView(Player player, double width, double height){
        Deck deck = player.getHand();
        CARD_WIDTH = width / 10;
        CARD_HEIGHT = height / 3;
        
        setAlignment(Pos.BOTTOM_CENTER);
        setPrefWidth(width);
        setPrefHeight(height);
        setHgap(deck.size());
        setVgap(1);
        
        double cardShift = CARD_HEIGHT / 5;
        double overlay = CARD_WIDTH / 2;
        for (int i = 0; i < deck.size(); i++){
            Card card = deck.get(i);
            card.setFitWidth(CARD_WIDTH);
            card.setFitHeight(CARD_HEIGHT);
            card.setPreserveRatio(true);
            
            card.setOnMouseEntered(e -> {
                double radians = card.getRotate() * toRadians;
                card.setTranslateX(cardShift * Math.tan(radians));
                card.setTranslateY(card.getTranslateY() - cardShift);
            });
            
            card.setOnMouseExited(e -> {
                card.setTranslateX(0);
                card.setTranslateY(card.getTranslateY() + cardShift);
            });
            
            card.setOnMouseClicked(e -> {
                if (player.isPlaying() == true){
                    player.playCard(card);
                    this.getChildren().remove(card);
                    this.applyRotation();
                }
            });
            
            GridPane.setConstraints(card, i, 0);
            GridPane.setMargin(card, new Insets(0, -overlay, 0, 0));
            this.getChildren().add(card);
        }
        
        this.applyRotation();
    }
    
    private void applyRotation(){
        int numOfChildrens = this.getChildren().size();   
        int midChild = numOfChildrens / 2;
        
        if (numOfChildrens % 2 == 1){
            Node child = this.getChildren().get(midChild);
            child.setRotate(0);
            child.setTranslateY(0);
        }
        
        double rotation;
        double rotationShift;
        Node firstChild, secondChild;
        for (int i = 0; i < midChild; i++){
            rotation = (midChild - i) * 10; //in degrees
            rotationShift = (
                (1 - Math.sin(2.25 * (40 - rotation) * toRadians)) *
                (CARD_HEIGHT / 2)
            );
            
            firstChild = this.getChildren().get(i);
            secondChild = this.getChildren().get(numOfChildrens - 1 - i);
          
            firstChild.setRotate(-rotation);
            firstChild.setTranslateY(rotationShift);
            
            secondChild.setRotate(rotation);
            secondChild.setTranslateY(rotationShift);
        }
    }
    
    protected double getCardWidth(){
        return CARD_WIDTH;
    }
    
    protected double getCardHeight(){
        return CARD_HEIGHT;
    }
    
}
