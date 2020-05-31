/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.lora.game_modes.GameModes;
import card_game.net.Server;
import javafx.animation.FadeTransition;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author stepan
 */
public class GameMenu extends StackPane{
    
    private final int MAX_PLAYERS = 4;
    private final Rectangle2D bounds = Screen.getPrimary().getBounds();
    private final double WIDTH = bounds.getWidth();
    private final double HEIGHT = bounds.getHeight();
    private final double BUTTON_WIDTH = WIDTH / 5;
    private final double BUTTON_HEIGHT = HEIGHT / 10;
    private final String bcgrPath = "/images/main_menu_bcgr2.png";
    private final Main program;
     
    private int numOfPlayers = -1;
    
    public GameMenu(Main program, String nickname){
        this.program = program;
        
        setWidth(WIDTH);
        setHeight(HEIGHT);
       
        Background bcgr = GameUtils.loadBackground(bcgrPath, WIDTH, HEIGHT);
        setBackground(bcgr); 
        
        if (nickname == null){
            setNameEntrance();
        } else {
            setMainButtons();
        }
    }
    
    private boolean isCorrectName(String s){
        if (s.length() < 1){
            return false;
        }
        
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            
            if (!(c >= 'a' && c <= 'z') &&
                !(c >= 'A' && c <= 'Z') &&
                !(c >= '0' && c <= '9'))
            {
                return false;
            }
        }
        
