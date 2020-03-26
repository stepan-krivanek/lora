/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Rank;
import card_game.card.Suit;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author stepan
 */
public class GameView extends StackPane{
    
    private final double WIDTH = GameUtils.getScreenWidth();
    private final double HEIGHT = GameUtils.getScreenHeight();
    private final List<Suit> suits;
    private final List<Rank> ranks;
    private GridPane cards;
    private Game game;
    
    public GameView(Game game){
        this.game = game;
        suits = game.getOrderedSuits();
        ranks = game.getOrderedRanks();
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
    
    public void showTens(Player player){
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
        
        HandView primaryHand = player.getHandView();
        primaryHand.setAlignment(Pos.BOTTOM_CENTER);
        primaryHand.setPrefSize(WIDTH, HEIGHT);
        
        Rectangle playZone = new Rectangle(WIDTH, HEIGHT * 2 / 3);
        this.setAlignment(playZone, Pos.TOP_CENTER);
        playZone.setOpacity(0);
        
        double handShift = CARD_HEIGHT / 2;
        primaryHand.setTranslateY(handShift);
        playZone.setOnMouseEntered(e -> {
            primaryHand.setTranslateY(handShift);
        });
        playZone.setOnMouseExited(e -> {
            primaryHand.setTranslateY(0);
        });
        
        Button passButton = new Button("PASS");
        this.setAlignment(passButton, Pos.BOTTOM_RIGHT);
        passButton.setOnAction(e -> {
            player.playCard(null);
        });
        
        this.getChildren().addAll(cards, primaryHand, playZone, passButton);
    }
    
    public void showCard(Card card){
        GameMode gameMode = game.getGameMode();
        
        if (gameMode == GameMode.TENS){
            int suitIndex = suits.indexOf(card.getSuit());
            int rankIndex = ranks.indexOf(card.getRank());
            int cardIndex = suitIndex + rankIndex * suits.size();
            cards.getChildren().get(cardIndex).setOpacity(1);
        }
        else if(gameMode == GameMode.QUARTS) {
            
        }
        else {
            
        }
    }
    
    public void cardDealing(){
        
    }
    
    public void showWinner(Player player){
        Text winner = new Text("The winner is player number " + player.getId());
        Font font = new Font(40);
        winner.setFont(font);
        this.setAlignment(winner, Pos.TOP_CENTER);
        
        Rectangle rect = new Rectangle(WIDTH, HEIGHT);
        rect.setOpacity(0);
        this.setAlignment(rect, Pos.CENTER);
        
        getChildren().addAll(rect, winner);
    }
    
    private PerspectiveTransform perspection(double width, double height){
        final double hSegment = width / 5;
        final double vSegment = height / 3;
        
        PerspectiveTransform perspection = new PerspectiveTransform(
                hSegment, vSegment,
                width - hSegment, vSegment,
                width + (hSegment / 2), height,
                0 - (hSegment / 2), height
        );
        
        return perspection;
    }
}