/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author stepa
 */
public class HandView extends GridPane{
    
    private final double CARD_WIDTH;
    private final double CARD_HEIGHT;
    private final double toRadians = (Math.PI / 180);
    private ArrayList<CardView> cards;
    
    public HandView(MpPlayer player, double width, double height){
        Deck deck = player.getHand();
        CARD_WIDTH = width / 10;
        CARD_HEIGHT = height / 3;
        
        setAlignment(Pos.BOTTOM_CENTER);
        setPrefWidth(width);
        setPrefHeight(height);
        setHgap(deck.size());
        setVgap(1);
        
        final double overlay = CARD_WIDTH / 2;
        cards = new ArrayList<>();
        
        for (int i = 0; i < deck.size(); i++){
            Card card = deck.get(i);
            CardView cardView = new CardView(card.getFront());
            
            cardView.setOnMouseClicked(e -> {
                if (player.isPlaying()){
                    player.playCard(card);
                }
            });
            
            GridPane.setConstraints(cardView, i, 0);
            GridPane.setMargin(cardView, new Insets(0, -overlay, 0, 0));
            this.getChildren().add(cardView);
            
            cards.add(i, cardView);
        }
        
        this.applyRotation();
    }
    
    public boolean glowCard(int index, Color color){
        if (index >= cards.size() || index < 0){
            return false;
        }
        
        cards.get(index).getBorderGlow().setColor(color);
        return true;
    }
    
    public boolean removeCard(int index){
        if (index >= cards.size() || index < 0){
            return false;
        }
        
        cards.remove(index);
        getChildren().remove(index);
        applyRotation();
        return true;
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
    
    public class CardView extends ImageView {
        
        private final double cardShift = CARD_HEIGHT / 5;
        private final DropShadow borderGlow = new DropShadow();
        
        public CardView(Image image){
            super(image);
            
            setFitWidth(CARD_WIDTH);
            setFitHeight(CARD_HEIGHT);
            setPreserveRatio(true);
            
            borderGlow.setRadius(CARD_WIDTH / 2);
            borderGlow.setSpread(0.2);
            
            setOnMouseEntered(e -> {
                double radians = getRotate() * toRadians;
                setTranslateX(cardShift * Math.tan(radians));
                setTranslateY(getTranslateY() - cardShift);
                
                borderGlow.setColor(Color.WHEAT);
                setEffect(borderGlow);
            });
            
            setOnMouseExited(e -> {
                setTranslateX(0);
                setTranslateY(getTranslateY() + cardShift);
                
                setEffect(null);
            });
        }
        
        public DropShadow getBorderGlow(){
            return borderGlow;
        }
    }
    
    public double getCardWidth(){
        return CARD_WIDTH;
    }
    
    public double getCardHeight(){
        return CARD_HEIGHT;
    }
    
}
