/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Deck;
import card_game.card.Rank;
import card_game.card.Suit;
import card_game.lora.game_modes.GameModes;
import java.util.List;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
    
    private final int MAX_CARDS = 32;
    private final int NUM_OF_PLAYERS = 4;
    private final double WIDTH;// = GameUtils.getScreenWidth();
    private final double HEIGHT;// = GameUtils.getScreenHeight();
    private final double CARD_WIDTH;// = WIDTH / 10;
    private final double CARD_HEIGHT;// = HEIGHT / 5;
    private final List<Suit> suits = GameUtils.getOrderedSuits();
    private final List<Rank> ranks = GameUtils.getOrderedRanks();
    private final List<GameModes> gameModes = GameUtils.getOrderedGamemodes();
    private final LoadingScreen loadingScreen = new LoadingScreen();
    private final StackPane table = new StackPane();
    private final HandView[] otherHands = new HandView[NUM_OF_PLAYERS - 1];
    private final Main program;
    private final MpPlayer player;
    
    private TableGUI tableGUI = null;
    private HandView primaryHand;
    private Rectangle playZone;
    
    public GameView(Main program, MpPlayer player){
        ///For testing
        
        WIDTH = program.getStage().getWidth();
        HEIGHT = program.getStage().getHeight();
        CARD_WIDTH = WIDTH / 10;
        CARD_HEIGHT = HEIGHT / 5;

        ///
        
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
        GameView.setAlignment(table, Pos.CENTER);
        this.getChildren().add(table);
        
        loadingScreen.show();
    }
    
    public void show(){
        loadingScreen.hide();
        program.getRoot().getChildren().add(this);
    }
    
    public void showHands(){
        //Other hands
        int handSize = MAX_CARDS / NUM_OF_PLAYERS;
        for (int i = 0; i < NUM_OF_PLAYERS - 1; i++){
            otherHands[i] = new HandView(handSize, WIDTH, HEIGHT);
        }
        
        double newCardHeight = otherHands[0].getCardHeight() * 50 / 23;
        double rightX, leftX, topY, botY;
        rightX = WIDTH / 6;
        leftX = - WIDTH / 10;
        botY = HEIGHT;
        topY = HEIGHT / 4;
        PerspectiveTransform leftHand = new PerspectiveTransform(
                leftX, botY - newCardHeight,
                rightX, topY - newCardHeight,
                rightX, topY,
                leftX, botY
        );
        
        PerspectiveTransform rightHand = new PerspectiveTransform(
                WIDTH - rightX, topY - newCardHeight,
                WIDTH - leftX, botY - newCardHeight,
                WIDTH - leftX, botY,
                WIDTH - rightX, topY
        );
        
        newCardHeight = otherHands[0].getCardHeight() * 5 / 4;
        rightX = WIDTH/2 + (otherHands[1].getCardWidth() * 8 * 5 / 9 / 2);   //(WIDTH / 2) + (WIDTH * 2 / 9);
        leftX = WIDTH/2 - otherHands[1].getCardWidth() * 8 * 5 / 9 / 2;    //(WIDTH / 2) - (WIDTH * 2 / 9);
        botY = HEIGHT / 4;
        topY = botY - newCardHeight;
        PerspectiveTransform topHand = new PerspectiveTransform(
                leftX, topY,
                rightX, topY,
                rightX, botY,
                leftX, botY
        );
        
        otherHands[0].setEffect(leftHand);
        otherHands[1].setEffect(topHand);
        otherHands[2].setEffect(rightHand);
        
        for (HandView hand : otherHands){
            this.getChildren().remove(hand);
            this.getChildren().add(hand);
        }
        
        //Primary hand
        primaryHand = new HandView(player, WIDTH, HEIGHT);
        
        playZone = new Rectangle(WIDTH, HEIGHT * 2 / 3);
        GameView.setAlignment(playZone, Pos.TOP_CENTER);
        playZone.setOpacity(0);
        
        double handShift = CARD_HEIGHT / 2;
        primaryHand.setTranslateY(handShift);
        playZone.setOnMouseEntered(e -> {
            primaryHand.setTranslateY(handShift);
        });
        playZone.setOnMouseExited(e -> {
            primaryHand.setTranslateY(0);
        });
        
        this.getChildren().removeAll(primaryHand, playZone);
        this.getChildren().addAll(primaryHand, playZone);
    }
    
    public void updateScore(int[] score){
        
    }
    
    public void showGameModeSelection(MpPlayer player){
        primaryHand.setTranslateY(CARD_HEIGHT / 2);
        Rectangle rect = new Rectangle(WIDTH, HEIGHT);
        rect.setOpacity(0);
        //cards are too low
        Button quarts = new Button("Quarts");
        quarts.setAlignment(Pos.CENTER);
        quarts.setOnAction(e -> {
            primaryHand.setTranslateY(0);
            player.chooseGameMode(GameModes.QUARTS.ordinal());
            this.getChildren().removeAll(rect, quarts);
        });
        
        this.getChildren().addAll(rect, quarts);
    }
    
    public void cardDealing(){
        
    }
    
    public void showCard(Card card, int playerId){
        if (playerId != player.getId()){
            int idx = playerId + NUM_OF_PLAYERS - player.getId();
            idx = idx % NUM_OF_PLAYERS - 1;
            otherHands[idx].removeCard(0);
        }
        
        tableGUI.showCard(card, playerId);
    }
    
    public void setGameMode(int id){
        if (tableGUI != null){
            tableGUI.hide();
        }
        
        switch (gameModes.get(id)){
            case TENS:
                tableGUI = new TensGUI();
                break;
                
            case QUARTS:
                tableGUI = new QuartsGUI();
                break;
                
            default:
                tableGUI = new MinigameGUI();
                break;
        }
        
        tableGUI.show();
    }
    
    public void showWinner(int id){
        Text winner = new Text("The winner is player number " + id);
        Font font = new Font(40);
        winner.setFont(font);
        GameView.setAlignment(winner, Pos.TOP_CENTER);
        
        Rectangle rect = new Rectangle(WIDTH, HEIGHT);
        rect.setOpacity(0);
        
        getChildren().addAll(rect, winner);
    }
    
    public void exit(){
        program.getMainMenu().setVisible(true);
        this.setVisible(false);
        program.getRoot().getChildren().remove(this);
    }
    
    public HandView getHandView(){
        return primaryHand;
    }
    
    private PerspectiveTransform perspection(double width, double height){
        final double hSegment = width / 6;
        final double vSegment = height / 4;
        
        PerspectiveTransform perspection = new PerspectiveTransform(
                hSegment, vSegment,
                width - hSegment, vSegment,
                width + (width / 10), height,
                0 - (width / 10), height
        );
        
        return perspection;
    }
    
    private class LoadingScreen {
        
        private final Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.CORAL);
        
        public void show(){
            program.getRoot().getChildren().add(rect);
        }

        public void hide(){
            program.getRoot().getChildren().remove(rect);
        }
    }
    
    private interface TableGUI {
        
        public void show();
        
        public void showCard(Card card, int playerId);
        
        public void hide();
    }
    
    private class MinigameGUI implements TableGUI{
        
        private final int ANGLE = 70;
        private StackPane discardLayout;
        private int cardsPlayed = 0;
        
        @Override
        public void show(){
            discardLayout = new StackPane();
            discardLayout.setPrefWidth(WIDTH);
            discardLayout.setPrefHeight(HEIGHT);
            discardLayout.setAlignment(Pos.CENTER);

            table.getChildren().add(discardLayout);
        }

        @Override
        public void showCard(Card card, int playerId){
            if (++cardsPlayed > 4){
                discardLayout.getChildren().clear();
                cardsPlayed = 1;
            }
            
            int id = playerId - player.getId();
            int rotation = new Random().nextInt(ANGLE) - (ANGLE / 2) + (id * 90);
            
            ImageView cardView = new ImageView(card.getFront());
            cardView.setFitWidth(CARD_WIDTH);
            cardView.setFitHeight(CARD_HEIGHT);
            cardView.setPreserveRatio(true);
            cardView.setRotate(rotation);
            
            discardLayout.getChildren().add(cardView);
        }
        
        @Override
        public void hide(){
            table.getChildren().remove(discardLayout);
        }
    }
    
    private class TensGUI implements TableGUI{
        
        private final int NUM_OF_ROWS = 4;
        private final int NUM_OF_COLS = 8;
        private final Button passButton = new Button("PASS");
        private final GridPane cards;
        
        public TensGUI(){
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
            
            setAlignment(passButton, Pos.BOTTOM_RIGHT);
            passButton.setOnMouseClicked(e -> {
                player.pass();
            });
        }
        
        @Override
        public void show(){  
            table.getChildren().add(cards);
            getChildren().add(passButton);
        }

        @Override
        public void showCard(Card card, int playerId){
            int suitIndex = suits.indexOf(card.getSuit());
            int rankIndex = ranks.indexOf(card.getRank());
            int cardIndex = suitIndex + rankIndex * suits.size();
            cards.getChildren().get(cardIndex).setOpacity(1);
        }
        
        @Override
        public void hide(){
            table.getChildren().remove(cards);
            getChildren().remove(passButton);
        }
    }
    
    private class QuartsGUI implements TableGUI{
        
        private final int DISCARD_DECK_SIZE = 4;
        private final int HGAP = 4;
        private final int VGAP = 1;
        private final GridPane discardLayout;
        private final Deck discardDeck = new Deck(4);
        
        private Card leadCard;
        private int cardsPlayed = 0;
        private int cardsToPlay = 0;
        
        public QuartsGUI(){
            discardLayout = new GridPane();
            discardLayout.setVgap(VGAP);
            discardLayout.setHgap(HGAP);
            discardLayout.setPrefWidth(getWidth());
            discardLayout.setPrefHeight(getHeight());
            discardLayout.setAlignment(Pos.CENTER);
        }
        
        @Override
        public void show(){
            table.getChildren().add(discardLayout);
        }

        @Override
        public void showCard(Card card, int playerId){
            if (++cardsPlayed > cardsToPlay){
                cardsPlayed = 1;
                showDeck(card);
            }
            
            int index = getRankDiff(card);
            discardLayout.getChildren().get(index).setOpacity(1);
        }
        
        @Override
        public void hide(){
            table.getChildren().remove(discardLayout);
        }
        
        private void showDeck(Card card){
            discardLayout.getChildren().clear();
            cardsToPlay = 0;
            leadCard = card;
            
            int value = ranks.indexOf(card.getRank());
            for (int i = 0; i < DISCARD_DECK_SIZE; i++){
                
                int index = value + i;
                if (index >= ranks.size()){
                    break;
                }
                
                Card tmp = new Card(card.getSuit(), ranks.get(index));
                if(discardDeck.contains(tmp)){
                    break;
                }
                discardDeck.add(tmp);
                cardsToPlay += 1;
                
                ImageView cardView = new ImageView(tmp.getFront());
                cardView.setOpacity(0.2);

                cardView.setFitWidth(CARD_WIDTH);
                cardView.setFitHeight(CARD_HEIGHT);
                cardView.setPreserveRatio(true);

                GridPane.setConstraints(cardView, i, 0);
                GridPane.setMargin(cardView, new Insets(5, 10, 5 ,10));

                discardLayout.getChildren().add(cardView);
            }
        }
        
        private int getRankDiff(Card card){
            int cardIndex = ranks.indexOf(card.getRank());
            int leadCardIndex = ranks.indexOf(leadCard.getRank());
            return cardIndex - leadCardIndex;
        }
    }
}