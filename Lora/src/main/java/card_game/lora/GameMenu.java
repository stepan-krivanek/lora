/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.lora.game_modes.GameModes;
import card_game.net.Server;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author stepan
 */
public class GameMenu extends StackPane{
    
    private final int MAX_PLAYERS = 4;
    private final int SPACING = 20;
    private final Rectangle2D bounds = Screen.getPrimary().getBounds();
    private final double WIDTH = bounds.getWidth();
    private final double HEIGHT = bounds.getHeight();
    private final double buttonWidth = WIDTH / 5;
    private final double buttonHeight = HEIGHT / 10;
    private final String bcgrPath = "/images/main_menu_bcgr.png";
    private final Main program;
    private final Stage stage;

    private int numOfPlayers = -1;
    
    public GameMenu(Main program, Stage stage){
        this.program = program;
        this.stage = stage;
        
        Background bcgr = GameUtils.loadBackground(bcgrPath, WIDTH, HEIGHT);
        setBackground(bcgr);
        setPrefHeight(HEIGHT);
        setPrefWidth(WIDTH);
        
        setMainButtons();
    }
    
    private void setFourButtons(ToggleButton[] buttons){
        final double hBoxes = 4;
        final double vBoxes = 2;
        final double gridPaneWidth = WIDTH;
        final double gridPaneHeight = HEIGHT / 3;
        
        GridPane buttonsPane = new GridPane();
        buttonsPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonsPane.setHgap(hBoxes);
        buttonsPane.setVgap(vBoxes);
        buttonsPane.setPrefWidth(gridPaneWidth);
        buttonsPane.setPrefHeight(gridPaneHeight);
        
        double hMargin = (gridPaneWidth - hBoxes * buttonWidth) / (2 * hBoxes);
        double vMargin = (gridPaneHeight - vBoxes * buttonHeight) / (2 * vBoxes);
        Insets insets = new Insets(vMargin, hMargin, vMargin, hMargin);
        
        for (int i = 0; i < 4; i++){
            buttonsPane.add(buttons[i], i, (i&1)^((i>>1)&1), 1, 1);
            GridPane.setMargin(buttons[i], insets);
        }
        
        this.getChildren().clear();
        this.getChildren().addAll(buttonsPane);
    }
    
    private void setMainButtons(){
        //Play
        ToggleButton playButton = new Design.Button(buttonWidth, buttonHeight);
        playButton.setText("Play");
        playButton.setOnAction(e -> {
            chooseGame();
        });
        
        //Saved games
        ToggleButton savesButton = new Design.Button(buttonWidth, buttonHeight);
        savesButton.setText("Saved games");
        savesButton.setOnAction(e -> {
            savedGames();
        });
        
        //Rules
        ToggleButton rulesButton = new Design.Button(buttonWidth, buttonHeight);
        rulesButton.setText("Rules");
        rulesButton.setOnAction(e -> {
            rules();
        });
        
        //Exit Button
        ToggleButton exitButton = new Design.Button(buttonWidth, buttonHeight);
        exitButton.setText("Exit");
        exitButton.setOnAction(e -> program.closeProgram(stage));
        
        ToggleButton[] buttons = {
            playButton, savesButton, rulesButton, exitButton
        };
        setFourButtons(buttons);
    }
    
    private void savedGames(){
        
    }
    
    private void rules(){
        
    }
    
    private void chooseGame(){
        //Training
        ToggleButton trainingButton = new Design.Button(buttonWidth, buttonHeight);
        trainingButton.setText("Training");
        trainingButton.setOnAction(e -> {
            chooseGameMode();
        });
        
        //Create game
        ToggleButton serverButton = new Design.Button(buttonWidth, buttonHeight);
        serverButton.setText("Create game");
        serverButton.setOnAction(e -> {
            createGame(-1);
        });
        
        //Join game
        ToggleButton clientButton = new Design.Button(buttonWidth, buttonHeight);
        clientButton.setText("Join game");
        clientButton.setOnAction(e -> {
            joinServer();
        });
        
        //Return
        ToggleButton returnButton = new Design.Button(buttonWidth, buttonHeight);
        returnButton.setText("Return");
        returnButton.setOnAction(e -> {
            setMainButtons();
        });
        
        ToggleButton[] buttons = {
            trainingButton, serverButton, clientButton, returnButton
        };
        setFourButtons(buttons);
    }
    
