/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.lora.game_modes.GameMode;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author stepa
 */
public class Rules extends VBox{
    
    private final StackPane stack;
    private final ImageView modeView;
    private final Label info;
    private final Label name;
    private final Rectangle rect;
    
    private int gameMode = -1;
    
    public Rules(StackPane stack, double width, double height){
        this.stack = stack;
        
        rect = new Rectangle(
                GameUtils.getScreenWidth(),
                GameUtils.getScreenHeight(),
                Color.GREY
        );
        rect.setOpacity(0.2);
        
        final double buttonWidth = width / 5;
        final double buttonHeight = height / 15;
        
        //---------------Top bar------------------
        
        ToggleButton closeButton = new Design.Button(buttonWidth, buttonHeight); 
        closeButton.setText("Close");
        closeButton.addEventHandler(ActionEvent.ACTION, e -> {
            stack.getChildren().removeAll(rect, this);
        });
        
        final double topBarHeight = height / 3;
        
        VBox closeBox = new VBox(closeButton);
        closeBox.setMaxSize(width / 4, topBarHeight);
        closeBox.setMinSize(width / 4, topBarHeight);
        closeBox.setAlignment(Pos.TOP_RIGHT);
        VBox.setVgrow(closeBox, Priority.ALWAYS);
        HBox.setHgrow(closeBox, Priority.ALWAYS);
        
        VBox fillBox = new VBox();
        fillBox.setMaxSize(width / 4, topBarHeight);
        fillBox.setMinSize(width / 4, topBarHeight);
        
        modeView = new ImageView();
        modeView.setFitWidth(width / 3);
        modeView.setFitHeight(width / 3);
        modeView.setPreserveRatio(true);
        
        VBox modeBox = new VBox(modeView);
        modeBox.setMaxSize(width / 2, topBarHeight);
        modeBox.setAlignment(Pos.CENTER);
        
        HBox topBar = new HBox(fillBox, modeBox, closeBox);
        topBar.setPadding(new Insets(width / 20));
        topBar.setMaxSize(width, topBarHeight);
        topBar.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(topBar, Priority.ALWAYS);
        
        //---------------Bot bar------------------
        
        ToggleButton nextButton = new Design.Button(buttonWidth, buttonHeight);
        nextButton.setText("Next");
        nextButton.addEventHandler(ActionEvent.ACTION, e -> {
            gameMode = (gameMode + 1) % Rule.values().length;
            setRule();
        });
        
        VBox rightBox = new VBox(nextButton);
        rightBox.setMaxSize(width / 2, buttonHeight);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        
        ToggleButton prevButton = new Design.Button(buttonWidth, buttonHeight);
        prevButton.setText("Prev");
        prevButton.addEventHandler(ActionEvent.ACTION, e -> {
            gameMode -= 1;
            if (gameMode < 0){
                gameMode = Rule.values().length - 1;
            }
            setRule();
        });
        
        VBox leftBox = new VBox(prevButton);
        leftBox.setMaxSize(width / 2, buttonHeight);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        
        name = new Label();
        name.setFont(Design.Font(buttonHeight));
        name.setAlignment(Pos.CENTER);
        
        HBox botBox = new HBox(leftBox, name, rightBox);
        botBox.setPadding(new Insets(width / 20));
        botBox.setMaxSize(width, buttonHeight);
        botBox.setAlignment(Pos.BOTTOM_CENTER);
        
        VBox botBar = new VBox(botBox);
        VBox.setVgrow(botBar, Priority.ALWAYS);
        botBar.setAlignment(Pos.BOTTOM_CENTER);
        //----------------Text--------------------
        
        final double textHeight = height - topBarHeight - buttonHeight;
        
        info = new Label();
        info.setWrapText(true);
        info.setMaxSize(width * 2 / 3, textHeight);
        info.setAlignment(Pos.CENTER);
        info.setFont(Design.Font(height / 40));
        
        //----------------This--------------------
        
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
        this.setAlignment(Pos.CENTER);
        this.setBackground(GameUtils.loadBackground(
                "/images/rules.png", width, height
        ));
        
        this.getChildren().addAll(topBar, info, botBar);
    }
    