        return true;
    }
    
    private void setNameEntrance(){
        final double nameGridWidth = WIDTH / 3;
        final double nameGridHeight = HEIGHT / 5;
        
        Text title = new Text("Enter the name:");
        title.setFont(Design.Font(nameGridHeight / 10));
        
        final int maxNameLength = 15;
        TextField name = new TextField();
        name.setFont(Design.Font(nameGridHeight / 10));
        name.textProperty().addListener((
            ObservableValue<? extends String> ov, String v1, String v2) -> {
                if (name.getText().length() > maxNameLength) {
                    String s = name.getText().substring(0, maxNameLength);
                    name.setText(s);
                }
            }
        );
        
        Text warning = new Text("Only numbers and english letters are allowed");
        warning.setFont(Design.Font(nameGridHeight / 12));
        warning.setOpacity(0);
        
        ToggleButton nextButton = new Design.Button(
                nameGridWidth / 5, nameGridHeight / 5
        );
        nextButton.setText("Next");
        nextButton.setOnAction(e -> {
            if (isCorrectName(name.getText())){
                program.setNickname(name.getText());
                setMainButtons();
            } else {
                warning.setOpacity(1);
            }            
        });
        
        GridPane nameGrid = new GridPane();
        nameGrid.setAlignment(Pos.CENTER);
        nameGrid.setMaxSize(nameGridWidth, nameGridHeight);
        nameGrid.setMinSize(nameGridWidth, nameGridHeight);
        nameGrid.setVgap(3);
        nameGrid.setHgap(2);
        nameGrid.setBackground(GameUtils.loadBackground(
                "/images/menu_bcgr.png", nameGridWidth, nameGridHeight
        ));
        
        nameGrid.add(title, 0, 0, 1, 1);
        nameGrid.add(name, 0, 1, 1, 1);
        nameGrid.add(warning, 0, 2, 2, 1);
        nameGrid.add(nextButton, 1, 1, 1, 1);
        GridPane.setMargin(nextButton, new Insets(0, 0, 0, nameGridWidth / 20));
   
        HBox capsule = new HBox(nameGrid);
        capsule.setAlignment(Pos.CENTER);
        capsule.setMaxSize(WIDTH, HEIGHT / 3);
        capsule.setMinSize(WIDTH, HEIGHT / 3);
        GameMenu.setAlignment(capsule, Pos.BOTTOM_CENTER);
        
        this.getChildren().add(capsule);
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
        buttonsPane.setMaxSize(gridPaneWidth, gridPaneHeight);
        buttonsPane.setMinSize(gridPaneWidth, gridPaneHeight);
        GameMenu.setAlignment(buttonsPane, Pos.BOTTOM_CENTER);
        
        double hMargin = (gridPaneWidth - hBoxes * BUTTON_WIDTH) / (2 * hBoxes);
        double vMargin = (gridPaneHeight - vBoxes * BUTTON_HEIGHT) / (2 * vBoxes);
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
        ToggleButton playButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
        playButton.setText("Play");
        playButton.setOnAction(e -> {
            chooseGame();
        });
        
        //Saved games
        ToggleButton savesButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
        savesButton.setText("Saved games");
        savesButton.setOnAction(e -> {
            savedGames();
        });
        
        //Rules
        ToggleButton rulesButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
        rulesButton.setText("Rules");
        rulesButton.setOnAction(e -> {
            rules();
        });
        
        //Exit Button
        ToggleButton exitButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setText("Exit");
        exitButton.setOnAction(e -> program.close());
        
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
        ToggleButton trainingButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
        trainingButton.setText("Training");
        trainingButton.setOnAction(e -> {
            chooseGameMode();
        });
        
        //Create game
        ToggleButton serverButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
        serverButton.setText("Create game");
        serverButton.setOnAction(e -> {
            createGame(-1);
        });
        
        //Join game
        ToggleButton clientButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
        clientButton.setText("Join game");
        clientButton.setOnAction(e -> {
            joinServer();
        });
        
        //Return
        ToggleButton returnButton = new Design.Button(BUTTON_WIDTH, BUTTON_HEIGHT);
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
        ModesView modesBox = new ModesView(HEIGHT);
        
        GameModes[] modes = GameModes.values();
        for (int i = 0; i < modes.length; i++){
            final int num = i;
            modesBox.getModeBox(modes[i]).setOnMouseClicked(e -> {
                createGame(num);
            });
        }
        
        this.getChildren().clear();
        this.getChildren().add(modesBox);
    }
    
    private void unselectAll(ToggleButton[] buttons){
        for (ToggleButton button : buttons){
            button.setStyle("-fx-text-fill: " + Design.Colour.GREY.toString());
        }
    }    
    
    private void createGame(int gameModeId){
        numOfPlayers = -1;

        final double boxWidth = WIDTH / 2;
        final double boxHeight = HEIGHT * 2 / 5;
        
        // Text
        Text text = new Text("Choose number of players");
        text.setFont(Design.Font(boxHeight / 5));
        
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), text);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(2);
        
        //Number of players buttonsPane
        final double numbersSize = boxWidth / 9;
        
        HBox numbers = new HBox();
        numbers.setAlignment(Pos.CENTER);
        numbers.setSpacing((boxWidth - numbersSize) / 15);
        numbers.setMinSize(boxWidth, boxHeight / 4);
        numbers.setMaxSize(boxWidth, boxHeight / 4);
        
        ToggleButton[] numOfPlayersButtons = new ToggleButton[MAX_PLAYERS];
        for (int i = 1; i <= MAX_PLAYERS; i++){
            ToggleButton button = new Design.Button(numbersSize, numbersSize);
            button.setText(Integer.toString(i));
            
            button.setOnMouseEntered(e -> {});
            button.setOnMouseExited(e -> {});
            
            final int num = i;
            button.setOnAction(e -> {
                unselectAll(numOfPlayersButtons);
                button.setStyle(
                        "-fx-text-fill: " + Design.Colour.YELLOW.toString()
                );
                numOfPlayers = num;
            });
            
            numOfPlayersButtons[i-1] = button;
            numbers.getChildren().add(button);
        }
        
        final double buttonWidth = boxWidth / 3;
        final double buttonHeight = boxHeight / 5;
        
        //Play button
        ToggleButton playButton = new Design.Button(buttonWidth, buttonHeight);
        playButton.setText("Play");
        playButton.setOnAction(e -> {
            if (numOfPlayers > 0 && numOfPlayers <= 4){
                startServer(gameModeId);
            } else {
                fadeTransition.play();
            }
        });
        
        //Cancel button
        ToggleButton cancelButton = new Design.Button(buttonWidth, buttonHeight);
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(e -> {
            setMainButtons();
        });
        
        HBox buttons = new HBox(playButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(buttonWidth / 3);
        buttons.setMinSize(boxWidth, boxHeight / 4);
        buttons.setMaxSize(boxWidth, boxHeight / 4);
        
        VBox vBox = new VBox(text, numbers, buttons);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(boxHeight / 15);
        vBox.setMinSize(boxWidth, boxHeight);
        vBox.setMaxSize(boxWidth, boxHeight);
        GameMenu.setAlignment(vBox, Pos.BOTTOM_CENTER);
        
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
        for (int i = 0; i < MAX_PLAYERS - numOfPlayers;  i++){
            MpBot bot = new MpBot("Bot" + i, program);
            bot.connectToServer();
        }
    }
    
    private void joinServer(){
        MpPlayer player = new MpPlayer(program.getNickname(), program);
        
        if (player.connectToServer()){
            program.getRoot().getChildren().remove(this);
        }
    }
}
