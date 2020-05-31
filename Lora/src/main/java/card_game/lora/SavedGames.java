/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.lora.game_modes.GameModes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 *
 * @author stepa
 */
public class SavedGames extends VBox{
    
    private final int ROWS = 3;
    private final int COLS = 4;
    private final StackPane stack;
    
    public SavedGames(double width, double height, StackPane stack){
        this.stack = stack;
        
        //------------------Saved games--------------
        GridPane savesGrid = new GridPane();
        savesGrid.setAlignment(Pos.CENTER);
        savesGrid.setVgap(3);
        savesGrid.setHgap(4);
        
        double saveBoxWidth = width / 5;
        double saveBoxHeight = height / 5;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                SaveBox saveBox = new SaveBox(
                        saveBoxWidth, saveBoxHeight, i * COLS + j
                );
                savesGrid.add(saveBox, j, i, 1, 1);
            }
        }
        
        //------------------Top bar-------------------
        Text name = new Text("Saved games");
        name.setFont(Design.Font(height / 20));
        
        ToggleButton returnButton = new Design.Button(
                width / 10, height / 20
        );
        returnButton.setText("Return");
        returnButton.setOnAction(e -> {
            hide();
        });
        
        HBox textBox = new HBox(name);
        textBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        
        HBox returnBox = new HBox(returnButton);
        returnBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(returnBox, Priority.ALWAYS);
        
        HBox topBar = new HBox(textBox, returnBox);
        
        //--------------------this--------------------
        
        this.setAlignment(Pos.CENTER);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
        this.getChildren().addAll(topBar, savesGrid);
        this.setBackground(new Background(new BackgroundFill(
                Design.BLUE, CornerRadii.EMPTY, Insets.EMPTY
        )));
    }
    
    public void show(){
        stack.getChildren().add(this);
    }
    
    private void hide(){
        stack.getChildren().remove(this);
    }
    
    public class SaveBox extends GridPane{
        
        private final int NUM_OF_PLAYERS = 4;
        private final Text[] names = new Text[NUM_OF_PLAYERS];
        private final Text[] score = new Text[NUM_OF_PLAYERS];
        private final Text gameMode = new Text("");
        private final Text round = new Text("");
        private final String path;
        
        private boolean hasSaved = false;
        
        public SaveBox(double width, double height, int index){
            path = "/saves/save" + index + ".txt";
            this.setVgap(5);
            this.setHgap(2);
            this.setMinSize(width, height);
            this.setMaxSize(width, height);
            this.setBackground(new Background(new BackgroundFill(
                    Design.BROWN, new CornerRadii(15), Insets.EMPTY
            )));
            this.setOnMouseEntered(e -> {
                changeColor(true);
            });
            this.setOnMouseExited(e -> {
                changeColor(false);
            });
            
            double textSize = height / 6;
            
            gameMode.setFont(Design.Font(textSize));
            gameMode.setFill(Design.GREY);
            this.add(gameMode, 0, 0, 1, 1);
            
            round.setFont(Design.Font(textSize));
            round.setFill(Design.GREY);
            this.add(round, 1, 0, 1, 1);
            
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                names[i] = new Text("");
                names[i].setFont(Design.Font(textSize));
                names[i].setFill(Design.GREY);
                this.add(names[i], 0, i+1, 1, 1);
                
                score[i] = new Text("");
                score[i].setFont(Design.Font(textSize));
                score[i].setFill(Design.GREY);
                this.add(score[i], 1, i+1, 1, 1);
            }
            
            loadFile();
        }
        
        public boolean delete(){
            boolean ret = true;
            try {
                PrintWriter writer = new PrintWriter(path);
                writer.write("");
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
                ret = false;
            }
            
            return ret;
        }
        
        public void save(String[] names, String[] score, GameModes gameMode, int round){
            for (int i = 0; i < NUM_OF_PLAYERS; i++){
                this.names[i].setText(names[i]);
                this.score[i].setText(score[i]);
            }
            this.gameMode.setText(gameMode.toString());
            this.round.setText(Integer.toString(round));
            
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
                
                bufWriter.write(gameMode.toString());
                bufWriter.newLine();
                bufWriter.write(Integer.toString(round));
                bufWriter.newLine();
                
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    bufWriter.write(names[i]);
                    bufWriter.newLine();
                    bufWriter.write(score[i]);
                    bufWriter.newLine();
                }
                
                bufWriter.close();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void loadFile(){
            File file = new File(path);
            if (!file.exists() || file.length() == 0){
                return;
            }
            
            try {
                FileReader reader = new FileReader(file);
                BufferedReader bufReader = new BufferedReader(reader);
                gameMode.setText(bufReader.readLine());
                round.setText(bufReader.readLine());
                
                for (int i = 0; i < NUM_OF_PLAYERS; i++){
                    names[i].setText(bufReader.readLine());
                    score[i].setText(bufReader.readLine());
                }
                
                bufReader.close();
                reader.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SavedGames.class.getName()).log(Level.SEVERE, null, ex);
            }
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