    public void show(int gameMode){
        if (gameMode > Rule.values().length || gameMode < 0){
            gameMode = Rule.INTRODUCTION.ordinal();
        }
        this.gameMode = gameMode;
        
        setRule();
        
        stack.getChildren().addAll(rect, this);
    }
    
    private void setRule(){
        int index = gameMode < Rule.INTRODUCTION.ordinal() ? gameMode : Rule.RED_KING.ordinal();
        
        modeView.setImage(GameUtils.getModeImage(GameMode.values()[index]));
        info.setText(Rule.values()[gameMode].toString());
        name.setText(Rule.values()[gameMode].getName());
    }
    
    private enum Rule {
        
        Reds("Reds",
            "The game is played in the classic order.\n" +
"\n" +
"In the first trick, red cards (hearts, reds) can not be played. No trick can be led by a red card (unless there is no other option). Players receive 1 point for each red card in the trick they take."  
        ),
        
        SUPERIORS("Superiors",
            "The game is played in the classic order.\n" +
"\n" +
"Players try to avoid taking superiors. Each superior in a taken trick scores 2 points."
        ),
        
        FRLA("First-Last",
            "The game is played in the classic order.\n" +
"\n" +
"Players try to avoid taking the first and the last trick of the deal. Each of those tricks scores 4 points for the player taking it." 
        ),
        
        ALL("All",
            "The game is played in the classic order.\n" +
"\n" +
"Players try to avoid taking any trick. Each trick scores 1 point for the player taking it."
        ),
        
        RED_KING("Red King",
            "The game is played in the classic order.\n" +
"\n" +
"In the first trick, red cards (hearts, reds) and especially the red king can not be played (unless there is no other option). No trick can be led by a red card (unless there is no other option). The player taking a trick with the red king receives 8 points."
        ),
        
        TENS("Tens",
            "Starting by the forehand and continuing in the clockwise direction, the players put cards on the table, until one player has no card left in hand. Players can now play multiple cards in a single turn. They only can play a ten or any card that already has its neighbor of the same suit on the table. As a neighbor is considered a card with rank 1 higher or 1 lower of the same suit. (The order of ranks: 7, 8, 9, X, Inferior, Superior, King, Ace.)"
        ),
        
        QUARTS("Quarts",
            "Each turn, to the first card played, the following 3 higher cards of the same suit must be added. Player, who played the highest card, takes the trick and leads the next one. (The order of ranks: 7, 8, 9, X, Inferior, Superior, King, Ace.)\n" +
"\n" +
"The game ends when a player has no more cards in his hand. Then each player receives 1 point for each card left in his/her hand."
        ),
        
        INTRODUCTION("Introduction",
            "Lóra consists of four rounds. In a round, seven different minigames and a graduation is played. The minigames are started by the current forehand and continue in the clockwise order (except Quarts).\n" +
"\n" +
"The trick is taken by the highest card of the led suit (suit of the first card played in a trick) and the winner of a trick leads to the next trick. When a minigame is finished, the next deal with the next minigame follows and the forehand starts again."
        ),
        GRADUATION("Graduation",
            "After 7 deals, the forehand is about to “graduate”, which means, that he/she must choose any of the minigames to play and win it with 0 points. In case of failure, he/she receives 8 points and the other players none. The forehand has two more attempts to graduate. After the forehand wins with 0 points or fails all 3 attempts, the next player clockwise becomes the forehand. After all 4 rounds, all players have gone through their graduation and the game is over."
        );
        
        
        private final String name;
        private final String label;
        
        private Rule(String name, String label){
            this.name = name;
            this.label = label;
        }
        
        public String getName(){
            return name;
        }
        
        @Override
        public String toString(){
            return label;
        }
    }
    
}
