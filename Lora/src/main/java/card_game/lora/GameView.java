/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.CardUtils;
import card_game.card.Rank;
import card_game.card.Suit;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author stepan
 */
public class GameView extends StackPane{
    
    private final double WIDTH = GameUtils.getScreenWidth();
    private final double HEIGHT = GameUtils.getScreenHeight();
    private GridPane cards;
    
    public GameView(){
        //Background bcgr = GameUtils.loadBackground(path, WIDTH, HEIGHT);
        Background bcgr = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        this.setBackground(bcgr);
        this.setHeight(HEIGHT);
        this.setWidth(WIDTH);

        Image table = new Image("/images/table2.png", WIDTH, HEIGHT, false, true);
        ImageView imageView = new ImageView(table);
        imageView.setEffect(perspection(WIDTH, HEIGHT));
        
        this.getChildren().addAll(imageView);
    }
    
    public void showTens(HandView primaryHand){
        final double CARD_WIDTH = WIDTH / 10;
        final double CARD_HEIGHT = HEIGHT / 5;
        final int NUM_OF_ROWS = 4;
        final int NUM_OF_COLS = 8;
        
        cards = new GridPane();
        cards.setAlignment(Pos.CENTER);
        cards.setPrefWidth(WIDTH);
        cards.setPrefHeight(HEIGHT);
        cards.setHgap(NUM_OF_COLS);
        cards.setVgap(NUM_OF_ROWS);

        ArrayList<Suit> suits = CardUtils.getOrderedSuits();
        ArrayList<Rank> ranks = CardUtils.getOrderedRanks();
        for (int col = 0; col < NUM_OF_COLS; col++){
            for (int row = 0; row < NUM_OF_ROWS; row++){
                Card card = new Card(suits.get(row), ranks.get(col));
                card.setOpacity(0.2);
                card.setFitWidth(CARD_WIDTH);
                card.setFitHeight(CARD_HEIGHT);
                card.setPreserveRatio(true);
                GridPane.setConstraints(card, col, row);
                GridPane.setMargin(card, new Insets(5, 10, 5 ,10));
                cards.getChildren().add(card);
            }
        }
        cards.setEffect(perspection(WIDTH, HEIGHT));
        
        primaryHand.setAlignment(Pos.BOTTOM_CENTER);
        primaryHand.setPrefSize(WIDTH, HEIGHT);
        this.getChildren().addAll(cards, primaryHand);
    }
    
    public void showCard(Card card){
        ArrayList<Suit> suits = CardUtils.getOrderedSuits();
        ArrayList<Rank> ranks = CardUtils.getOrderedRanks();
        int suitIndex = suits.indexOf(card.getSuit());
        int rankIndex = ranks.indexOf(card.getRank());
        int cardIndex = suitIndex + rankIndex * suits.size();
        cards.getChildren().get(cardIndex).setOpacity(1);
    }
    
    public void cardDealing(){
        
    }
    
    private PerspectiveTransform perspection(double width, double height){
        double hSegment = width / 5;
        double vSegment = height / 3;
        
        PerspectiveTransform perspection = new PerspectiveTransform(
                hSegment, vSegment,
                width - hSegment, vSegment,
                width + (hSegment / 2), height,
                0 - (hSegment / 2), height
        );
        
        return perspection;
    }
}