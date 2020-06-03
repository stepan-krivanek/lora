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
import card_game.lora.game_modes.GameMode;
import card_game.net.ClientConnection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import static javafx.scene.layout.StackPane.setAlignment;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 *  The view of the game Lóra.
 * 
 * @author Štěpán Křivánek
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
    private final List<GameMode> gameModes = GameUtils.getOrderedGamemodes();
    private final LoadingScreen loadingScreen;
    private final StackPane hands;
    private final StackPane playGround = new StackPane();
    private final StackPane table;
    private final StackPane privatePane = new StackPane();
    private final Text[] names = new Text[NUM_OF_PLAYERS];
    private final Text[] scoreTexts = new Text[NUM_OF_PLAYERS];
    private final Text modeText, roundText;
    private final HandView[] otherHands = new HandView[NUM_OF_PLAYERS - 1];
    private final Main program;
    private final MpPlayer player;
    
    private int[] score = {100, 100, 100, 100};
    private boolean saved = false;
    private boolean soundOn = true;
    private TableGUI tableGUI = null;
    private HandView primaryHand;
    private Rectangle playZone;
    private GameMode gameMode = GameMode.REDS;
    private int round = 0;
    
    /**
     * Creates a new game view for the specified player.
     * 
     * @param program Application with this view
     * @param player Player for whom the game view is
     */
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
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            scoreTexts[i] = new Text(Integer.toString(score[i]));
            scoreTexts[i].setFont(Design.Font(HEIGHT / 20));
            scoreTexts[i].setFill(Design.ORANGE);
            scoreTexts[i].setBoundsType(TextBoundsType.VISUAL);
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
        table.setMinSize(WIDTH, HEIGHT);
        table.setMaxSize(WIDTH, HEIGHT);
        table.setEffect(perspection(WIDTH, HEIGHT));
        GameView.setAlignment(table, Pos.CENTER);
        
        /////////////////////////////////////////////////////////////
        //--------------------------Top bar------------------------//
        /////////////////////////////////////////////////////////////
        
        //---------------------------Round---------------------------
        roundText = new Text("1");
        roundText.setFont(Design.Font(HEIGHT / 10));
        
        //-------------------------Game mode-------------------------
        modeText = new Text(GameMode.REDS.toString());
        modeText.setFont(Design.Font(HEIGHT / 10));
        
        HBox modeBox = new HBox(modeText, roundText);
        modeBox.setSpacing(WIDTH / 100);
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
            Design.playClick();
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
            Design.playClick();
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
        
        this.getChildren().addAll(hands, table, privatePane, border);
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
    
    /**
     * Shows the game view.
     */
    public void show(){
        loadingScreen.hide();
        program.getRoot().getChildren().add(this);
    }
    
    /**
     * Shows a new round.
     * 
     * @param round Number of the round.
     */
    public void newRound(int round){
        roundText.setText(Integer.toString(round + 1));
        this.round = round;
    }
    
    /**
     * Shows hands of all players.
     */
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
        
        double vectorX = WIDTH * 4 / 15;
        double vectorY = HEIGHT * -3 / 4;
        double shiftFactor = 10;
        double newCardHeight = otherHands[0].getCardHeight() * 50 / 23;
        double rightX, leftX, topY, botY;
        rightX = WIDTH / 6 + (vectorX / shiftFactor);
        leftX = - WIDTH / 10 + (vectorX / shiftFactor);
        botY = HEIGHT + (vectorY / shiftFactor);
        topY = HEIGHT / 4 + (vectorY / shiftFactor);
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
    
    /**
     * Updates the score of the game.
     * 
     * @param score New score
     */
    public void updateScore(int[] score){
        this.score = score;
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            this.scoreTexts[i].setText(Integer.toString(score[i]));
        }
    }
    
    /**
     * Show the game mode selection to the player.
     * Used before graduation.
     */
    public void showGameModeSelection(){
        Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.GREY);
        rect.setOpacity(0.2);
        
        ModesView modesBox = new ModesView(playGround.getHeight());
        
        GameMode[] modes = GameMode.values();
        for (int i = 0; i < modes.length; i++){
            final int num = i;
            modesBox.getModeBox(modes[i]).setOnMouseClicked(e -> {
                Design.playClick();
                player.chooseGameMode(num);
                this.getChildren().removeAll(rect, modesBox);
            });
        }
        
        this.getChildren().addAll(rect, modesBox);
    }
    
    /**
     * Shows the animation of card dealing (Not implemented yet).
     */
    public void cardDealing(){
        // Not implemented yet
    }
    
    /**
     * Shows the card played;
     * 
     * @param card The card played
     * @param playerId Player, who played the card
     */
    public void showCard(Card card, int playerId){
        if (soundOn){
            Design.playCardPlay();
        }
        
        if (playerId != player.getId()){
            int idx = playerId + NUM_OF_PLAYERS - player.getId();
            idx = idx % NUM_OF_PLAYERS - 1;
            otherHands[idx].removeCard(0);
        }
        
        tableGUI.showCard(card, playerId);
    }
    
    /**
     * Sets a new game mode.
     * 
     * @param id Id of teh game mode
     */
    public void setGameMode(int id){
        gameMode = gameModes.get(id);
        saved = false;
        
        switch (gameMode){
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
    
    /**
     * Shows that connection was lost and the player must quit the game.
     * 
     * @param playerId Player who has been disconnected from the game.
     */
    public void connectionLost(int playerId){
        Rectangle rect = new Rectangle(WIDTH, HEIGHT, Color.GREY);
        rect.setOpacity(0.2);
        
        final double alertHeight = HEIGHT / 4;
        final double alertWidth = WIDTH / 2;
        final double buttonWidth = WIDTH / 10;
        final double buttonHeight = HEIGHT / 20;
        
        VBox alertBox = new VBox();
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setMinSize(alertWidth, alertHeight);
        alertBox.setMaxSize(alertWidth, alertHeight);
        alertBox.setBackground(GameUtils.loadBackground(
                "images/menu_bcgr.png", alertWidth, alertHeight
        ));
        alertBox.setSpacing(alertHeight / 6);
        
        String msg = names[playerId] + " has left the game, connection lost!";
        Text alert = new Text(msg);
        alert.setFont(Design.Font(HEIGHT / 25));

        ToggleButton saveButton = new Design.Button(buttonWidth, buttonHeight);
        saveButton.setText("Save");
        saveButton.addEventHandler(ActionEvent.ACTION, e -> saveGame());
        
        ToggleButton exitButton = new Design.Button(buttonWidth, buttonHeight);
        exitButton.setText("Exit");
        exitButton.addEventHandler(ActionEvent.ACTION, e -> exit());

        HBox buttonBox = new HBox(exitButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(alertWidth / 6);
        
        Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, msg);
        alertBox.getChildren().addAll(alert, buttonBox);
        this.getChildren().addAll(rect, alertBox);
    }
    
    private HBox getPersonalBox(int playerId){
        Text name = names[playerId];
        name.setFont(Design.Font(HEIGHT / 15));
        
        ImageView goldView = new ImageView("/images/gold.png");
        goldView.setFitWidth(WIDTH / 15);
        goldView.setFitHeight(HEIGHT / 10);
        goldView.setPreserveRatio(true);
        StackPane gold = new StackPane(goldView, scoreTexts[playerId]);
        gold.setMinSize(WIDTH / 15, HEIGHT / 10);
        gold.setMaxSize(WIDTH / 15, HEIGHT / 10);
        
        HBox personalBox = new HBox(gold, name);
        personalBox.setSpacing(WIDTH / 100);
        personalBox.setAlignment(Pos.BOTTOM_LEFT);
        personalBox.setPadding(new Insets(10, WIDTH / 100, 10, WIDTH / 100));
        
        return personalBox;
    }
    
    /**
     * Shows the names of the players.
     * 
     * @param names Names of the players
     */
    public void showNames(String[] names){
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            this.names[i] = new Text(names[i]);
        }
        
        //------------------Own personal box---------------------
        HBox privateBox = getPersonalBox(player.getId());
        privateBox.setAlignment(Pos.BOTTOM_LEFT);
        privatePane.getChildren().add(privateBox);
        
        //----------------Other personal boxes-------------------
        final double translate = WIDTH / 15;
        final double boxWidth = WIDTH / 3;
        final double boxHeight = HEIGHT / 10;
        final Pos[] positions = {
            Pos.CENTER_LEFT, Pos.TOP_CENTER, Pos.CENTER_RIGHT
        };
        
        for (int i = 1; i < NUM_OF_PLAYERS; i++){
            int playerId = (player.getId() + i) % NUM_OF_PLAYERS;
            
            HBox personalBox = getPersonalBox(playerId);
            personalBox.setMinSize(boxWidth, boxHeight);
            personalBox.setMaxSize(boxWidth, boxHeight);
            personalBox.setRotate(90 * i + 180);
            personalBox.setAlignment(Pos.CENTER);
            personalBox.setTranslateX((translate * i) - (translate * 2));
            
            table.setAlignment(personalBox, positions[i-1]);
            table.getChildren().add(personalBox);
        }
    }
    
    /**
     * Shows the player, who can play.
     * 
     * @param playerId Id of the player, who can play
     */
    public void showPlayingOne(int playerId){
        names[playerId].setFill(Design.YELLOW);
    }
    
    /**
     * Shows that a player can not play anymore.
     * 
     * @param playerId Id of the player who can not play anymore
     */
    public void stopPlayingOne(int playerId){
        names[playerId].setFill(Color.BLACK);
    }
    
    /**
     * Destroys the game view and sets back a game menu.
     */
    public void exit(){
        player.disconnect();
        program.setMainMenu();
        program.getRoot().getChildren().remove(this);
        System.gc();
    }
    
    /**
     * @return Hand view of the player
     * @see card_game.lora.HandView
     */
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
        yes.addEventHandler(ActionEvent.ACTION, e -> exit());

        ToggleButton no = new Design.Button(WIDTH / 10, HEIGHT / 20);
        no.setText("No");
        no.addEventHandler(ActionEvent.ACTION, e -> {
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
        rulesButton.addEventHandler(ActionEvent.ACTION, e -> showRules());
        
        //Exit button
        ToggleButton exitButton = new Design.Button(buttonWidth, buttonHeight);
        exitButton.setText("Exit");
        exitButton.addEventHandler(ActionEvent.ACTION, e -> {
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
        saveButton.addEventHandler(ActionEvent.ACTION, e -> saveGame());
        
        //Play button
        ToggleButton playButton = new Design.Button(buttonWidth, buttonHeight);
        playButton.setText("Continue");
        playButton.addEventHandler(ActionEvent.ACTION, e -> {
            this.getChildren().removeAll(rect, menu);
        });
        
        menu.getChildren().addAll(rulesButton, exitButton, saveButton, playButton);
        this.getChildren().addAll(rect, menu);
    }
    
    private void showRules(){
        Rules rules = new Rules(this, WIDTH / 3, HEIGHT * 2 / 3);
        rules.show(gameMode.ordinal());
    }
    
    private void saveGame(){
        SavedGames savedGames = new SavedGames(WIDTH, HEIGHT, this);
        SavedGames.SaveBox[] saveBoxes = savedGames.getSaveBoxes();
        String[] nameStrings = new String[NUM_OF_PLAYERS];
        for (int i = 0; i < NUM_OF_PLAYERS; i++){
            nameStrings[i] = names[i].getText();
        }
        
        for (int i = 0; i < saveBoxes.length; i++){
            final int num = i;
            saveBoxes[i].setOnMouseClicked(e -> {
                Design.playClick();
                
                if (saveBoxes[num].isEmpty()){
                    saveBoxes[num].save(nameStrings, score, gameMode, round);
                    saved = true;
                }
            });
        }
        savedGames.show();
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
    
    private class LoadingScreen extends StackPane{
        
        public LoadingScreen(double width, double height){
            Text text = new Text("Waiting for other players to connect");
            text.setFont(Design.Font(height / 15));
            
            this.getChildren().add(text);
            this.setAlignment(Pos.CENTER);
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
            this.setBackground(GameUtils.loadBackground(
                    "/images/loadingscreen.png", width, height)
            );
        }
        
        public void show(){
            program.getRoot().getChildren().add(this);
        }

        public void hide(){
            program.getRoot().getChildren().remove(this);
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
        private final VBox passBox;
        private final GridPane cards;
        
        public TensGUI(){
            cards = new GridPane();
            cards.setAlignment(Pos.CENTER);
            cards.setMinSize(WIDTH, HEIGHT);
            cards.setMaxSize(WIDTH, HEIGHT);
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
                    GridPane.setMargin(cardView, new Insets(
                            HEIGHT / 200, WIDTH / 200, HEIGHT / 200, WIDTH / 200
                    ));

                    cards.getChildren().add(cardView);
                }
            }
            
            ToggleButton passButton = new Design.Button(WIDTH / 10, HEIGHT / 15);
            passButton.setText("Pass");
            passButton.addEventHandler(ActionEvent.ACTION, e -> player.pass());
            
            passBox = new VBox(passButton);
            passBox.setPadding(new Insets(WIDTH / 100));
            passBox.setMaxSize(WIDTH / 10, HEIGHT / 10);
            passBox.setMaxSize(WIDTH / 10, HEIGHT / 10);
            setAlignment(passBox, Pos.BOTTOM_RIGHT);
        }
        
        @Override
        public void show(){  
            table.getChildren().add(cards);
            cards.toBack();
            getChildren().add(passBox);
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
            getChildren().remove(passBox);
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