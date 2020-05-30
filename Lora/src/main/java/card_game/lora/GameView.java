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
import card_game.net.ClientConnection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

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
    private final LoadingScreen loadingScreen;
    private final StackPane hands;
    private final StackPane playGround = new StackPane();
    private final StackPane table;
    private final Text modeText, playerScore;
    private final Text[] score;
    private final HandView[] otherHands = new HandView[NUM_OF_PLAYERS - 1];
    private final Main program;
    private final MpPlayer player;
    
    private boolean saved = false;
    private boolean soundOn = true;
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
        
        program.getStage().setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        
        loadingScreen = new LoadingScreen(WIDTH, HEIGHT);
        //-----------------------Background-------------------------
        Background bcgr = GameUtils.loadBackground(
                "/images/game_bcgr.png", WIDTH, HEIGHT
        );
        this.setBackground(bcgr);
        this.setHeight(HEIGHT);
        this.setWidth(WIDTH);
        
        //--------------------------Hands---------------------------
        hands = new StackPane();
        hands.setMinSize(WIDTH, HEIGHT);
        hands.setMaxSize(WIDTH, HEIGHT);
        
        ////////////////////////////////////////////////////////////
        //--------------------------Table-------------------------//
        ////////////////////////////////////////////////////////////
        
        //--------------------------Score---------------------------
        playerScore = new Text("123");
        playerScore.setFill(Design.ORANGE);
        playerScore.setFont(Design.Font(HEIGHT / 20));
        playerScore.setBoundsType(TextBoundsType.VISUAL);
        
        score = new Text[NUM_OF_PLAYERS];
        for (Text text : score){
            text = new Text("0");
            text.setFont(Design.Font(HEIGHT / 10));
        }
        
        //--------------------------Table---------------------------
        table = new StackPane();
        Background tableBcgr = new Background(new BackgroundImage(
                new Image("/images/table2.png", WIDTH / 5, 0, true, true),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        ));
        table.setBackground(tableBcgr);
        table.setPrefSize(WIDTH, HEIGHT);
        table.setEffect(perspection(WIDTH, HEIGHT));
        GameView.setAlignment(table, Pos.CENTER);
        
        //----------------------Personal info-----------------------
        //BETA
        Text name = new Text("Ututu halabala");
        name.setFont(Design.Font(HEIGHT / 15));
        
        ImageView goldView = new ImageView("/images/gold.png");
        goldView.setFitWidth(WIDTH / 15);
        goldView.setFitHeight(HEIGHT / 10);
        goldView.setPreserveRatio(true);
        StackPane gold = new StackPane(goldView, playerScore);
        gold.setMinSize(WIDTH / 15, HEIGHT / 10);
        gold.setMaxSize(WIDTH / 15, HEIGHT / 10);
        
        HBox personalBox = new HBox(gold, name);
        personalBox.setSpacing(WIDTH / 100);
        personalBox.setAlignment(Pos.BOTTOM_LEFT);
        personalBox.setPadding(new Insets(10, WIDTH / 100, 10, WIDTH / 100));
        
        /////////////////////////////////////////////////////////////
        //--------------------------Top bar------------------------//
        /////////////////////////////////////////////////////////////
        
        //-------------------------Game mode-------------------------
        modeText = new Text(GameModes.REDS.toString());
        modeText.setFont(Design.Font(HEIGHT / 10));
        
        HBox modeBox = new HBox(modeText);
        modeBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(modeBox, Priority.ALWAYS);
        
        //------------------------Pause button-----------------------
        Rectangle pause = new Rectangle(WIDTH / 15, HEIGHT / 10);
        pause.setFill(fillRect("/images/icons/pause_grey.png"));
        pause.setOnMouseEntered(e -> {
            pause.setFill(fillRect("/images/icons/pause_orange.png"));
        });
        pause.setOnMouseExited(e -> {
            pause.setFill(fillRect("/images/icons/pause_grey.png"));
        });
        pause.setOnMouseClicked(e -> {
            showMenu();
        });
        
        //------------------------Volume button----------------------
        Rectangle volume = new Rectangle(WIDTH / 15, HEIGHT / 10);
        volume.setFill(fillRect("/images/icons/sound_on_grey.png"));
        volume.setOnMouseEntered(e -> {
            if (soundOn){
                volume.setFill(fillRect("/images/icons/sound_on_orange.png"));
            } else {
                volume.setFill(fillRect("/images/icons/sound_off_orange.png"));
            }
        });
        volume.setOnMouseExited(e -> {
            if (soundOn){
                volume.setFill(fillRect("/images/icons/sound_on_grey.png"));
            } else {
                volume.setFill(fillRect("/images/icons/sound_off_grey.png"));
            }
        });
        volume.setOnMouseClicked(e -> {
            soundOn = !soundOn;
            if (soundOn){
                volume.setFill(fillRect("/images/icons/sound_on_orange.png"));
            } else {
                volume.setFill(fillRect("/images/icons/sound_off_orange.png"));
            }
        });
        
        HBox pauseBox = new HBox(volume, pause);
        pauseBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(pauseBox, Priority.ALWAYS);

        //---------------------------Top bar--------------------------
        HBox topBar = new HBox(modeBox, pauseBox);
        topBar.setPadding(new Insets(10, WIDTH / 100, 10, WIDTH / 100));
        
        //////////////////////////////////////////////////////////////
        //-------------------------Border pane----------------------//
        //////////////////////////////////////////////////////////////
        BorderPane border = new BorderPane();
        // The order matters!
        border.setCenter(playGround);
        border.setTop(topBar);
        
        this.getChildren().addAll(hands, table, personalBox, border);
        loadingScreen.show();
    }
    
    private void closeProgram(){
        String msg = "Do you really want to exit?";
        msg = saved ? msg : msg + "\nThe progress will be lost!";
        if (ExitAlertBox.display(program, msg)){
            player.disconnect();
            program.close();
        }
    }
    
    private Paint fillRect(String path){
        return new ImagePattern(new Image(path));
    }
    
    public void show(){
        loadingScreen.hide();
        program.getRoot().getChildren().add(this);
    }
    
    public void showHands(){
        playGround.getChildren().removeAll(primaryHand, playZone);
        hands.getChildren().clear();
        if (tableGUI != null){
            tableGUI.hide();
        }
        
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
        rightX = (WIDTH / 2) + (WIDTH * 2 / 9);
        leftX = (WIDTH / 2) - (WIDTH * 2 / 9);
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
            hands.getChildren().add(hand);
        }
        
        //Primary hand
        primaryHand = new HandView(player, WIDTH, HEIGHT);
        
        playZone = new Rectangle(WIDTH, HEIGHT / 2);
        GameView.setAlignment(playZone, Pos.TOP_CENTER);
        playZone.setOpacity(0);
        
        playZone.setOnMouseEntered(e -> {
            primaryHand.setTranslateY(CARD_HEIGHT / 2);
        });
        playZone.setOnMouseExited(e -> {
            primaryHand.setTranslateY(0);
        });
        
        playGround.getChildren().addAll(primaryHand, playZone);
    }
    
    public void updateScore(int[] score){
        
    }
    
    public void showGameModeSelection(MpPlayer player){
        Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.GREY);
        rect.setOpacity(0.2);
        
        ModesView modesBox = new ModesView(playGround.getHeight());
        
        GameModes[] modes = GameModes.values();
        for (int i = 0; i < modes.length; i++){
            final int num = i;
            modesBox.getModeBox(modes[i]).setOnMouseClicked(e -> {
                player.chooseGameMode(num);
                this.getChildren().removeAll(rect, modesBox);
            });
        }
        
        this.getChildren().addAll(rect, modesBox);
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
        modeText.setText(gameModes.get(id).toString());
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
    
    public void connectionLost(int playerId){
        Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.GREY);
        rect.setOpacity(0.2);
        
        String msg = "Player " + playerId + " has left the game, connection Lost!";
        Text text = new Text(msg);
        text.setFont(Design.Font(40));
        
        ToggleButton exitButton = new Design.Button(WIDTH / 5, HEIGHT / 10);
        exitButton.setText("Exit");
        exitButton.setOnAction(e -> {
            exit();
        });
        
        VBox alertBox = new VBox(text, exitButton);
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setSpacing(HEIGHT / 5);
        
        Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, msg);
        this.getChildren().addAll(rect, alertBox);
    }
    
    public void exit(){
        player.disconnect();
        program.setMainMenu();
        program.getRoot().getChildren().remove(this);
        System.gc();
    }
    
    public HandView getHandView(){
        return primaryHand;
    }
    
    private void showExitAlert(){
        Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.GREY);
        rect.setOpacity(0.2);
        
        final double alertHeight = HEIGHT / 4;
        final double alertWidth = WIDTH / 2;
        
        VBox alertBox = new VBox();
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setMinSize(alertWidth, alertHeight);
        alertBox.setMaxSize(alertWidth, alertHeight);
        alertBox.setBackground(GameUtils.loadBackground(
                "images/menu_bcgr.png", alertWidth, alertHeight
        ));
        alertBox.setSpacing(alertHeight / 6);
        
        Text alert = new Text("Do you really want to exit without saving?");
        alert.setFont(Design.Font(HEIGHT / 25));

        ToggleButton yes = new Design.Button(WIDTH / 10, HEIGHT / 20);
        yes.setText("Yes");
        yes.setOnAction(e -> {
            exit();
        });

        ToggleButton no = new Design.Button(WIDTH / 10, HEIGHT / 20);
        no.setText("No");
        no.setOnAction(e -> {
            this.getChildren().removeAll(rect, alertBox);
            showMenu();
        });

        HBox alertButtons = new HBox(yes, no);
        alertButtons.setAlignment(Pos.CENTER);
        alertButtons.setSpacing(alertWidth / 6);
        
        alertBox.getChildren().addAll(alert, alertButtons);
        this.getChildren().addAll(rect, alertBox);
    }
    
    private void showMenu(){
        Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.GREY);
        rect.setOpacity(0.2);
        
        final double menuHeight = HEIGHT / 2;
        final double menuWidth = WIDTH / 4;
        final double spacing = menuHeight / 10;
        
        VBox menu = new VBox();
        menu.setSpacing(spacing);
        menu.setMinSize(menuWidth, menuHeight);
        menu.setMaxSize(menuWidth, menuHeight);
        menu.setBackground(GameUtils.loadBackground(
                "images/menu_bcgr.png", menuWidth, menuHeight
        ));
        menu.setAlignment(Pos.CENTER);
        
        final double buttonWidth = menuWidth * 2 / 3;
        final double buttonHeight = menuHeight / 10;
        
        //Rules button
        ToggleButton rulesButton = new Design.Button(buttonWidth, buttonHeight);
        rulesButton.setText("Rules");
        rulesButton.setOnAction(e -> {
            //showRules();
        });
        
        //Exit button
        ToggleButton exitButton = new Design.Button(buttonWidth, buttonHeight);
        exitButton.setText("Exit");
        exitButton.setOnAction(e -> {
            if (saved){
                exit();
            } else {
                this.getChildren().removeAll(rect, menu);
                showExitAlert();
            }
        });
        
        //Save button
        ToggleButton saveButton = new Design.Button(buttonWidth, buttonHeight);
        saveButton.setText("Save");
        saveButton.setOnAction(e -> {
            saved = true;
        });
        
        //Play button
        ToggleButton playButton = new Design.Button(buttonWidth, buttonHeight);
        playButton.setText("Continue");
        playButton.setOnAction(e -> {
            this.getChildren().removeAll(rect, menu);
        });
        
        menu.getChildren().addAll(exitButton, saveButton, playButton);
        this.getChildren().addAll(rect, menu);
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
        
        private final StackPane loadingScreen;
        
        public LoadingScreen(double width, double height){
            Text text = new Text("Waiting for other players to connect");
            text.setFont(Design.Font(height / 15));
            
            loadingScreen = new StackPane(text);
            loadingScreen.setAlignment(Pos.CENTER);
            loadingScreen.setMinSize(width, height);
            loadingScreen.setMaxSize(width, height);
            loadingScreen.setBackground(GameUtils.loadBackground(
                    "/images/loadingscreen.png", width, height)
            );
        }
        
        public void show(){
            program.getRoot().getChildren().add(loadingScreen);
        }

        public void hide(){
            program.getRoot().getChildren().remove(loadingScreen);
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