    private void chooseGameMode(){
        final int numOfModes = GameModes.values().length;
        final int topModes = numOfModes / 2;

        final double windowHeight = HEIGHT / 3;
        final double windowWidth = windowHeight * 2 / 3;
        final double vSpacing = windowHeight / 3;
        final double hSpacing = windowWidth / 2;
        
        HBox topBox = new HBox();
        topBox.setSpacing(hSpacing);
        topBox.setAlignment(Pos.CENTER);
        
        HBox botBox = new HBox();
        botBox.setSpacing(hSpacing);
        botBox.setAlignment(Pos.CENTER);
        
        BackgroundFill onFill = new BackgroundFill(
                    Design.YELLOW, new CornerRadii(15), Insets.EMPTY
        );
        BackgroundFill offFill = new BackgroundFill(
                Design.GREY, new CornerRadii(15), Insets.EMPTY
        );
        
        for (int i = 0; i < numOfModes; i++){
            GameModes gameMode = GameModes.values()[i];
            //-----------------Icon--------------------
            ImageView modeIcon = new ImageView(
                    GameUtils.getModeImage(gameMode)
            );
            modeIcon.setFitWidth(windowWidth * 2 / 3);
            modeIcon.setFitHeight(windowHeight * 2 / 3);
            modeIcon.setPreserveRatio(true);
            
            //-----------------Text---------------------
            Text modeText = new Text(gameMode.toString());
            modeText.setFont(Design.Font(windowHeight / 10));
            
            //----------------Mode box------------------
            VBox modeBox = new VBox(modeIcon, modeText);
            modeBox.setSpacing(windowHeight / 8);
            modeBox.setAlignment(Pos.CENTER);
            modeBox.setMinSize(windowWidth, windowHeight);
            modeBox.setMaxSize(windowWidth, windowHeight);
            
            modeBox.setBackground(new Background(offFill));
            modeBox.setOnMouseEntered(e -> {
                modeBox.setBackground(new Background(onFill));
            });
            modeBox.setOnMouseExited(e -> {
                modeBox.setBackground(new Background(offFill));
            });
            final int num = i;
            modeBox.setOnMouseClicked(e -> {
                createGame(num);
            });
            
            if (i < topModes){
                topBox.getChildren().add(modeBox);
            } else {
                botBox.getChildren().add(modeBox);
            }
        }
        
        VBox gameModes = new VBox(topBox, botBox);
        gameModes.setSpacing(vSpacing);
        gameModes.setAlignment(Pos.CENTER);
        
        this.getChildren().clear();
        this.getChildren().add(gameModes);
    }
    
    private void unselectAll(ToggleButton[] buttons){
        for (ToggleButton button : buttons){
            button.setSelected(false);
        }
    }    
    
    private void createGame(int gameModeId){
        numOfPlayers = -1;

        // Text
        Text text = new Text("Choose number of players");
        text.setFont(Font.font ("Verdana", 30));
        text.setFill(Color.WHITE);
        
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), text);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(2);
        
        //Number of players buttonsPane
        HBox numbers = new HBox();
        numbers.setAlignment(Pos.CENTER);
        numbers.setSpacing(SPACING);
        
        ToggleButton[] numOfPlayersButtons = new ToggleButton[MAX_PLAYERS];
        for (int i = 1; i <= MAX_PLAYERS; i++){
            ToggleButton button = new ToggleButton(Integer.toString(i));
            button.setStyle("-fx-focus-color: transparent;");
            
            final int num = i;
            button.setOnAction(e -> {
                unselectAll(numOfPlayersButtons);
                button.setSelected(true);
                numOfPlayers = num;
            });
            
            numOfPlayersButtons[i - 1] = button;
            numbers.getChildren().add(button);
        }
        
        //Play button
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-focus-color: transparent;");
        playButton.setOnAction(e -> {
            if (numOfPlayers > 0 && numOfPlayers <= 4){
                startServer(gameModeId);
            } else {
                fadeTransition.play();
            }
        });
        
        //Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-focus-color: transparent;");
        cancelButton.setOnAction(e -> {
            setMainButtons();
        });
        
        HBox buttons = new HBox(playButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(SPACING);
        
        VBox vBox = new VBox(text, numbers, buttons);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(SPACING);
        
        this.getChildren().clear();
        this.getChildren().addAll(vBox);
    }
    
    private void startServer(int gameModeId){
        Server server = new Server(gameModeId);
        
        Thread t = new Thread(server);
        t.setName("Thread: server");
        t.start();
        
        joinServer();
        addBots(numOfPlayers);
    }
    
    private void addBots(int numOfPlayers){
        for (int i = 4; i > numOfPlayers;  --i){
            MpBot bot = new MpBot(program);
            bot.connectToServer();
        }
    }
    
    private void joinServer(){
        MpPlayer player = new MpPlayer(program);
        
        if (player.connectToServer()){
            this.setVisible(false);
        }
    }
}
