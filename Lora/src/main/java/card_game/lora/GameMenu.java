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
    private final String bcgrPath = "/images/the_card_players.jpg";
    private final Rectangle2D bounds = Screen.getPrimary().getBounds();
    private final double width = bounds.getWidth();
    private final double height = bounds.getHeight();
    private final Button serverButton, clientButton, exitButton, trainingButton;
    private final VBox centerVBox;
    private final Main program;

    private int numOfPlayers = -1;
    
    public GameMenu(Main program, Stage stage){
        this.program = program;
        
        Background bcgr = GameUtils.loadBackground(bcgrPath, width, height);
        setBackground(bcgr);
        setPrefHeight(height);
        setPrefWidth(width);
        
        centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setSpacing(SPACING);
        
        //Create game
        serverButton = new Button("Create new game");
        serverButton.setOnAction(e -> {
            createGame(-1);
        });
        
        //Join game
        clientButton = new Button("Join existing game");
        clientButton.setOnAction(e -> {
            joinServer();
        });
        
        //Training
        trainingButton = new Button("Training");
        trainingButton.setOnAction(e -> {
            chooseGameMode();
        });
        
        //Exit Button
        exitButton = new Button("Exit");
        exitButton.setOnAction(e -> program.closeProgram(stage));
        
        getChildren().add(centerVBox);
        setAlignment(centerVBox, Pos.CENTER);
        
        setMainButtons();
    }
    
    private void setMainButtons(){
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(
                serverButton, clientButton, trainingButton, exitButton
        );
    }
    
    private void chooseGameMode(){
        int length = GameModes.values().length;

        GridPane gameModes = new GridPane();
        gameModes.setAlignment(Pos.CENTER);
        gameModes.setHgap(length);
        gameModes.setVgap(2);

        double windowWidth = width / (length + SPACING);
        double windowHeight = height / 2;
        Insets margin = new Insets(0, SPACING / 2, SPACING, SPACING / 2);
        
        for (int i = 0; i < length; i++){
            GameModes gameMode = GameModes.values()[i];
            ImageView windowView = new ImageView(
                    GameUtils.getModeImage(gameMode)
            );
            
            windowView.setFitWidth(windowWidth);
            windowView.setFitHeight(windowHeight);
            windowView.setPreserveRatio(true);
            
            final int num = i;
            windowView.setOnMouseClicked(e -> {
                createGame(num);
            });
            
            GridPane.setConstraints(windowView, i, 0);
            GridPane.setMargin(windowView, margin);
            
            Text windowName = new Text(gameMode.toString());
            GridPane.setConstraints(windowName, i, 0);
            
            gameModes.getChildren().addAll(windowView, windowName);
        }
        
        centerVBox.getChildren().clear();
        centerVBox.getChildren().add(gameModes);
    }
    
    private void unselectAll(ToggleButton[] buttons){
        for (ToggleButton button : buttons){
            button.setSelected(false);
        }
    }    
    
    private void createGame(int gameModeId){
        numOfPlayers = -1;
        
        Text text = new Text("Choose number of players");
        text.setFont(Font.font ("Verdana", 30));
        text.setFill(Color.WHITE);
        
        HBox numbers = new HBox();
        numbers.setAlignment(Pos.CENTER);
        numbers.setSpacing(SPACING);
        
        //Number of players buttons
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
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), text);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(2);
        
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-focus-color: transparent;");
        playButton.setOnAction(e -> {
            if (numOfPlayers > 0 && numOfPlayers <= 4){
                startServer();
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
        
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(text, numbers, buttons);
    }
    
    private void startServer(){
        Server server = new Server();
        Thread t = new Thread(server);
        t.setName("Thread: server");
        t.start();
        
        addBots(numOfPlayers);
        joinServer();
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
