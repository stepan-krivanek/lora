/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.lora.game_modes.GameMode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * GUI with the saved games.
 * 
 * @author Štěpán Křivánek
 */
public class SavedGames extends VBox{
    
    private final int ROWS = 3;
    private final int COLS = 4;
    private final SaveBox[] saveBoxes = new SaveBox[ROWS * COLS];
    private final StackPane stack;
    
    /**
     * Creates a new GUI with saved games.
     * 
     * @param width Width of the GUI
     * @param height Height of the GUI
     * @param stack StackPane to be placed
     */
    public SavedGames(double width, double height, StackPane stack){
        this.stack = stack;
        
        //------------------Saved games--------------
        GridPane savesGrid = new GridPane();
        savesGrid.setAlignment(Pos.CENTER);
        savesGrid.setVgap(3);
        savesGrid.setHgap(4);
        
        double saveBoxWidth = width / 5;
        double saveBoxHeight = height / 5;
        double hMargin = saveBoxWidth / 10;
        double vMargin = saveBoxHeight / 8;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                int index = i * COLS + j;
                
                SaveBox saveBox = new SaveBox(
                        saveBoxWidth, saveBoxHeight, index
                );
                GridPane.setMargin(saveBox, new Insets(
                        vMargin, hMargin, vMargin, hMargin)
                );
                savesGrid.add(saveBox, j, i, 1, 1);
                
                saveBoxes[index] = saveBox;
            }
        }
        
        //-------------------Name--------------------
        Text name = new Text("Saved games");
        name.setFont(Design.Font(height / 15));
        
        HBox textBox = new HBox(name);
        textBox.setPadding(new Insets(width / 100));
        textBox.setAlignment(Pos.TOP_LEFT);
        
        //---------------Return button---------------
        ToggleButton returnButton = new Design.Button(
                width / 10, height / 20
        );
        returnButton.setText("Return");
        returnButton.setOnAction(e -> {
            hide();
        });
        
        HBox returnBox = new HBox(returnButton);
        returnBox.setPadding(new Insets(width / 100));
        returnBox.setAlignment(Pos.BOTTOM_RIGHT);
        
        //--------------------this--------------------
        
        this.setAlignment(Pos.CENTER);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
        this.getChildren().addAll(textBox, savesGrid, returnBox);
        this.setBackground(new Background(new BackgroundFill(
                Design.BLUE, CornerRadii.EMPTY, Insets.EMPTY
        )));
    }
    
    /**
     * Gets the save boxes to apply actions
     * or for further editation.
     * 
     * @return Array of boxes with saved games
     */
    public SaveBox[] getSaveBoxes(){
        return saveBoxes;
    }
    
    /**
     * Shows this GUI.
     */
    public void show(){
        stack.getChildren().add(this);
    }
    
    private void hide(){
        stack.getChildren().remove(this);
    }
    
    /**
     * Box containing all information about a saved game,
     * and button to delete the saved game.
     */
    public class SaveBox extends HBox{
        
        private final int NUM_OF_PLAYERS = 4;
        private final Text[] names = new Text[NUM_OF_PLAYERS];
        private final Text[] score = new Text[NUM_OF_PLAYERS];
        private final Text gameMode = new Text("");
        private final Text round = new Text("");
        private final String path;
        private final double width;
        private final double height;
        
        private boolean isEmpty = true;
        private int gameModeId = 0;
        
        /**
         * Creates a new box with a saved game at specified index.
         * 
         * @param width Box width
         * @param height Box height
         * @param index Index of the saved game
         */
        public SaveBox(double width, double height, int index){
            this.width = width;
            this.height = height;
            path = "src/main/resources/saves/save" + index + ".txt";
            
            this.setAlignment(Pos.CENTER);
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
            this.setBackground(new Background(new BackgroundFill(
                    Design.BROWN, new CornerRadii(15), Insets.EMPTY
            )));
            
            double textSize = height / 8;
            Insets hMargin = new Insets(0, width / 15, 0, width / 15);
            
            gameMode.setFont(Design.Font(textSize));
            gameMode.setFill(Design.GREY);
            GridPane.setMargin(gameMode, hMargin);
            
            round.setFont(Design.Font(textSize));
            round.setFill(Design.GREY);
            GridPane.setMargin(round, hMargin);
            
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                names[i] = new Text("");
                names[i].setFont(Design.Font(textSize));
                names[i].setFill(Design.GREY);
                GridPane.setMargin(names[i], hMargin);
                
                score[i] = new Text("");
                score[i].setFont(Design.Font(textSize));
                score[i].setFill(Design.GREY);
                GridPane.setMargin(score[i], hMargin);
            }
            
            if (!loadFile()){
                setEmpty();
            } else {
                setData();
            }
        }
        
        /**
         * Saves a game with the specified parameters.
         * 
         * @param names Names of the players in the game
         * @param score Score of the player.
         * @param gameMode Current game mode to play
         * @param round Current round of the game
         */
        public void save(String[] names, int[] score, GameMode gameMode, int round){
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                this.names[i].setText(names[i]);
                this.score[i].setText(Integer.toString(score[i]));
            }
            this.gameMode.setText(gameMode.toString());
            this.round.setText(Integer.toString(round));
            setData();
            
            File file = new File(path);
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                FileWriter writer = new FileWriter(file);
                BufferedWriter bufWriter = new BufferedWriter(writer);
                
                bufWriter.write(Integer.toString(gameMode.ordinal()));
                bufWriter.newLine();
                bufWriter.write(Integer.toString(round));
                bufWriter.newLine();
                
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    bufWriter.write(names[i]);
                    bufWriter.newLine();
                    bufWriter.write(Integer.toString(score[i]));
                    bufWriter.newLine();
                }
                
                bufWriter.close();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * @return Round of the saved game
         */
        public int getRound(){
            return Integer.parseInt(round.getText());
        }
        
        /**
         *
         * @return Game mode of the saved game
         */
        public int getMode(){
            return gameModeId;
        }
        
        /**
         *
         * @return Score of the saved game
         */
        public int[] getScore(){
            int[] intScore = new int[NUM_OF_PLAYERS];
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                intScore[i] = Integer.parseInt(score[i].getText());
            }
            return intScore;
        }
        
        /**
         *
         * @return True if no game is saved under the box' index, false otherwise
         */
        public boolean isEmpty(){
            return isEmpty;
        }
        
        private boolean delete(){
            boolean ret = true;
            try {
                PrintWriter writer = new PrintWriter(path);
                writer.write("");
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
                ret = false;
            }
            
            setEmpty();
            return ret;
        }
        
        private void setData(){
            GridPane info = new GridPane();
            info.setMaxSize(width, height);
            info.setAlignment(Pos.CENTER);
            info.setHgap(3);
            info.setVgap(5);
            
            info.add(gameMode, 0, 0, 1, 1);
            info.add(round, 1, 0, 1, 1);
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                info.add(names[i], 0, i+1, 1, 1);
                info.add(score[i], 1, i+1, 1, 1);
            }
            
            Rectangle bin = new Rectangle(width / 10, width / 10);
            bin.setFill(new ImagePattern(new Image(
                "/images/icons/delete.png"
            )));
            bin.setOnMouseEntered(e -> {
                bin.setFill(new ImagePattern(new Image(
                    "/images/icons/delete_hover.png"
                )));
            });
            bin.setOnMouseExited(e -> {
                bin.setFill(new ImagePattern(new Image(
                    "/images/icons/delete.png"
                )));
            });
            bin.setOnMouseClicked(e -> {
                delete();
            });
            
            HBox binBox = new HBox(bin);
            binBox.setAlignment(Pos.BOTTOM_RIGHT);
            HBox.setHgrow(binBox, Priority.ALWAYS);
            
            HBox infoBox = new HBox(info);
            infoBox.setAlignment(Pos.CENTER);
            HBox.setHgrow(infoBox, Priority.ALWAYS);
            
            this.setOnMouseEntered(e -> {
                changeColor(true);
            });
            this.setOnMouseExited(e -> {
                changeColor(false);
            });
            
            isEmpty = false;
            this.getChildren().clear();
            this.getChildren().addAll(infoBox, binBox);
        }
        
        private void setEmpty(){
            this.getChildren().clear();
            
            Text empty = new Text("Empty");
            empty.setFont(Design.Font(height / 5));
            empty.setFill(Design.GREY);
            this.setOnMouseEntered(e -> {
                empty.setFill(Design.YELLOW);
            });
            this.setOnMouseExited(e -> {
                empty.setFill(Design.GREY);
            });
            
            isEmpty = true;
            this.getChildren().add(empty);
        }
        
        private boolean loadFile(){
            File file = new File(path);
            if (!file.exists() || file.length() == 0){
                return false;
            }
            
            try {
                Scanner scanner = new Scanner(file);
                
                gameModeId = Integer.parseInt(scanner.nextLine());
                gameMode.setText(GameMode.values()[gameModeId].toString());
                round.setText(scanner.nextLine());
                
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    names[i].setText(scanner.nextLine());
                    score[i].setText(scanner.nextLine());
                }
                
                scanner.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            return true;
        }
       
        private void changeColor(boolean mouseOn){
            Paint color = mouseOn ? Design.YELLOW : Design.GREY;
            
            gameMode.setFill(color);
            round.setFill(color);
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                names[i].setFill(color);
                score[i].setFill(color);
            }
        }
    }
    
}
