/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Rank;
import card_game.card.Suit;
import card_game.lora.game_modes.GameModes;
import card_game.net.Client;
import card_game.net.Server;
import java.util.List;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author stepan
 */
public class GameView extends StackPane{
    
    private final double WIDTH = GameUtils.getScreenWidth();
    private final double HEIGHT = GameUtils.getScreenHeight();
    private final double CARD_WIDTH = WIDTH / 10;
    private final double CARD_HEIGHT = HEIGHT / 5;
    private final List<Suit> suits = GameUtils.getOrderedSuits();
    private final List<Rank> ranks = GameUtils.getOrderedRanks();
    private final List<GameModes> gameModes = GameUtils.getOrderedGamemodes();
    private final LoadingScreen loadingScreen = new LoadingScreen();
    private final StackPane table = new StackPane();
    private final GameView gameView = this;
    private final Main program;
    private final MpPlayer player;
    private HandView primaryHand;
    private Rectangle playZone;
    
    public GameView(Main program, MpPlayer player){
        this.player = player;
        this.program = program;
        program.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                exit();
            }
        });
        
        //Background bcgr = GameUtils.loadBackground(path, WIDTH, HEIGHT);
        Background bcgr = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        this.setBackground(bcgr);
        this.setHeight(HEIGHT);
        this.setWidth(WIDTH);

        Background tableBcgr = new Background(new BackgroundImage(
                new Image("/images/table.png", WIDTH / 5, 0, true, true),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        ));
        table.setBackground(tableBcgr);
        table.setPrefSize(WIDTH, HEIGHT);
        table.setEffect(perspection(WIDTH, HEIGHT));
        this.setAlignment(table, Pos.CENTER);
        this.getChildren().add(table);
    }
    
    public void show(){
        program.getRoot().getChildren().add(this);
    }
    
    public void showHand(Player player){
        this.getChildren().removeAll(primaryHand, playZone);
        primaryHand = player.getHandView();
        
        playZone = new Rectangle(WIDTH, HEIGHT * 2 / 3);
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
        
        this.getChildren().addAll(primaryHand, playZone);
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
        final double hSegment = width / 6;
        final double vSegment = height / 5;
        
        PerspectiveTransform perspection = new PerspectiveTransform(
                hSegment, vSegment,
                width - hSegment, vSegment,
                width + (width / 10), height,
                0 - (width / 10), height
        );
        
        return perspection;
    }
    
    private void showPassButton(Player player){
        Button passButton = new Button("PASS");
        
        this.setAlignment(passButton, Pos.BOTTOM_RIGHT);
        passButton.setOnAction(e -> {
            player.playCard(null);
        });
        
        this.getChildren().add(passButton);
    }
    
    private class MinigameGUI {
        
        private StackPane discardLayout;
        
        private void show(){
            discardLayout = new StackPane();
            discardLayout.setPrefWidth(WIDTH);
            discardLayout.setPrefHeight(HEIGHT);
            discardLayout.setAlignment(Pos.CENTER);

            table.getChildren().add(discardLayout);
        }

        private void showCard(Card card){
            ImageView cardView = new ImageView(card.getFront());
            cardView.setFitWidth(CARD_WIDTH);
            cardView.setFitHeight(CARD_HEIGHT);
            cardView.setPreserveRatio(true);
            cardView.setRotate(new Random().nextInt(360));

            discardLayout.getChildren().add(cardView);
        }
        
        private void onDestroy(){
            table.getChildren().remove(discardLayout);
        }
    }
    
    public void showLoadingScreen(){
        loadingScreen.show();
    }
    
    public void hideLoadingScreen(){
        loadingScreen.hide();
    }
    
    private class LoadingScreen {
        
        private Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.CORAL);
        
        public void show(){
            program.getRoot().getChildren().add(rect);
        }

        public void hide(){
            program.getRoot().getChildren().remove(rect);
        }
    }
    
    private class TensGUI {
        
        private final int NUM_OF_ROWS = 4;
        private final int NUM_OF_COLS = 8;
        private final Button passButton = new Button("PASS");
        private final GridPane cards;
        
        public TensGUI(){
            Rank[] rankArr = new Rank[Rank.values().length];
            for (Rank rank : Rank.values()){
                rankArr[GameUtils.getRankValue(rank)] = rank;
            }
            
            cards = new GridPane();
            cards.setAlignment(Pos.CENTER);
            cards.setPrefWidth(WIDTH);
            cards.setPrefHeight(HEIGHT);
            cards.setHgap(NUM_OF_COLS);
            cards.setVgap(NUM_OF_ROWS);

            for (int col = 0; col < NUM_OF_COLS; col++){
                for (int row = 0; row < NUM_OF_ROWS; row++){
                    Card card = new Card(suits.get(row), ranks.get(col));

                    ImageView cardView = new ImageView(card.getFront());
                    cardView.setOpacity(0.2);

                    cardView.setFitWidth(CARD_WIDTH);
                    cardView.setFitHeight(CARD_HEIGHT);
                    cardView.setPreserveRatio(true);

                    GridPane.setConstraints(cardView, col, row);
                    GridPane.setMargin(cardView, new Insets(5, 10, 5 ,10));

                    cards.getChildren().add(cardView);
                }
            }
            
            passButton.setAlignment(Pos.BOTTOM_RIGHT);
            passButton.setOnAction(e -> {
                //game.getPlayer(0).playCard(null);//send null to server
            });
        }
        
        private void show(){  
            table.getChildren().add(cards);
            gameView.getChildren().add(passButton);
        }

        private void showCard(Card card){
            int suitIndex = suits.indexOf(card.getSuit());
            int rankIndex = ranks.indexOf(card.getRank());
            int cardIndex = suitIndex + rankIndex * suits.size();
            cards.getChildren().get(cardIndex).setOpacity(1);
        }
    }
    
    private class QuartsGUI {
        
        private final int HGAP = 4;
        private final int VGAP = 1;
        private final GridPane discardLayout;
        private Card leadCard;
        
        public QuartsGUI(){
            discardLayout = new GridPane();
            discardLayout.setVgap(VGAP);
            discardLayout.setHgap(HGAP);
            discardLayout.setPrefWidth(gameView.getWidth());
            discardLayout.setPrefHeight(gameView.getHeight());
            discardLayout.setAlignment(Pos.CENTER);
        }
        
        private void show(){
            table.getChildren().add(discardLayout);
        }

        private void showDeck(Card card1, Card card2){
            leadCard = card1;
            int value1 = ranks.indexOf(card1.getRank());
            int value2 = ranks.indexOf(card2.getRank());
            int range = value2 - value1 + 1;
            
            for (int i = 0; i < range; i++){
                Card card = new Card(card1.getSuit(), ranks.get(value1 + i));

                ImageView cardView = new ImageView(card.getFront());
                cardView.setOpacity(0.2);

                cardView.setFitWidth(CARD_WIDTH);
                cardView.setFitHeight(CARD_HEIGHT);
                cardView.setPreserveRatio(true);

                GridPane.setConstraints(cardView, i, 0);
                GridPane.setMargin(cardView, new Insets(5, 10, 5 ,10));

                discardLayout.getChildren().add(cardView);
            }
        }
        
        private void showCard(Card card){
            int index = getRankDiff(card);
            discardLayout.getChildren().get(index).setOpacity(1);
        }
        
        private int getRankDiff(Card card){
            int cardIndex = ranks.indexOf(card.getRank());
            int leadCardIndex = ranks.indexOf(leadCard.getRank());
            return cardIndex - leadCardIndex;
        }
    }
    
    private void exit(){
        program.getMainMenu().setVisible(true);
        this.setVisible(false);
        program.getRoot().getChildren().remove(this);
    }
}