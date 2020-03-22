/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.CardUtils;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.card.Suit;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
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
import javafx.stage.Screen;

/**
 *
 * @author stepan
 */
public class GameView extends Parent{
    
    private final double width = GameUtils.getScreenWidth();
    private final double height = GameUtils.getScreenHeight();
    private Button returnButton = new Button("Back");
    private StackPane gameView;
    
    public GameView(){
        gameView = new StackPane();
        //Background bcgr = GameUtils.loadBackground(path, width, height);
        Background bcgr = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        gameView.setBackground(bcgr);
        gameView.setPrefHeight(height);
        gameView.setPrefWidth(width);
        
        Image table = new Image("/images/table2.png", width, height, false, true);
        ImageView imageView = new ImageView(table);
        imageView.setEffect(perspection(width, height));
        
        gameView.getChildren().addAll(imageView);
        
        StackPane root = new StackPane(gameView, returnButton);
        root.setAlignment(returnButton, Pos.TOP_RIGHT);
        getChildren().add(root);
    }
    
    public void playLora(GameMenu menu){
        setReturnButtonAction(menu);
        this.setVisible(true);
        menu.setVisible(false);
        
        Game game = new Game(4, 32);
        
        playTens(game);
        
        
    }
    
    private void playTens(Game game){
        cardDealing(game);
        
        int numOfRows = 4;
        int numOfCols = 8;
        
        GridPane cards = new GridPane();
        cards.setAlignment(Pos.CENTER);
        cards.setPrefWidth(width);
        cards.setPrefHeight(height);
        cards.setHgap(numOfCols);
        cards.setVgap(numOfRows);

        Suit[] suits = CardUtils.getOrderedSuits();
        Rank[] ranks = CardUtils.getOrderedRanks();
        for (int col = 0; col < numOfCols; col++){
            for (int row = 0; row < numOfRows; row++){
                Card card = new Card(suits[row], ranks[col]);
                card.setOpacity(0.2);
                card.setFitWidth(width / 10);
                card.setFitHeight(height / 5);
                card.setPreserveRatio(true);
                GridPane.setConstraints(card, col, row);
                GridPane.setMargin(card, new Insets(5, 10, 5 ,10));
                cards.getChildren().add(card);
            }
        }
        cards.setEffect(perspection(width, height));
        
        HandView primaryHand = new HandView(
                game.getPlayer(0).getHand(),
                width,
                height
        );
        primaryHand.setAlignment(Pos.BOTTOM_CENTER);
        
        gameView.getChildren().addAll(cards, primaryHand);
    }
    
    private void setReturnButtonAction(GameMenu menu){
        returnButton.setOnAction(e -> {
            menu.setVisible(true);
            this.setVisible(false);
         });
    }
    
    private void cardDealing(Game game){
        game.getMainDeck().shuffle();
        
        final int cardsToDeal = 2;
        Player player = game.getFirstPlayer();
        Deck mainDeck = game.getMainDeck();
        Card card;
        while (game.getMainDeck().isEmpty() == false){
            for (int i = 0; i < cardsToDeal; i++){
                card = mainDeck.removeTopCard();
                player.getHand().addTopCard(card);
            }
            
            player = game.nextPlayer(player);
        }
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